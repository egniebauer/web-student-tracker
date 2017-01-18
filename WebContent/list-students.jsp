<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Student Tracker App</title>
	<link type="text/css" rel="stylesheet" href="css/style.css">
</head>

<body>

	<div id="wrapper">
		<div id="header">
			<h2>FooBar University</h2>
		</div>
	</div>

	<div id="container">
		<div id="content">
			
			<!-- put new button: Add Student -->
			<input type="button" value="Add Student" onclick="window.location.href='add-student-form.jsp'; return false;" class="add-student-button"
			/>
			
			<table>
				<tr>
					<th>First Name</th>
					<th>Last Name</th>
					<th>Email</th>
					<th>Action</th>
				</tr>
				
			<c:forEach var="currentStudent" items="${STUDENT_LIST}" >
			
				<!-- update link for each student -->
				<c:url var="updateLink" value="StudentControllerServlet">
					<c:param name="command" value="LOAD" />
					<c:param name="studentID" value="${currentStudent.id}" />
				</c:url>
				
				<!-- delete link for each student -->
				<c:url var="deleteLink" value="StudentControllerServlet">
					<c:param name="command" value="DELETE" />
					<c:param name="studentID" value="${currentStudent.id}" />
				</c:url>
				
				<tr>
					<td> ${currentStudent.firstName} </td>
					<td> ${currentStudent.lastName} </td>
					<td> ${currentStudent.email} </td>
					<td> <a href="${updateLink}">Update</a>
					 <br /> 
					<a href="${deleteLink}"
						onclick="if (!(confirm('Are you sure you want to delete this student?'))) return false">Delete</a></td>
				</tr>
				
			</c:forEach>
				
			</table>
			
		</div>
	</div>
</body>
</html>