<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add phone to client</title>
    <style>
        <%@ include file="../css/style.css"%>
    </style>
</head>
<body>
    <form method="post" class="form-container">
        <h1>Add phone to client #<%=request.getSession().getAttribute("client_id")%></h1>
        <input name="callminscount" type="number" placeholder="Number of calling minutes"/>
        <input name="smscount" type="number" placeholder="Number of SMS"/>
        <input type="submit" value="Add" class="btn">
    </form>
</body>
</html>
