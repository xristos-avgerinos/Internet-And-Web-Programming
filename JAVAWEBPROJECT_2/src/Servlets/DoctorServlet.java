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
import Classes.Available_appointments;
import Classes.Confirmed_appointments;
import Classes.Encryption;
import dao.doctorDao;

@WebServlet("/DoctorServlet")
public class DoctorServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	public static doctorDao dao;
	private static String INSERT_AvailableAppointment = "/insertAvailableAppointment.jsp";
    private static String LIST_PendingAppointments = "/DoctorsListPendingAppointments.jsp";
    private static String LIST_AvailableAppointments = "/ListAppointmentAvailability.jsp";
    private static String EDIT_AvailableAppointments = "/EditAppointmentAvailability.jsp";
    
    String dateFactor = null;
    Available_appointments EditAvailableAppointment=null;
    
	public DoctorServlet() {
		super();
        dao = new doctorDao();
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
		
        if (action.equalsIgnoreCase("deleteAvailableAppointment")){
        	
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
				e.printStackTrace();
			}
        	
        	String doctorAMKA = session.getAttribute("doctorAMKA").toString();
        	Available_appointments AvailableAppointment = new Available_appointments(sqlDate,StartSlotTime,EndSlotTime,doctorAMKA);
        	dao.deleteAvailableAppointment(AvailableAppointment);
            forward = LIST_AvailableAppointments;
            request.setAttribute("AvailableAppointments", dao.getAllAvailableAppointmentsByDoctorAMKA(session.getAttribute("doctorAMKA").toString())); 
            
        }else if(action.equalsIgnoreCase("ListAppointmentAvailability")) {
        	 forward = LIST_AvailableAppointments;
             request.setAttribute("AvailableAppointments", dao.getAllAvailableAppointmentsByDoctorAMKA(session.getAttribute("doctorAMKA").toString()));
        }
        else if(action.equalsIgnoreCase("ListPendingAppointments")){
            dateFactor = request.getParameter("date");
        	if(dateFactor!=null) {
        		if(dateFactor.equalsIgnoreCase("week")) {
        			request.setAttribute("PendingAppointments", dao.getAllPendingAppointmentsbyDate(7,session.getAttribute("doctorAMKA").toString()));
        		}else if(dateFactor.equalsIgnoreCase("month")) {
        			request.setAttribute("PendingAppointments", dao.getAllPendingAppointmentsbyDate(30,session.getAttribute("doctorAMKA").toString()));
        		}
        		request.setAttribute("dateFactor", dateFactor);
        	}else {
        		request.setAttribute("dateFactor", "week");
        	}
            forward = LIST_PendingAppointments;
            
        } else if(action.equalsIgnoreCase("insertAvailableAppointemnt")){
              forward = INSERT_AvailableAppointment;

        } else if(action.equalsIgnoreCase("editAvailableAppointment")) {
        	forward = EDIT_AvailableAppointments;
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
        	
        	String doctorAMKA = session.getAttribute("doctorAMKA").toString();
        	
        
        	EditAvailableAppointment = new Available_appointments(sqlDate,StartSlotTime,EndSlotTime,doctorAMKA);
            request.setAttribute("AvailableAppointment", EditAvailableAppointment);
        }
        else if((action.equalsIgnoreCase("deleteConfirmedAppointment"))) {
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
        	
        	String doctorAMKA = session.getAttribute("doctorAMKA").toString();
        	String patientAMKA = request.getParameter("patientAMKA");
        	
        	LocalDate tempdate=LocalDate.now();
			tempdate=tempdate.plusDays(3);
			Date datenowplus3=Date.valueOf(tempdate);//cast
			
			//System.out.println(sqlDate+" "+datenowplus3 );
			if(datenowplus3.before(sqlDate)){
				Confirmed_appointments conf = new Confirmed_appointments(sqlDate,StartSlotTime,EndSlotTime,patientAMKA,doctorAMKA);
				dao.deleteConfirmedAppointment(conf);
			}else {
				tempDelete = true;
			}
			
			if(dateFactor!=null) {
        		if(dateFactor.equalsIgnoreCase("week")) {
        			request.setAttribute("PendingAppointments", dao.getAllPendingAppointmentsbyDate(7,session.getAttribute("doctorAMKA").toString()));
        		}else if(dateFactor.equalsIgnoreCase("month")) {
        			request.setAttribute("PendingAppointments", dao.getAllPendingAppointmentsbyDate(30,session.getAttribute("doctorAMKA").toString()));
        		}
        		request.setAttribute("dateFactor", dateFactor);
        	}else {
        		request.setAttribute("dateFactor", "week");
        	}
        	forward = LIST_PendingAppointments;
        }

        RequestDispatcher view = request.getRequestDispatcher(forward);
        
        if(tempDelete) {
        	view.include(request, response);
        	out.println("<script>" + "alert(\"Can't delete appointment because it's less than 3 days later\");" +
				    "</script>");
        }else {
        	view.forward(request, response);
        }
		
	}
	
	public void doPost(HttpServletRequest req,HttpServletResponse res) throws ServletException,IOException{
		
		res.setContentType("text/html; charset=UTF-8");
		res.setCharacterEncoding("UTF-8");
		req.setCharacterEncoding("UTF-8");
		
		HttpSession session=req.getSession();
		
		if(session.getAttribute("patientAMKA")!=null) {
			PatientServlet.Logout(req, res);
		}else if(session.getAttribute("userid")!=null) {
			 AdminServlet.Logout(req, res);
		 }
		 
		
		if(req.getParameter("LoginButton")!=null){
			Login(req,res);
			
		}
		else if(req.getParameter("LogoutButton")!=null) {
			Logout(req,res);
			res.sendRedirect("doctorLogin.html");
		}else if (req.getParameter("editAvailableAppointment")!=null){ 
			
			Date sqlDate = null;
        	Time StartSlotTime=null;
        	Time EndSlotTime=null;
        	
        	try {
        		java.util.Date date = new SimpleDateFormat("yyyy-MM-dd").parse( req.getParameter("date"));
				sqlDate = new java.sql.Date(date.getTime());
				
				DateFormat formatter = new SimpleDateFormat("HH:mm");
				StartSlotTime = new Time(formatter.parse(req.getParameter("StartSlotTime")).getTime());
				
				EndSlotTime = new Time(formatter.parse(req.getParameter("EndSlotTime")).getTime());
				
			} catch (ParseException e) {
				e.printStackTrace();
			}
        	String doctorAMKA = session.getAttribute("doctorAMKA").toString();
			Available_appointments av_appointment = new Available_appointments(sqlDate,StartSlotTime,EndSlotTime,doctorAMKA);
			if(EditAvailableAppointment!=null) {
				dao.updateAvailableAppointment(av_appointment,EditAvailableAppointment);
			}
			RequestDispatcher view = req.getRequestDispatcher(LIST_AvailableAppointments);
			req.setAttribute("AvailableAppointments", dao.getAllAvailableAppointmentsByDoctorAMKA(session.getAttribute("doctorAMKA").toString()));
			view.forward(req, res);
		}else if (req.getParameter("insertAvailableAppointment")!=null){
			Date sqlDate = null;
        	Time StartSlotTime=null;
        	Time EndSlotTime=null;
        	
        	try {
        		java.util.Date date = new SimpleDateFormat("yyyy-MM-dd").parse( req.getParameter("date"));
				sqlDate = new java.sql.Date(date.getTime());
				
				DateFormat formatter = new SimpleDateFormat("HH:mm");
				StartSlotTime = new Time(formatter.parse(req.getParameter("StartSlotTime")).getTime());
				
				EndSlotTime = new Time(formatter.parse(req.getParameter("EndSlotTime")).getTime());
				
			} catch (ParseException e) {
				e.printStackTrace();
			}
        	String doctorAMKA = session.getAttribute("doctorAMKA").toString();
			Available_appointments av_appointment = new Available_appointments(sqlDate,StartSlotTime,EndSlotTime,doctorAMKA);
			dao.insertAvailableAppointment(av_appointment,req,res);
			
		}
		
	}
	
	public void Login(HttpServletRequest req,HttpServletResponse res) throws IOException, ServletException{
		PrintWriter out=res.getWriter();
		String uname=req.getParameter("username");
		String pass=req.getParameter("pass");
		String name=null;
		String surname=null;
		String doctorAMKA=null;
		
		String sql="Select * from doctor where username=?";
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
			//System.out.println(salt + "= " + newhashedpass);
		
		
			while(next) {
				
				if (uname.equals(rs.getString("username")) && (newhashedpass.equals(rs.getString("hashedpassword") ))  ){
					found=true;
					name=rs.getString("name");
					surname=rs.getString("surname");
					doctorAMKA=rs.getString("doctorAMKA");
					break;
				}
				next=rs.next();
			}
		 }
		//rs.close();
		//con.close();
		}catch (SQLException sqle) {
			exceptionThrown = true;
			//sqle.printStackTrace();
			req.getRequestDispatcher("doctorLogin.html").include(req, res);
			out.println("<script>" + "alert(\"Database connection problem.\");" +
				    "</script>");
		
		}
		
		if(found){
			HttpSession session=req.getSession();
			session.setAttribute("username", uname);
			session.setAttribute("pass", pass);
			session.setAttribute("name", name);
			session.setAttribute("surname", surname);
			session.setAttribute("doctorAMKA", doctorAMKA);
			
			
			res.sendRedirect("doctorProfile.jsp");
		} else { 
			if(!exceptionThrown) {
				req.getRequestDispatcher("doctorLogin.html").include(req, res);
				out.println("<script>" + "alert(\"Account wasn't found\");" + "</script>");
			 }
		}
		
		out.flush();
		out.close();
	}
	
	
	public static void Logout(HttpServletRequest req,HttpServletResponse res) throws ServletException,IOException{

	HttpSession session=req.getSession();
	session.removeAttribute("username");
	session.removeAttribute("pass");
	session.removeAttribute("name");
	session.removeAttribute("surname");
	session.removeAttribute("doctorAMKA");
	session.invalidate();
	}
}
