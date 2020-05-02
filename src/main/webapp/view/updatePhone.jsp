<%@ page import="ru.internetprovider.model.services.Phone" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Update phone</title>
    <style>
        <%@ include file="../resources/css/style.css"%>
    </style>
</head>
<body>
    <%
        Phone phone = (Phone) request.getAttribute("phone");
    %>
    <form method="post" action="${pageContext.request.contextPath}/updatePhone" class="form-container">
        <h1>Update phone #<%=request.getSession().getAttribute("phoneId")%></h1>
        <input name="minsCount" type="number" min="1" value="<%=phone.getMinsCount()%>" required/>
        <input name="smsCount" type="number" min="1" value="<%=phone.getSmsCount()%>" required/>
        <input type="submit" value="Update" class="btn">
    </form>
</body>
</html>
