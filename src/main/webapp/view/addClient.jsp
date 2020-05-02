<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add client</title>
    <style>
        <%@ include file="../resources/css/style.css"%>
    </style>
</head>
<body>
    <form method="post" action="${pageContext.request.contextPath}/addClient" class="form-container">
        <h1>Add client</h1>
        <input name="name" type="text" placeholder="Name" required/>
        <input name="phone" type="number" placeholder="Phone number" required/>
        <input name="email" type="text" placeholder="Email address" required/>
        <input type="submit" value="Add" class="btn"/>
    </form>
</body>
</html>
