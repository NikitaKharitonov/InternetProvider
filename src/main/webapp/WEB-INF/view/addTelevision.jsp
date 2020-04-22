<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add television to client</title>
    <style>
        <%@ include file="../css/style.css"%>
    </style>
</head>
<body>
    <form method="post" class="form-container">
        <h1>Add television to client #<%=request.getSession().getAttribute("client_id")%></h1>
        <input name="channelcount" type="number" placeholder="Number of channels"/>
        <input type="submit" value="Add" class="btn">
    </form>
</body>
</html>
