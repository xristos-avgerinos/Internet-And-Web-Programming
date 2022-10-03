<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import ="java.sql.*,Servlets.PatientServlet"%>
    
<!DOCTYPE html>
<html>
<head>
	
<meta charset="UTF-8">
<title>Patient Profile</title>

<link rel="stylesheet" type="text/css" href="css/PatientProfile.css">
</head>
<body style="background-color:#ebeeef;">
	
	
<% 

response.setHeader("Cache-Control","no-cache,no-store,must_revalidate");

if(session.getAttribute("patientAMKA")==null){
	response.sendRedirect("patientLogin.html");
}
else{
%>
	

<h1 style="text-align:center;font-size:35px">Patient's Profile Card</h1>

<div class="card">
	<form action="PatientServlet" method="post">
<button name="LogoutButton" type="submit" value="Logout" style="position:relative;top:10px;left:330px;width:120px;color:white;background-color:#808080;">Logout</button>
</form> 
  
  <img src="images/patients.jpg" alt="patients" style="width:450px; height:340px;">
  <h2>Username: <%= session.getAttribute("username")%></h2>
  <p >Name: <%=session.getAttribute("name") %> </p>
  <p> Surname: <%= session.getAttribute("surname")%> </p>
  <p>AMKA: <%=session.getAttribute("patientAMKA") %></p>

 
 
  <p><button style="background-color: #57b846;"onclick="document.getElementById('id01').style.display='block'">Appointment History</button></p>
  <p><button style="background-color: #57b846;" onclick="javascript:window.location.href='PatientServlet?action=ListPendingAppointments';">Pending Appointments</button></p>
  <br>
</div>

<div id="id01" class="modal">
  <span onclick="document.getElementById('id01').style.display='none'" class="close" title="Close Modal">&times;</span>  
  <img src="images/appointments.png" style="left:15%;position:relative;top:-10%;width:160px; height:140px;">
  <h1 style="left:35%;position:absolute;top:20px;">Appointment History</h1>
  
  
  <table style="margin-left: auto; margin-right: auto;font-size:20px;width:70%;height:10%">
			<tr>
			<th>Date</th>
			<th>Start Slot Time</th>
			<th>End Slot Time</th>
			<th>Doctor's AMKA</th>
			<th>Doctor's Name</th>
			</tr>
<%
try {
		
		String sql="Select * from confirmed_appointments as A natural join doctor as D where A.patientAMKA=? and date < now() order by date desc";
		
		
		PreparedStatement st=PatientServlet.dao.connection.prepareStatement(sql);
		
		st.setString(1, session.getAttribute("patientAMKA").toString());
		
		
		
		ResultSet rs = st.executeQuery();
		
		
		while(rs.next()) {
				String date = rs.getString("date");			    String sst = rs.getString("startSlotTime");
			    String est = rs.getString("endSlotTime");
				String doctorsAMKA = rs.getString("doctorAMKA");
				String doctorsName = rs.getString("D.name");
				
				StringBuilder row = new StringBuilder("<tr>");
				
				row.append("<td>" + date + "</td>");
				row.append("<td>" + sst + "</td>");
				row.append("<td>" + est + "</td>");
				row.append("<td>" + doctorsAMKA + "</td>");
				row.append("<td>" + doctorsName + "</td>");
				row.append("</tr>");
			    out.println(row);
		}
		
		//rs.close();
		//con.close();
		
		
		}catch (SQLException sqle) {
			sqle.printStackTrace();
			out.println("Database connection problem");
		
		}catch(Exception e){
			e.printStackTrace();	
		}
}
%>
   </table>
   


</div>
</body>
</html>

