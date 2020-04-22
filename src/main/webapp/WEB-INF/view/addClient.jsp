<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add client</title>
    <style>
        <%@ include file="../css/style.css"%>
    </style>
</head>
<body>
    <form method="post" class="form-container">
        <h1>Add client</h1>
        <input name="name" type="text" placeholder="Name"/>
        <input name="phone" type="number" placeholder="Phone number"/>
        <input name="email" type="text" placeholder="Email address"/>
        <input type="submit" value="Add" class="btn"/>
    </form>
</body>
</html>
