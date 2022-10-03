package Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Classes.Confirmed_appointments;
import Classes.Encryption;
import Classes.Patient;
import dao.patientDao;

@WebServlet("/PatientServlet")
public class PatientServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	
	public static patientDao dao;
	
	private static String INSERT_Appointment = "/addPatientAppointment.jsp";
    private static String LIST_PendingAppointments = "/PatientsListPendingAppointments.jsp";
    
	public PatientServlet() {
		super();
        dao = new patientDao();
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out=response.getWriter();
		String forward="";
        String action = request.getParameter("action");
        response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");
		HttpSession session=request.getSession();
		boolean tempDelete = false;
		boolean tempBook = false;
        if (action.equalsIgnoreCase("delete")){
        	Date sqlDate = null;
        	Time StartSlotTime=null;
        	Time EndSlotTime=null;
        	try {
        		java.util.Date date = new SimpleDateFormat("yyyy-MM-dd").parse( request.getParameter("date"));
				sqlDate = new java.sql.Date(date.getTime());
				
				DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
				StartSlotTime = new Time(formatter.parse(request.getParameter("StartSlotTime")).getTime());
				
				EndSlotTime = new Time(formatter.parse(request.getParameter("EndSlotTime")).getTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        	String patientAMKA = session.getAttribute("patientAMKA").toString();
        	String doctorAMKA =  request.getParameter("doctorAMKA");
        	
        	LocalDate tempdate=LocalDate.now();
			tempdate=tempdate.plusDays(3);
			Date datenowplus3=Date.valueOf(tempdate);
			
			//System.out.println(sqlDate+" "+datenowplus3 );
			if(datenowplus3.before(sqlDate)){
				Confirmed_appointments conf = new Confirmed_appointments(sqlDate,StartSlotTime,EndSlotTime,patientAMKA,doctorAMKA);
				dao.deleteAppointment(conf);
			}else {
				tempDelete = true;
			}
            
        	forward = LIST_PendingAppointments;
            request.setAttribute("PendingAppointments", dao.getAllPendingAppointments(patientAMKA)); 
            
        }  else if (action.equalsIgnoreCase("ListPendingAppointments")){
            forward = LIST_PendingAppointments;
            String patientAMKA = session.getAttribute("patientAMKA").toString();
            request.setAttribute("PendingAppointments", dao.getAllPendingAppointments(patientAMKA));
            
        } else if(action.equalsIgnoreCase("insert")){
        	String specialty =  request.getParameter("specialty");
            forward = INSERT_Appointment;
            request.setAttribute("AvailableAppointments", dao.getAvailableAppointmentsBySpecialty(specialty));            
        }else if((action.equalsIgnoreCase("book"))) {
        	Date sqlDate = null;
        	Time StartSlotTime=null;
        	Time EndSlotTime=null;
        	try {
        		java.util.Date date = new SimpleDateFormat("yyyy-MM-dd").parse( request.getParameter("date"));
				sqlDate = new java.sql.Date(date.getTime());
				
				
				DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
				StartSlotTime = new Time(formatter.parse(request.getParameter("StartSlotTime")).getTime());
				
				EndSlotTime = new Time(formatter.parse(request.getParameter("EndSlotTime")).getTime());
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        	String doctorAMKA =  request.getParameter("doctorAMKA");
        	String patientAMKA = session.getAttribute("patientAMKA").toString();
        	Confirmed_appointments conf = new Confirmed_appointments(sqlDate,StartSlotTime,EndSlotTime,patientAMKA,doctorAMKA);
        	dao.BookAppointment(conf);
        	tempBook = true;
        	forward = LIST_PendingAppointments;
            request.setAttribute("PendingAppointments", dao.getAllPendingAppointments(patientAMKA));
        }

        RequestDispatcher view = request.getRequestDispatcher(forward);
        
        if(tempDelete) {
        	view.include(request, response);
        	out.println("<script>" + "alert(\"Can't delete appointment because it's less than 3 days later\");" +
				    "</script>");
        }else if(tempBook){
        	view.include(request, response);
        	out.println("<script>" + "alert(\"Appointment booked succesfully\");" +
				    "</script>");
        }else {
        	view.forward(request, response);
        }
		
	}
	public void doPost(HttpServletRequest req,HttpServletResponse res) throws ServletException,IOException{
		
		HttpSession session=req.getSession();
		if(session.getAttribute("doctorAMKA")!=null) {
			DoctorServlet.Logout(req, res);
		}else if(session.getAttribute("userid")!=null) {
			AdminServlet.Logout(req, res);
		}
		
		if(req.getParameter("LoginButton")!=null){
			Login(req,res);
			
		}else if(req.getParameter("SignupButton")!=null){
			SignupPatient(req,res);
		}
		else if(req.getParameter("LogoutButton")!=null) {
			Logout(req,res);
			res.sendRedirect("patientLogin.html");
		}
	}
	
	
	public void Login(HttpServletRequest req,HttpServletResponse res) throws IOException, ServletException{
		PrintWriter out=res.getWriter();
		String uname=req.getParameter("username");
		String pass=req.getParameter("pass");
		String name=null;
		String surname=null;
		String patientAMKA=null;
		
		String sql="Select * from patient where username=?";
		boolean found=false;
		boolean exceptionThrown = false;
		try {
		
		PreparedStatement st=dao.connection.prepareStatement(sql);
		st.setString(1, uname);
		
		
		ResultSet rs = st.executeQuery();
		
		boolean next=rs.next();
		if(next) { 
			String salt=rs.getString("salt");
			String newhashedpass=Encryption.getHashMD5(pass, salt);
			
			
			while(next) {
				
				if (uname.equals(rs.getString("username")) && (newhashedpass.equals(rs.getString("hashedpassword") ))  ){
					found=true;
					name=rs.getString("name");
					surname=rs.getString("surname");
					patientAMKA=rs.getString("patientAMKA");
					break;
				}
				next=rs.next();
			}
		}
		//rs.close();
		//con.close();
		}catch (SQLException sqle) {
			exceptionThrown = true;
			req.getRequestDispatcher("patientLogin.html").include(req, res);
			out.println("<script>" + "alert(\"Database connection problem.\");" +
				    "</script>");
		
		}
			
		if(found){
			HttpSession session=req.getSession();
			session.setAttribute("username", uname);
			session.setAttribute("pass", pass);
			session.setAttribute("name", name);
			session.setAttribute("surname", surname);
			session.setAttribute("patientAMKA", patientAMKA);
			
			
			res.sendRedirect("patientProfile.jsp");
		}else {
			if(!exceptionThrown) {
				
				req.getRequestDispatcher("patientLogin.html").include(req, res);
				out.println("<script>" + "alert(\"Account wasn't found\");" +
					    "</script>");
			}
		}
		out.flush();
		out.close();
	}
	
	
	
	
	public void SignupPatient(HttpServletRequest req,HttpServletResponse res) throws IOException, ServletException{
		    PrintWriter out=res.getWriter();
		    String patientAMKA = req.getParameter("AMKA");
			String username = req.getParameter("username");
			String password = req.getParameter("pass");
			String name = req.getParameter("First name");
			String surname = req.getParameter("Last name");
			if(!isNullOrBlankOrWhiteSpace(patientAMKA)&&!isNullOrBlankOrWhiteSpace(username)&&!isNullOrBlankOrWhiteSpace(password)&&!isNullOrBlankOrWhiteSpace(name)&&!isNullOrBlankOrWhiteSpace(surname)){
				Patient patient = new Patient(username,password,name,surname,patientAMKA);
				dao.addPatient(patient, req, res);
		
		   }else {
			
			   req.getRequestDispatcher("SignUpPatient.html").include(req, res);
			   out.println("<script>" + "alert(\"Inputs  cannot be blank or contain white spaces\");" +
					    "</script>"); 
			   
		   }

			out.flush();
			out.close();		
	}
	
	public static boolean isNullOrBlankOrWhiteSpace(String value) {

	    if (value == null) {
	         return true;
	    }
	    if (value.length() == 0) {
	        return true;
	    }
	    for (int i = 0; i < value.length(); i++) {
	        if (Character.isWhitespace(value.charAt(i))) {
	            return true;
	        }
	    }

	    return false;
	}
	
	
	public static void Logout(HttpServletRequest req,HttpServletResponse res) throws ServletException,IOException{

	HttpSession session=req.getSession();
	session.removeAttribute("username");
	session.removeAttribute("pass");
	session.removeAttribute("name");
	session.removeAttribute("surname");
	session.removeAttribute("patientAMKA");
	session.invalidate();
	}
}
