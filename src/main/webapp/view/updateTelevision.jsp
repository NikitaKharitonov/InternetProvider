<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Update television</title>
    <style>
        <%@ include file="../resources/css/style.css"%>
    </style>
</head>
<body>
    <form method="post" action="${pageContext.request.contextPath}/updateTelevision" class="form-container">
        <h1>Add television <%=request.getSession().getAttribute("televisionId")%></h1>
        <!--todo add current values-->
        <input name="channelsCount" type="number" placeholder="Number of channels"/>
        <input type="submit" value="Update" class="btn">
    </form>
</body>
</html>
