<%@ page import="ru.internetprovider.model.services.Television" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Update television</title>
    <style>
        <%@ include file="../resources/css/style.css"%>
    </style>
</head>
<body>
    <%
        Television television = (Television) request.getAttribute("television");
    %>
    <div class="form-container">
        <form method="post" action="${pageContext.request.contextPath}/updateTelevision">
            <h1>Update television #<%=request.getSession().getAttribute("televisionId")%></h1>
            <input name="channelsCount" type="number" min="1" value="<%=television.getChannelsCount()%>" required/>
            <input type="submit" value="Update" class="btn">
        </form>
        <form action="${pageContext.request.contextPath}/services">
            <button class="btn">Back</button>
        </form>
    </div>
</body>
</html>
