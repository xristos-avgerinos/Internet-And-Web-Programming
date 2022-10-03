package dao;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Classes.Admin;
import Classes.Doctor;
import Classes.Encryption;
import util.DbUtil;

public class adminDao {
	public  Connection connection;

    public adminDao() {
        connection = DbUtil.getConnection();
    }

    public void addAdmin(Admin admin,HttpServletRequest req,HttpServletResponse res) throws IOException, ServletException {
    	PrintWriter out=res.getWriter();
    	try {
				
				String salt=Encryption.createSalt().toString();
			    PreparedStatement insertAdmin = connection.prepareStatement(admin.registration());
			    
			    insertAdmin.setString(1, admin.getUsername());
			    insertAdmin.setString(2, Encryption.getHashMD5(admin.getPassword(),salt));
			    insertAdmin.setString(3, salt.toString());
			    insertAdmin.executeUpdate();
			    
				//insertAdmin.close();
				//connection.close(); 
				
				out.println("<script>" + "alert(\"Successful registration\");" +
			    "</script>"); 
				req.getRequestDispatcher("adminProfile.jsp").include(req, res);
				
			} catch(SQLException sqle ) {
				//sqle.printStackTrace();
				if(sqle.getMessage().contains(admin.getUsername())){
					
					req.getRequestDispatcher("SignUpAdmin.jsp").include(req, res);
					out.println("<script>" + "alert(\"This username is already in use.Please try an other username.\");" +
						    "</script>"); 
					
				}
				
			}
    }
    
    public void addDoctor(Doctor doctor,HttpServletRequest req,HttpServletResponse res) throws IOException, ServletException {
    	PrintWriter out=res.getWriter();
    	try {
    		HttpSession session=req.getSession();
			String salt=Encryption.createSalt().toString();
		    PreparedStatement insertDoctor = connection.prepareStatement(doctor.registration());
		    insertDoctor.setString(1,doctor.getAMKA());
		    insertDoctor.setString(2, doctor.getUsername());
		    insertDoctor.setString(3, Encryption.getHashMD5(doctor.getPassword(),salt));
		    insertDoctor.setString(4, doctor.getName());
		    insertDoctor.setString(5, doctor.getSurname());
		    insertDoctor.setString(6, doctor.getSpecialty());
		    int ADMIN_userid = Integer.parseInt( session.getAttribute("userid").toString());	    
		    insertDoctor.setInt(7, ADMIN_userid);
		    insertDoctor.setString(8, salt.toString());
		    insertDoctor.executeUpdate();
			
			out.println("<script>" + "alert(\"Successful registration\");" +
		    "</script>"); 

			RequestDispatcher view = req.getRequestDispatcher("listDoctor.jsp");
			req.setAttribute("Doctors", getAllDoctors()); 
			view.forward(req, res);
			
		} catch(SQLException sqle ) {
			//sqle.printStackTrace();
			if(sqle.getMessage().contains(doctor.getAMKA())) {
				
				req.getRequestDispatcher("addDoctor.jsp").include(req, res);
				out.println("<script>" + "alert(\"Wrong AMKA.Please give your real AMKA.\");" +
					    "</script>"); 
				
			}
			else if(sqle.getMessage().contains(doctor.getUsername())){
				
				req.getRequestDispatcher("addDoctor.jsp").include(req, res);
				out.println("<script>" + "alert(\"This username is already in use.Please try an other username.\");" +
					    "</script>"); 
				
			}
			
		}
    }
    

    public void deleteDoctor(String doctorAMKA) {
        try {
            PreparedStatement deleteDoctor = connection.prepareStatement("delete from doctor where doctorAMKA=?");
            deleteDoctor.setString(1, doctorAMKA);
            deleteDoctor.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateDoctor(Doctor doctor) {
        try {
        	if(doctor.getSalt()==null) {
	        	PreparedStatement updateDoctor= connection.prepareStatement("update doctor set  username=?, hashedPassword=?, name=?, surname=?, specialty=? where doctorAMKA=?");
	            
	            updateDoctor.setString(1, doctor.getUsername());
	            updateDoctor.setString(2, doctor.getPassword());
	            updateDoctor.setString(3, doctor.getName());
	            updateDoctor.setString(4, doctor.getSurname());
	            updateDoctor.setString(5, doctor.getSpecialty());
	            updateDoctor.setString(6, doctor.getAMKA());
	            updateDoctor.executeUpdate(); 
	            updateDoctor.close();
	            
        	}else {
        		PreparedStatement updateDoctor= connection.prepareStatement("update doctor set  username=?, hashedPassword=?, name=?, surname=?, specialty=?, salt=? where doctorAMKA=?");
	            
	            updateDoctor.setString(1, doctor.getUsername());
	            updateDoctor.setString(2, doctor.getPassword());
	            updateDoctor.setString(3, doctor.getName());
	            updateDoctor.setString(4, doctor.getSurname());
	            updateDoctor.setString(5, doctor.getSpecialty());
	            updateDoctor.setString(6, doctor.getSalt());
	            updateDoctor.setString(7, doctor.getAMKA());
	            updateDoctor.executeUpdate();  
        	}
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }

    public List<Doctor> getAllDoctors() {
        List<Doctor> Doctors = new ArrayList<Doctor>();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select * from doctor");
            while (rs.next()) {
                Doctor doctor = new Doctor(rs.getString("username"),rs.getString("hashedpassword"),rs.getString("name"),rs.getString("surname"),rs.getString("specialty"),rs.getString("doctorAMKA"));
                Doctors.add(doctor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Doctors;
    }

    public Doctor getDoctorBydoctorAMKA(String doctorAMKA) {
        Doctor doctor = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from Doctor where doctorAMKA=?");
            preparedStatement.setString(1, doctorAMKA);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
            	doctor = new Doctor(rs.getString("username"),rs.getString("hashedpassword"),rs.getString("name"),rs.getString("surname"),rs.getString("specialty"),rs.getString("doctorAMKA"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return doctor;
    }
}
