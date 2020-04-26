<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Update client</title>
    <style>
        <%@ include file="../resources/css/style.css"%>
    </style>
</head>
<body>
    <form method="post" action="${pageContext.request.contextPath}/updateClient" class="form-container">
        <h1>Update client #<%=request.getSession().getAttribute("clientId")%></h1>
        <!--todo add current values-->
        <input name="name" type="text" placeholder="Name"/>
        <input name="phone" type="number" placeholder="Phone number"/>
        <input name="email" type="text" placeholder="Email address"/>
        <input type="submit" value="Update" class="btn"/>
    </form>
</body>
</html>
