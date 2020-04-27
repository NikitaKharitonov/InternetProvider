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
