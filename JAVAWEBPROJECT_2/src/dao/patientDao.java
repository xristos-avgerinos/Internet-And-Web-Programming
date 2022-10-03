package dao;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import Classes.Available_appointments;
import Classes.Confirmed_appointments;
import Classes.Encryption;
import Classes.Patient;
import util.DbUtil;

public class patientDao {
	public Connection connection;

    public patientDao() {
        connection = DbUtil.getConnection();
    }

    public void addPatient(Patient patient,HttpServletRequest req,HttpServletResponse res) throws IOException, ServletException {
    	PrintWriter out=res.getWriter();
    	try {
			
			String salt=Encryption.createSalt().toString();
		    PreparedStatement insertPatient = connection.prepareStatement(patient.registration());
		    insertPatient.setString(1,patient.getAMKA());
		    insertPatient.setString(2, patient.getUsername());
		    insertPatient.setString(3, Encryption.getHashMD5(patient.getPassword(),salt));
		    insertPatient.setString(4, patient.getName());
		    insertPatient.setString(5, patient.getSurname());
		    insertPatient.setString(6, salt.toString());
		    insertPatient.executeUpdate();
		    
			//insertPatient.close();
			//con.close(); 
			
			
			out.println("<script>" + "alert(\"Successful registration\");" +
		    "</script>"); 
			req.getRequestDispatcher("patientLogin.html").include(req, res);
			
		} catch(SQLException sqle ) {
			//sqle.printStackTrace();
			if(sqle.getMessage().contains(patient.getAMKA())) {
				
				req.getRequestDispatcher("SignUpPatient.html").include(req, res);
				out.println("<script>" + "alert(\"Wrong AMKA.Please give your real AMKA.\");" +
					    "</script>"); 
				
			}
			else if(sqle.getMessage().contains(patient.getUsername())){
				
				req.getRequestDispatcher("SignUpPatient.html").include(req, res);
				out.println("<script>" + "alert(\"This username is already in use.Please try an other username.\");" +
					    "</script>"); 
				
			}
			
		}
    }
    
    public void deleteAppointment(Confirmed_appointments conf) {
        try {
            PreparedStatement deleteAppointment = connection.prepareStatement("delete from confirmed_appointments where date=? and patientAMKA=? and doctorAMKA=?");
            deleteAppointment.setDate(1, conf.getDate());
            deleteAppointment.setString(2, conf.getPatientAMKA());
            deleteAppointment.setString(3, conf.getDoctorAMKA());
            deleteAppointment.executeUpdate();

            PreparedStatement makeAppointemntavailable = connection.prepareStatement("update available_appointments set isAvailable=1 where date=? and startSlotTime=? and endSlotTime=? and doctorAMKA=?");
            makeAppointemntavailable.setDate(1, conf.getDate());
            makeAppointemntavailable.setTime(2, conf.getStartSlotTime());
            makeAppointemntavailable.setTime(3, conf.getEndSlotTime());
            makeAppointemntavailable.setString(4, conf.getDoctorAMKA());
            makeAppointemntavailable.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void BookAppointment(Confirmed_appointments conf) {
    	 try {
             PreparedStatement makeAppointemntUnavailable = connection.prepareStatement("update available_appointments set isAvailable=0 where date=? and startSlotTime=? and endSlotTime=? and doctorAMKA=?");
             makeAppointemntUnavailable.setDate(1, conf.getDate());
             makeAppointemntUnavailable.setTime(2, conf.getStartSlotTime());
             makeAppointemntUnavailable.setTime(3, conf.getEndSlotTime());
             makeAppointemntUnavailable.setString(4, conf.getDoctorAMKA());
             makeAppointemntUnavailable.executeUpdate();

             PreparedStatement addConfirmedAppointment = connection.prepareStatement(conf.addConfirmedAppointment());
             addConfirmedAppointment.setDate(1, conf.getDate());
             addConfirmedAppointment.setTime(2, conf.getStartSlotTime());
             addConfirmedAppointment.setTime(3, conf.getEndSlotTime());
             addConfirmedAppointment.setString(4, conf.getPatientAMKA());
             addConfirmedAppointment.setString(5, conf.getDoctorAMKA());
             addConfirmedAppointment.executeUpdate();
             
         } catch (SQLException e) {
             //e.printStackTrace();
         }
    }
    
    public List<Confirmed_appointments> getAllPendingAppointments(String patientAMKA) {
        List<Confirmed_appointments> PendingAppointments = new ArrayList<Confirmed_appointments>();
        try {
        	PreparedStatement statement = connection.prepareStatement("Select * from confirmed_appointments as A natural join doctor as D where  A.patientAMKA=? and date >= now() order by date");
            statement.setString(1, patientAMKA);
        	ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Confirmed_appointments Pending_appointment = new Confirmed_appointments(rs.getDate("date"),rs.getTime("startSlotTime"),rs.getTime("endSlotTime"),rs.getString("patientAMKA"),rs.getString("doctorAMKA"));
                Pending_appointment.setDoctorNAME(rs.getString("name"));
                PendingAppointments.add(Pending_appointment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return PendingAppointments;
    }
    
    public List<Available_appointments> getAvailableAppointmentsBySpecialty(String specialty) {
        List<Available_appointments> AvailableAppointments = new ArrayList<Available_appointments>();
        try {
            PreparedStatement Appointments = connection.prepareStatement("Select * from available_appointments as A natural join doctor as D where date >= now() and isAvailable=1 and specialty=? order by date");
            Appointments.setString(1, specialty);
            ResultSet rs = Appointments.executeQuery();
            while (rs.next()) {
            	Available_appointments Available_appointment = new Available_appointments(rs.getDate("date"),rs.getTime("startSlotTime"),rs.getTime("endSlotTime"),rs.getString("doctorAMKA"));
            	Available_appointment.setDoctorNAME(rs.getString("name"));
            	Available_appointment.setisAvailable(rs.getBoolean("isAvailable"));
            	AvailableAppointments.add(Available_appointment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return AvailableAppointments;
    }
}
