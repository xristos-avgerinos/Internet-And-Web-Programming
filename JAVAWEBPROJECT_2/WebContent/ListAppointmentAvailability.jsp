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
<title>Show All Available Appointments</title>
</head>
<body>
	
<% 

response.setHeader("Cache-Control","no-cache,no-store,must_revalidate");

if(session.getAttribute("doctorAMKA")==null){
	response.sendRedirect("doctorLogin.html");
}
%>
<div>
     <h2>All Available Appointments</h2>
 <input type="button" onclick="location.href='doctorProfile.jsp';" value="Back to Profile" class="myButton" style="float:right;top:5%;"/>
</div>

    <table border=1>
        <thead>
            <tr>
			<th>Date</th>
			<th>Start Slot Time</th>
			<th>End Slot Time</th>
			<th colspan=2>Action</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${requestScope.AvailableAppointments}" var="Ap">
                <tr>
                    <td><c:out value="${Ap.getDate()}" /></td>
                    <td><c:out value="${Ap.getStartSlotTime()}" /></td>
                    <td><c:out value="${Ap.getEndSlotTime()}" /></td>
                    <td><a href="DoctorServlet?action=editAvailableAppointment&date=<c:out value="${Ap.getDate()}"/>&StartSlotTime=<c:out value="${Ap.getStartSlotTime()}"/>&EndSlotTime=<c:out value="${Ap.getEndSlotTime()}"/>">Update</a></td>
                    <td><a href="DoctorServlet?action=deleteAvailableAppointment&date=<c:out value="${Ap.getDate()}"/>&StartSlotTime=<c:out value="${Ap.getStartSlotTime()}"/>&EndSlotTime=<c:out value="${Ap.getEndSlotTime()}"/>">Delete</a></td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    <p><a href="DoctorServlet?action=insertAvailableAppointemnt">Add Available Appointment</a></p>

</body>
</html>