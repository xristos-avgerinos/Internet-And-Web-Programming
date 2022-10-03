<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import ="java.sql.*,Servlets.AdminServlet"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Admin Profile</title>
<link rel="stylesheet" type="text/css" href="css/AdminProfile.css">
</head>
<body style="background-color:#bdd0d6;">
<%
response.setHeader("Cache-Control","no-cache,no-store,must_revalidate");

if(session.getAttribute("userid")==null){
	response.sendRedirect("adminLogin.html");
}
%>
	
<h1 style="text-align:center;font-size:35px">Admin's Profile Card</h1>

<div class="card">
<form action="AdminServlet" method="post">
<button name="LogoutButton" type="submit" value="Logout" style="position:relative;top:10px;left:330px;width:120px;color:white;background-color:#808080;">Logout</button>
</form> 
  
  <img src="images/admin.png" alt="admin" style="width:300px; height:180px;">
  <br><br><br><br>
  <p>Username: <%= session.getAttribute("username")%></p>
 
 
  <p><button style="background-color: #2B547E;"onclick="javascript:window.location.href='AdminServlet?action=listDoctor';">Registered Doctors</button></p>
  <br>
  <h3 style="text-align: right; "> <strong>  <a href="SignUpAdmin.jsp"><span>  Create a new admin   </span></a></strong> </h3>				
  
</div>

</body>
</html>