<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add phone to client</title>
    <style>
        <%@ include file="../resources/css/style.css"%>
    </style>
</head>
<body>
    <form method="post" action="${pageContext.request.contextPath}/addPhone" class="form-container">
        <h1>Add phone to client #<%=request.getSession().getAttribute("clientId")%></h1>
        <input name="minsCount" type="number" min="1" placeholder="Number of calling minutes" required/>
        <input name="smsCount" type="number" min="1" placeholder="Number of SMS" required/>
        <input type="submit" value="Add" class="btn">
    </form>
</body>
</html>
