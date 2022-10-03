package Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import Classes.Admin;
import Classes.Doctor;
import Classes.Encryption;
import dao.adminDao;

@WebServlet("/AdminServlet")
public class AdminServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private static String INSERT_Doctor = "/addDoctor.jsp";
    private static String EDIT_Doctor = "/editDoctor.jsp";
    private static String LIST_Doctor = "/listDoctor.jsp";
    
	private static adminDao dao;
	
	public AdminServlet() {
		super();
        dao = new adminDao();
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String forward="";
        String action = request.getParameter("action");
        response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");

        if (action.equalsIgnoreCase("delete")){
            String doctorAMKA = request.getParameter("doctorAMKA");
            dao.deleteDoctor(doctorAMKA);
            forward = LIST_Doctor;
            request.setAttribute("Doctors", dao.getAllDoctors());    
        } else if (action.equalsIgnoreCase("edit")){
            forward = EDIT_Doctor;
            String doctorAMKA = request.getParameter("doctorAMKA");
            Doctor doctor = dao.getDoctorBydoctorAMKA(doctorAMKA);
            request.setAttribute("Doctor", doctor);
			
        } else if (action.equalsIgnoreCase("listDoctor")){
            forward = LIST_Doctor;
            request.setAttribute("Doctors", dao.getAllDoctors());
        } else {
            forward = INSERT_Doctor;
        }

        RequestDispatcher view = request.getRequestDispatcher(forward);
        view.forward(request, response);
	}
	public void doPost(HttpServletRequest req,HttpServletResponse res) throws ServletException,IOException{
		PrintWriter out=res.getWriter();
		res.setContentType("text/html; charset=UTF-8");
		res.setCharacterEncoding("UTF-8");
		req.setCharacterEncoding("UTF-8");
		
		HttpSession session=req.getSession();
		if(session.getAttribute("doctorAMKA")!=null) {
			DoctorServlet.Logout(req, res);
		}else if(session.getAttribute("patientAMKA")!=null) {
			PatientServlet.Logout(req, res);
		}
		
		if(req.getParameter("LoginButton")!=null){
			Login(req,res);
			
		}else if(req.getParameter("SignupButton")!=null){
			SignupAdmin(req,res);
		}
		else if(req.getParameter("LogoutButton")!=null) {
			Logout(req,res);
			res.sendRedirect("adminLogin.html");
		} else if (req.getParameter("edit")!=null){ 
			boolean temp = false;	
        	String doctorAMKA = req.getParameter("doctorAMKA");
        	String username = req.getParameter("username");
			String hashedpassword=req.getParameter("hashedpassword");
        	String password = req.getParameter("newpassword");
        	String name = req.getParameter("name");
        	String surname = req.getParameter("surname");
        	String specialty = req.getParameter("specialty");
			
			if (!isNullOrBlankOrWhiteSpace(name)  && !isNullOrBlankOrWhiteSpace(surname) ){

	        	if(isNullOrBlankOrWhiteSpace(password)) {
	        		Doctor doctor = new Doctor(username,hashedpassword,name,surname,specialty,doctorAMKA);
	        		dao.updateDoctor(doctor);
	        	}else{
					String salt=Encryption.createSalt().toString();
					Doctor doctor = new Doctor(username,Encryption.getHashMD5(password, salt),name,surname,specialty,doctorAMKA);
					doctor.setSalt(salt);
					dao.updateDoctor(doctor);
				}

			}else {
				temp = true;
				Doctor doctor = dao.getDoctorBydoctorAMKA(doctorAMKA);
		        req.setAttribute("Doctor", doctor);
		        req.getRequestDispatcher(EDIT_Doctor).include(req, res);
				out.println("<script>" + "alert(\"Inputs  cannot be blank or contain white spaces\");" +
						    "</script>");        
			 }
        
		
		  if(!temp) {
			   RequestDispatcher view = req.getRequestDispatcher(LIST_Doctor);
			   req.setAttribute("Doctors", dao.getAllDoctors()); 
			   view.forward(req, res);
		  }
			 
		
		}else if(req.getParameter("insert")!=null) {
			  addDoctor(req,res);
		}
		
    	
	}
	
	
	public void Login(HttpServletRequest req,HttpServletResponse res) throws IOException, ServletException{
		PrintWriter out=res.getWriter();
		String uname=req.getParameter("username");
		String pass=req.getParameter("pass");
		int userid = 0;
		
		String sql="Select * from admin where username=?";
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
			//System.out.println(newhashedpass);
			
			while(next) {
				
				if (uname.equals(rs.getString("username")) && (newhashedpass.equals(rs.getString("hashedpassword") ))  ){
					found=true;
					userid=rs.getInt("userid");
					break;
				}
				next=rs.next();
			}
		}
		
		//rs.close();
		//con.close();
		}catch (SQLException sqle) {
			sqle.printStackTrace();
			exceptionThrown = true;
			req.getRequestDispatcher("adminLogin.html").include(req, res);
			out.println("<script>" + "alert(\"Database connection problem.\");" +
				    "</script>");
		
		}
			
		if(found){
			HttpSession session=req.getSession();
			session.setAttribute("username", uname);
			session.setAttribute("pass", pass);
			session.setAttribute("userid", userid);
			
			res.sendRedirect("adminProfile.jsp");
		}else {
			if(!exceptionThrown) {
				
				req.getRequestDispatcher("adminLogin.html").include(req, res);
				out.println("<script>" + "alert(\"Account wasn't found\");" +
					    "</script>");
			}
		}
		out.flush();
		out.close();
	}
	
	
	
	
	public void SignupAdmin(HttpServletRequest req,HttpServletResponse res) throws IOException, ServletException{
	    PrintWriter out=res.getWriter();
	    
		String username = req.getParameter("username");
		String password = req.getParameter("pass");
		
		if(!isNullOrBlankOrWhiteSpace(username)&&!isNullOrBlankOrWhiteSpace(password)){
			Admin admin = new Admin(username,password);
			dao.addAdmin(admin,req,res);
	
	   }else {
		
		   req.getRequestDispatcher("SignUpAdmin.jsp").include(req, res);
		   out.println("<script>" + "alert(\"Inputs  cannot be blank or contain white spaces\");" +
				    "</script>"); 
		   
	   }

	out.flush();
	out.close();		
	}
	
	public void addDoctor(HttpServletRequest req,HttpServletResponse res) throws IOException, ServletException{
	    PrintWriter out=res.getWriter();
	    String doctorAMKA = req.getParameter("AMKA");
		String username = req.getParameter("username");
		String password = req.getParameter("pass");
		String name = req.getParameter("firstname");
		String surname = req.getParameter("lastname");
		String specialty = req.getParameter("specialty");
		if(!isNullOrBlankOrWhiteSpace(doctorAMKA)&&!isNullOrBlankOrWhiteSpace(username)&&!isNullOrBlankOrWhiteSpace(password)&&!isNullOrBlankOrWhiteSpace(name)&&!isNullOrBlankOrWhiteSpace(surname)){
			Doctor doctor = new Doctor(username,password,name,surname,specialty,doctorAMKA);
			dao.addDoctor(doctor, req, res);
	
	   }else {
		
		   req.getRequestDispatcher(INSERT_Doctor).include(req, res);
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
	session.removeAttribute("userid");
	session.invalidate();
	}
}
