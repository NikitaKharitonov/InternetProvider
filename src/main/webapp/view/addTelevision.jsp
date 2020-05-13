<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add television to client</title>
    <style>
        <%@ include file="../resources/css/style.css"%>
    </style>
</head>
<body>
    <div class="form-container">
        <form method="post" action="${pageContext.request.contextPath}/addTelevision">
            <h1>Add television to client #<%=request.getSession().getAttribute("clientId")%></h1>
            <input name="channelsCount" type="number" min="1" placeholder="Number of channels" required/>
            <input type="submit" value="Add" class="btn">
        </form>
        <form action="${pageContext.request.contextPath}/services">
            <button class="btn">Back</button>
        </form>
    </div>
</body>
</html>
