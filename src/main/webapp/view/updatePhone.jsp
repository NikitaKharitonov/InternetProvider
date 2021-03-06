<%@ page import="ru.internetprovider.model.services.PhoneState" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Update Phone</title>
    <style>
        <%@ include file="../resources/css/style.css"%>
    </style>
</head>
<body>
    <%
        PhoneState phoneState = (PhoneState) request.getAttribute("phone");
    %>
    <div class="form-container">
        <form method="post" action="${pageContext.request.contextPath}/updatePhone">
            <h1>Update Phone #<%=request.getSession().getAttribute("phoneId")%></h1>
            <input name="minsCount" type="number" min="1" value="<%=phoneState.getMinsCount()%>" required/>
            <input name="smsCount" type="number" min="1" value="<%=phoneState.getSmsCount()%>" required/>
            <input type="submit" value="Update" class="btn">
        </form>
        <form action="${pageContext.request.contextPath}/showPhone">
            <button class="btn">Back</button>
        </form>
    </div>
</body>
</html>
