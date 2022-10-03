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
<body>

<% 

response.setHeader("Cache-Control","no-cache,no-store,must_revalidate");

if(session.getAttribute("patientAMKA")==null){
	response.sendRedirect("patientLogin.html");
}
%>
<div>
     <h2>All Pending Appointments </h2>
 <input type="button" onclick="location.href='patientProfile.jsp';" value="Back to Profile" class="myButton" style="float:right;top:5%;"/>
</div>
    <table border=1>
        <thead>
            <tr>
			<th>Date</th>
			<th>Start Slot Time</th>
			<th>End Slot Time</th>
			<th>Doctor's AMKA</th>
			<th>Doctor's Name</th>
			<th>Action</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${requestScope.PendingAppointments}" var="Ap">
                <tr>
                    <td><c:out value="${Ap.getDate()}" /></td>
                    <td><c:out value="${Ap.getStartSlotTime()}" /></td>
                    <td><c:out value="${Ap.getEndSlotTime()}" /></td>
                    <td><c:out value="${Ap.getDoctorAMKA()}" /></td>
                    <td><c:out value="${Ap.getDoctorNAME()}" /></td>
                    <td><a href="PatientServlet?action=delete&doctorAMKA=<c:out value="${Ap.getDoctorAMKA()}"/>&date=<c:out value="${Ap.getDate()}"/>&StartSlotTime=<c:out value="${Ap.getStartSlotTime()}"/>&EndSlotTime=<c:out value="${Ap.getEndSlotTime()}"/>">Delete</a></td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    <p><a href="PatientServlet" onclick="addSelectData(this)">Add Appointment with doctor's specialty :</a>
    
    <select id= "specialty" name="specialty" >
    <option value="Pathologist" >Pathologist</option>
    <option value="Ophthalmologist">Ophthalmologist</option>
	<option value="Orthopedic">Orthopedic</option>
    </select>
    </p>
    <script>
    	function addSelectData(e){
        	 e.href = e.href + "?action=insert&specialty=" + document.getElementById('specialty').value;
   		 }
    </script>
</body>
</html>