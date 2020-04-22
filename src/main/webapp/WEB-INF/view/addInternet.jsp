<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add internet</title>
    <style>
        <%@ include file="../css/style.css"%>
    </style>
</head>
<body>
    <form method="post" class="form-container">
        <h1>Add internet to client #<%=request.getSession().getAttribute("client_id")%></h1>
        <input name="speed" type="number" placeholder="Speed"/>
        <input name="antivirus" type="text" placeholder="Antivirus">
        <input name="connectionType" type="text" placeholder="Connection Type"/>
        <input type="submit" value="Add" class="btn">
    </form>
</body>
</html>
