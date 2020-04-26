<%@ page import="ru.internetprovider.model.services.Internet" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Update internet</title>
    <style>
        <%@ include file="../resources/css/style.css"%>
    </style>
</head>
<body>
    <form method="post" action="${pageContext.request.contextPath}/updateInternet" class="form-container">
        <h1>Update internet #<%=request.getSession().getAttribute("internetId")%></h1>
        <!--todo add current values-->
        <input name="speed" type="number" placeholder="Speed"/>
        <label>Antivirus:
            <input name="antivirus" type="checkbox">
        </label>
        <label>Connection Type:
            <select name="connectionType">
                <%List<Internet.ConnectionType> connectionTypeList = (List<Internet.ConnectionType>) request.getSession().getAttribute("connectionTypeList");%>
                <%for(Internet.ConnectionType connectionType: connectionTypeList) {%>
                <option><%=connectionType.toString()%></option>
                <%}%>
            </select>
        </label>
        <input type="submit" value="Update" class="btn">
    </form>
</body>
</html>
