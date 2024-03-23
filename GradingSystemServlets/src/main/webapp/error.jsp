<%--
  Created by IntelliJ IDEA.
  User: ammar
  Date: ٢٠‏/٢‏/٢٠٢٤
  Time: ٦:٤٢ م
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Error</title>
</head>
<body>
<%
    String error = request.getParameter("error");
%>
    <h1><%=error%></h1>
</body>
</html>
