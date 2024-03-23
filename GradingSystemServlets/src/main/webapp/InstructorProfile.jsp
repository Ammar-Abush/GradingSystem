<%@ page import="model.Instructor" %>
<%@ page import="dao.InstructorInfoDao" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.util.Objects" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Course" %><%--
  Created by IntelliJ IDEA.
  User: ammar
  Date: ١٩‏/٢‏/٢٠٢٤
  Time: ٨:٣٢ م
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Instructor</title>

</head>
<body>

<%
    String username = (String) session.getAttribute("isn");
    Instructor instructorInfo;
    instructorInfo = (Instructor) session.getAttribute("instructorInfo");
    List<Course> courses = (List<Course>) session.getAttribute("courses");
    if (instructorInfo != null && username.equals(request.getParameter("isn"))) {
%>
<h1>Welcome to the profile of <%= instructorInfo.getFirstName() %>!</h1>
<p>Welcome <%=instructorInfo.getInstructorRank() + " " +   instructorInfo.getFirstName() + " " + instructorInfo.getLastName()%></p>
<p>Enrolled Courses:</p>
<ul>
    <%
        for (Course course : courses) {
    %>
    <li>
        Course: <a href = "AssignMarks?courseId=<%=course.getCourseId()%>&isn=<%=username%>"><%=course.getTitle()%></a>,
        SubjectId: <%= course.getSubjectId() %>,
        Number of Students in course: <%= course.getNumOfStudents()%>
        Number of credits: <%= course.getNumOfCredits()%>
    </li>
    <%
        }
    %>
</ul>
<%
} else {
%>
<p>Instructor not found</p>
<%
    }
%>
<a href = "InstructorLogout">Logout</a>
</body>
</html>

