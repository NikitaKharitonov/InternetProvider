<%@ page import="ru.internetprovider.model.services.TelevisionState" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Update Television</title>
    <style>
        <%@ include file="../resources/css/style.css"%>
    </style>
</head>
<body>
    <%
        TelevisionState televisionState = (TelevisionState) request.getAttribute("television");
    %>
    <div class="form-container">
        <form method="post" action="${pageContext.request.contextPath}/updateTelevision">
            <h1>Update Television #<%=request.getSession().getAttribute("televisionId")%></h1>
            <input name="channelsCount" type="number" min="1" value="<%=televisionState.getChannelsCount()%>" required/>
            <input type="submit" value="Update" class="btn">
        </form>
        <form action="${pageContext.request.contextPath}/showTelevision">
            <button class="btn">Back</button>
        </form>
    </div>
</body>
</html>
