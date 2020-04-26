<%--
  Created by IntelliJ IDEA.
  User: Nikita
  Date: 26.04.2020
  Time: 22:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Error</title>
    <style>
        <%@ include file="../resources/css/style.css"%>
    </style>
</head>
<body>
    <div class="form-container">
        <%=request.getAttribute("javax.servlet.error.exception")%>
    </div>
</body>
</html>
