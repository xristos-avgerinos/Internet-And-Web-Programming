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
<title>Show All Pending Appointments</title>
</head>
<body onload="javascript:SetSelected()">
	
<% 

response.setHeader("Cache-Control","no-cache,no-store,must_revalidate");

if(session.getAttribute("doctorAMKA")==null){
	response.sendRedirect("doctorLogin.html");
}
%>
<div>
     <h2>All Pending Appointments</h2>
 <input type="button" onclick="location.href='doctorProfile.jsp';" value="Back to Profile" class="myButton" style="float:right;top:5%;"/>
</div>
    
    <p><a href="DoctorServlet" onclick="addSelectData(this) " style="font-size:25px;margin-left:17%">Show pending appointemnts by:</a>
    
    
    <select id= "date" name="date" style="height:30px;width:90px;font-size:20px; -webkit-appearance: menulist-button;">
    <option selected="selected" value="week" >Week</option>
    <option value="month">Month</option>
    </select>
    </p>
   

    <table border=1>
        <thead>
            <tr>
			<th>Date</th>
			<th>Start Slot Time</th>
			<th>End Slot Time</th>
			<th>Patients's AMKA</th>
			<th>Patients's Name</th>
			<th>Action</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${requestScope.PendingAppointments}" var="Ap">
                <tr>
                    <td><c:out value="${Ap.getDate()}" /></td>
                    <td><c:out value="${Ap.getStartSlotTime()}" /></td>
                    <td><c:out value="${Ap.getEndSlotTime()}" /></td>
                    <td><c:out value="${Ap.getPatientAMKA()}" /></td>
                    <td><c:out value="${Ap.getPatientNAME()}" /></td>
                    <td><a href="DoctorServlet?action=deleteConfirmedAppointment&patientAMKA=<c:out value="${Ap.getPatientAMKA()}"/>&date=<c:out value="${Ap.getDate()}"/>&StartSlotTime=<c:out value="${Ap.getStartSlotTime()}"/>&EndSlotTime=<c:out value="${Ap.getEndSlotTime()}"/>">Delete</a></td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    <input type="hidden" id="Factor" value="${dateFactor}">
    <script>
    	function addSelectData(e){
        	 e.href = e.href + "?action=ListPendingAppointments&date=" + document.getElementById('date').value;
   		 }

        function SetSelected(){
            var x=document.getElementById("Factor").value;
           document.getElementById('date').value =x ; 	 
            
                 }
    </script>
</body>
</html>