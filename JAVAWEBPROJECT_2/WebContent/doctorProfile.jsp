<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<!DOCTYPE html>
<html>
<head>
	
<meta charset="UTF-8">
<title>Doctor Profile</title>

<link rel="stylesheet" type="text/css" href="css/AdminProfile.css">
</head>
<body style="background-color:#bdd0d6;">
	
	
<% 

response.setHeader("Cache-Control","no-cache,no-store,must_revalidate");

if(session.getAttribute("doctorAMKA")==null){
	response.sendRedirect("doctorLogin.html");
}
%>
	

<h1 style="text-align:center;font-size:35px">Doctor's Profile Card</h1>

<div class="card">
<form action="DoctorServlet" method="post">
<button name="LogoutButton" type="submit" value="Logout" style="position:relative;top:10px;left:330px;width:120px;color:white;background-color:#808080;">Logout</button>
</form> 
  
  <img src="images/doctors.png" alt="doctors" style="width:350px; height:240px;">
  <h2>Username: <%= session.getAttribute("username")%></h2>
  <p >Name: <%=session.getAttribute("name") %> </p>
  <p> Surname: <%= session.getAttribute("surname")%> </p>
  <p>AMKA: <%=session.getAttribute("doctorAMKA") %></p>

 
 
  <p><button style="background-color: #3366cc;"onclick="javascript:window.location.href='DoctorServlet?action=ListAppointmentAvailability';">Declare Appointment Availability</button></p>
  <p><button style="background-color: #3366cc;" onclick="javascript:window.location.href='DoctorServlet?action=ListPendingAppointments';">Pending Appointments</button></p>
  <br>
</div>
</body>
</html>

