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
import javax.servlet.http.HttpSession;


import Classes.Available_appointments;
import Classes.Confirmed_appointments;
import util.DbUtil;

public class doctorDao {
	public Connection connection;

    public doctorDao() {
        connection = DbUtil.getConnection();
    }
    private static String LIST_AvailableAppointments = "/ListAppointmentAvailability.jsp";
    private static String INSERT_AvailableAppointment = "/insertAvailableAppointment.jsp";
    
    public void insertAvailableAppointment(Available_appointments av_appointment,HttpServletRequest req,HttpServletResponse res) throws IOException, ServletException {
    	PrintWriter out=res.getWriter();
    	HttpSession session=req.getSession();
    	try {
		    PreparedStatement insertappointment = connection.prepareStatement(av_appointment.addAvailableAppointment());
		    insertappointment.setDate(1,av_appointment.getDate());
		    insertappointment.setTime(2, av_appointment.getStartSlotTime());
		    insertappointment.setTime(3,  av_appointment.getEndSlotTime());
		    insertappointment.setString(4, av_appointment.getDoctorAMKA());
		    insertappointment.setInt(5, 1);
		    insertappointment.executeUpdate();
			req.setAttribute("AvailableAppointments", getAllAvailableAppointmentsByDoctorAMKA(session.getAttribute("doctorAMKA").toString()));
			req.getRequestDispatcher(LIST_AvailableAppointments).include(req, res);
			out.println("<script>" + "alert(\"Successful insertion of available appointment\");" +
				    "</script>"); 
			
		} catch(SQLException sqle ) {
			//sqle.printStackTrace();	
			req.getRequestDispatcher(INSERT_AvailableAppointment).include(req, res);
			out.println("<script>" + "alert(\"This available appointment already exists.Please give a new available appointment.\");" +
				    "</script>"); 
		
		}
    }
    public List<Confirmed_appointments> getAllPendingAppointmentsbyDate(int dateFactor,String doctorAMKA) {
        List<Confirmed_appointments> PendingAppointments = new ArrayList<Confirmed_appointments>();
        try {
            PreparedStatement statement = connection.prepareStatement("Select * from confirmed_appointments as A natural join patient as P where date >= current_date() and date <date_add(current_date(), interval ? day) and doctorAMKA=? order by date");
            statement.setInt(1, dateFactor);
            statement.setString(2, doctorAMKA);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Confirmed_appointments Pending_appointment = new Confirmed_appointments(rs.getDate("date"),rs.getTime("startSlotTime"),rs.getTime("endSlotTime"),rs.getString("patientAMKA"),rs.getString("doctorAMKA"));
                Pending_appointment.setPatientNAME(rs.getString("P.name"));
                PendingAppointments.add(Pending_appointment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return PendingAppointments;
    }
    
    public void deleteAvailableAppointment(Available_appointments av) {
        try {
            PreparedStatement deleteAppointment = connection.prepareStatement("delete from available_appointments where date=? and startSlotTime=? and endSlotTime=? and doctorAMKA=?");
            deleteAppointment.setDate(1, av.getDate());
            deleteAppointment.setTime(2, av.getStartSlotTime());
            deleteAppointment.setTime(3, av.getEndSlotTime());
            deleteAppointment.setString(4, av.getDoctorAMKA());
            deleteAppointment.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void deleteConfirmedAppointment(Confirmed_appointments conf) {
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
    
    public List<Available_appointments> getAllAvailableAppointmentsByDoctorAMKA(String doctorAMKA) {
        List<Available_appointments> AvailableAppointments = new ArrayList<Available_appointments>();
        try {
            PreparedStatement statement = connection.prepareStatement("Select * from available_appointments as A natural join doctor as D where date >= now() and isAvailable=1 and doctorAMKA=? order by date");
            statement.setString(1,doctorAMKA);
            ResultSet rs = statement.executeQuery();
           
            while (rs.next()) {
                Available_appointments Available_appointment = new Available_appointments(rs.getDate("date"),rs.getTime("startSlotTime"),rs.getTime("endSlotTime"),rs.getString("doctorAMKA"));
                Available_appointment.setDoctorNAME(rs.getString("name"));
                AvailableAppointments.add(Available_appointment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return AvailableAppointments;
    }

	public void updateAvailableAppointment(Available_appointments new_appointment,Available_appointments old_appointment) {
		 try {
           PreparedStatement updateAvailableAppointment = connection.prepareStatement("update available_appointments  set date=? ,startSlotTime=? , endSlotTime=? where date=? and startSlotTime=? and endSlotTime=? and doctorAMKA=?");
           updateAvailableAppointment.setDate(1, new_appointment.getDate());
           updateAvailableAppointment.setTime(2, new_appointment.getStartSlotTime());
           updateAvailableAppointment.setTime(3, new_appointment.getEndSlotTime());

           updateAvailableAppointment.setDate(4, old_appointment.getDate());
           updateAvailableAppointment.setTime(5, old_appointment.getStartSlotTime());
           updateAvailableAppointment.setTime(6, old_appointment.getEndSlotTime());
           updateAvailableAppointment.setString(7, old_appointment.getDoctorAMKA());
           updateAvailableAppointment.executeUpdate();
          
       } catch (SQLException e) {
           e.printStackTrace();
       }
		
	}
    
}
