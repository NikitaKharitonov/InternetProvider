<%@ page import="ru.internetprovider.model.Client" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Update client</title>
    <style>
        <%@ include file="../resources/css/style.css"%>
    </style>
</head>
<body>
    <%
        Client client = (Client) request.getAttribute("client");
    %>
    <div class="form-container">
        <form method="post" action="${pageContext.request.contextPath}/updateClient">
            <h1>Update client #<%=request.getSession().getAttribute("clientId")%></h1>
            <input name="name" type="text" min="1" value="<%=client.getName()%>" required/>
            <input name="phone" type="number" min="1" value="<%=client.getPhoneNumber()%>" required/>
            <input name="email" type="text" min="1" value="<%=client.getEmailAddress()%>" required/>
            <input type="submit" value="Update" class="btn"/>

        </form>
        <form action="${pageContext.request.contextPath}/">
            <button class="btn">Back</button>
        </form>
    </div>
</body>
</html>
