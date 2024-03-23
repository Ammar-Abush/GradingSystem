<%@ page import="model.StudentInfo" %>
<%@ page import="dao.StudentInfoDao" %>
<%@ page import="model.Enrollment" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>User Profile</title>

</head>
<body>

<%
    String username = request.getParameter("ssn");
    StudentInfo userInfo = (StudentInfo) session.getAttribute("userInfo");
    List<Enrollment> enrollments = (List<Enrollment>) session.getAttribute("enrollments");
    if (userInfo != null && session.getAttribute("ssn").equals(username)) {
%>
<h1>Welcome to the profile of <%= userInfo.getFirstName() %>!</h1>
<p>Welcome <%= userInfo.getFirstName() + " " + userInfo.getLastName()%></p>

<p>Enrolled Courses:</p>
<ul>
    <%
        for (Enrollment enrollment : enrollments) {
    %>
    <li>
        Course: <%= enrollment.getTitle() %>,
        Grade: <%= enrollment.getGrade() %>,
        Mark: <%= enrollment.getMark() %>,
        Class average: <%= enrollment.getAverage()%>,
        Highest mark: <%=enrollment.getHighest()%>,
        Lowest mark: <%=enrollment.getLower()%>
    </li>
    <%
        }
    %>
</ul>
<%
} else {
%>
<p>User not found</p>
<%
    }
%>
<a href = "StudentLogout">Logout</a>
</body>
</html>
