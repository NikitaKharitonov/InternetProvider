<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Update phone</title>
    <style>
        <%@ include file="../resources/css/style.css"%>
    </style>
</head>
<body>
    <form method="post" action="${pageContext.request.contextPath}/updatePhone" class="form-container">
        <h1>Update phone <%=request.getSession().getAttribute("phoneId")%></h1>
        <!--todo add current values-->
        <input name="minsCount" type="number" placeholder="Number of calling minutes"/>
        <input name="smsCount" type="number" placeholder="Number of SMS"/>
        <input type="submit" value="Update" class="btn">
    </form>
</body>
</html>
