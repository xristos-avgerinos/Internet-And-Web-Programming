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
<title>Show All Doctors</title>
</head>
<body>
    <%
response.setHeader("Cache-Control","no-cache,no-store,must_revalidate");

if(session.getAttribute("userid")==null){
	response.sendRedirect("adminLogin.html");
}
%>
 <div>
     <h2>All Doctors</h2>
 <input type="button" onclick="location.href='adminProfile.jsp';" value="Back to Profile" class="myButton" style="float:right;top:5%;"/>
</div>

    <table border=1>
        <thead>
            <tr>
                <th>Doctor's AMKA</th>
                <th>Username</th>
                <th>Name</th>
                <th>SurName</th>
                <th>Specialty</th>
                <th colspan=2>Action</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${requestScope.Doctors}" var="d">
                <tr>
                    <td><c:out value="${d.getAMKA()}" /></td>
                    <td><c:out value="${d.getUsername()}" /></td>
                    <td><c:out value="${d.getName()}" /></td>
                    <td><c:out value="${d.getSurname()}" /></td>
                    <td><c:out value="${d.getSpecialty()}" /></td>
                    <td><a href="AdminServlet?action=edit&doctorAMKA=<c:out value="${d.getAMKA()}"/>">Update</a></td>
                    <td><a href="AdminServlet?action=delete&doctorAMKA=<c:out value="${d.getAMKA()}"/>">Delete</a></td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    <p><a href="AdminServlet?action=insert" >Add Doctor</a></p>
</body>
</html>