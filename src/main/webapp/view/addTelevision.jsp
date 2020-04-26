<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add television to client</title>
    <style>
        <%@ include file="../resources/css/style.css"%>
    </style>
</head>
<body>
    <form method="post" action="${pageContext.request.contextPath}/addTelevision" class="form-container">
        <h1>Add television to client #<%=request.getSession().getAttribute("clientId")%></h1>
        <input name="channelsCount" type="number" placeholder="Number of channels"/>
        <input type="submit" value="Add" class="btn">
    </form>
</body>
</html>