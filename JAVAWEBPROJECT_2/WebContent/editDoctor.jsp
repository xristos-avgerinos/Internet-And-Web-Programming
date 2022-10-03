<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link REL=STYLESHEET
      HREF="css/JSP-Styles.css"
      TYPE="text/css">
<script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="js/jquery-ui-1.8.18.custom.min.js"></script>
<title>Edit Doctor</title>
</head>
<body>

    	
<%
response.setHeader("Cache-Control","no-cache,no-store,must_revalidate");

if(session.getAttribute("userid")==null){
	response.sendRedirect("adminLogin.html");
}
%>
<div>
     <h2>Edit Doctor </h2>
 <input type="button" onclick="location.href='adminProfile.jsp';" value="Back to Profile" class="myButton" style="float:right;top:5%;"/>
</div>
   
    
    <form method="POST" action='AdminServlet' name="frmeditDoctor">
        AMKA : <input type="text" readonly="readonly"  name="doctorAMKA" value="<c:out value="${Doctor.getAMKA()}" />" /> <br /> 
        Username : <input type="text" readonly="readonly" name="username" value="<c:out value="${Doctor.getUsername()}" />" /> <br /> 
         <input type="hidden" name="hashedpassword" value="<c:out value="${Doctor.getPassword()}" />" /> <br /> 
         Password: <input type="password" name="newpassword" value="" /> <br /> 
         Name : <input type="text" name="name" value="<c:out value="${Doctor.getName()}" />" /> <br />
         Surname : <input type="text" name="surname" value="<c:out value="${Doctor.getSurname()}" />" /> <br />  
         <label for="specialty">Choose a Specialty: </label>
 	 <select name="specialty" >
     <option selected="selected">${Doctor.getSpecialty()}</option>
     <option >Pathologist</option>
     <option >Ophthalmologist</option>
      <option >Orthopedic</option>
     </select>
  <input type="submit" value="Submit" name = "edit" />
    </form>
</body>
</html>