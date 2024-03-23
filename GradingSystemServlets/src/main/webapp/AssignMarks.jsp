<%@ page import="model.StudentInfo" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: ammar
  Date: ٢٠‏/٢‏/٢٠٢٤
  Time: ١:٣٩ ص
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Assign Marks</title>
    <style>
        body {
            font-family: 'Arial', sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 0;
            display: flex;
            align-items: center;
            justify-content: center;
            height: 100vh;
        }

        form {
            background-color: #fff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            text-align: center;
        }

        label {
            display: block;
            margin: 10px 0 5px;
            font-weight: bold;
        }

        select, input {
            width: 100%;
            padding: 10px;
            margin-bottom: 15px;
            box-sizing: border-box;
            border: 1px solid #ccc;
            border-radius: 4px;
            font-size: 16px;
        }

        select {
            appearance: none;
            background: url('https://cdn.iconscout.com/icon/free/png-256/arrow-drop-down-1767478-1502396.png') no-repeat right #fff;
            background-size: 20px;
            padding-right: 40px;
            cursor: pointer;
        }

        input[type="submit"] {
            background-color: #4caf50;
            color: white;
            cursor: pointer;
        }

        input[type="submit"]:hover {
            background-color: #45a049;
        }
    </style>
</head>
<body>
<%
    List<StudentInfo> students = (List<StudentInfo>) request.getAttribute("students");
%>
    <form action="AssignMarks" method="post">
        <label for="studentSsn">Select Student:</label>
        <select name="studentSsn" id="studentSsn">
            <% for (StudentInfo student : students){ %>
            <option value="<%= student.getSsn() %>"><%= student.getFirstName() %> <%= student.getLastName() %></option>
            <% } %>
        </select>
        <label for="mark">Mark:</label>
        <input type="number" name="mark" id="mark" required max="100" min="0">

        <label for="grade">Grade:</label>
        <select name="grade" id="grade">
            <option value="A">A</option>
            <option value="B">B</option>
            <option value="C">C</option>
            <option value="D">D</option>
            <option value="E">E</option>
            <option value="F">F</option>
        </select>
        <input type="hidden" name="courseId" value="<%=request.getParameter("courseId")%>">
        <input type="submit" value="Assign Mark">
    </form>
</body>
</html>
