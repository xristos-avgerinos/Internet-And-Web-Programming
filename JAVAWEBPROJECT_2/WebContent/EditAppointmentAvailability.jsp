<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link REL=STYLESHEET
      HREF="css/JSP-Styles.css"
      TYPE="text/css">
<script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="js/jquery-ui-1.8.18.custom.min.js"></script>
<title>Edit Available Appointment</title>
</head>
<body>
		
<% 

response.setHeader("Cache-Control","no-cache,no-store,must_revalidate");

if(session.getAttribute("doctorAMKA")==null){
	response.sendRedirect("doctorLogin.html");
}
%>
<div>
     <h2>Edit Available Appointment </h2>
 <input type="button" onclick="location.href='doctorProfile.jsp';" value="Back to Profile" class="myButton" style="float:right;top:5%;"/>
</div>

 
    
    <form method="POST" action='DoctorServlet' name="frmeditAvailableAppointment">
		<label for="date"> Date:</label>
        <input type="date" id="date" name="date" value="<c:out value="${AvailableAppointment.getDate()}" />"  min="" max="2030-12-31"> <br /> 
        <label for="sst"> StartSlotTime:</label>
        <input type="time" step = "1800" name="StartSlotTime" id="sst" onChange="myFunction()" value="<c:out value="${AvailableAppointment.getStartSlotTime()}" />" min="08:00" max="23:00" > <br /> 
        <label for="est"> EndSlotTime:</label>
        <input type="text" readonly name="EndSlotTime" id="est" value="<c:out value="${AvailableAppointment.getEndSlotTime()}"/>"> <br /> 

   <input type="submit" value="Submit" name = "editAvailableAppointment" />
   </form>
    <script>
		var today = new Date();
		var dd = today.getDate();
		var mm = today.getMonth()+1; //January is 0 so need to add 1 to make it 1!
		var yyyy = today.getFullYear();
		if(dd<10){
		  dd='0'+dd
		} 
		if(mm<10){
		  mm='0'+mm
		} 
		
		today = yyyy+'-'+mm+'-'+dd;
		document.getElementById("date").setAttribute("min", today);


   function myFunction() {

		 var x = document.getElementById("sst").value;	 
		 var z=new Date("December 12, 2000 "+x);		 		 
		 z.setMinutes( z.getMinutes() + 30 );		 
		 
		 if(z.getHours()>=0 && z.getHours()<10){
		 	var p ="0"+z.getHours()+":";
		 }else{
		 	var p =z.getHours()+":";	 
		 } 
		 if(z.getMinutes()>=0 && z.getMinutes()<10){
		 	 p =p+"0"+z.getMinutes();
		 }else{
		  	 p =p+z.getMinutes(); 
		 }
		 
		 document.getElementById("est").setAttribute("value", p);
}
        
</script>
</body>
</html>