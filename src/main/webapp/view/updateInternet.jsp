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
    <%
        Internet internet = (Internet) request.getAttribute("internet");
    %>
    <div class="form-container">
        <form method="post" action="${pageContext.request.contextPath}/updateInternet">
            <h1>Update internet #<%=request.getSession().getAttribute("internetId")%></h1>
            <input name="speed" type="number" min="1" value="<%=internet.getSpeed()%>" required/>
            <label>Antivirus:
                <input name="antivirus" type="checkbox" <%=internet.isAntivirus() ? "checked" : ""%>>
            </label>
            <label>Connection Type:
                <select name="connectionType">
                    <option selected><%=internet.getConnectionType()%></option>
                    <%List<Internet.ConnectionType> connectionTypeList = (List<Internet.ConnectionType>) request.getSession().getAttribute("connectionTypeList");%>
                    <%for(Internet.ConnectionType connectionType: connectionTypeList) {%>
                    <option><%=connectionType.toString()%></option>
                    <%}%>
                </select>
            </label>
            <input type="submit" value="Update" class="btn">
        </form>
        <form action="${pageContext.request.contextPath}/services">
            <button class="btn">Back</button>
        </form>
    </div>

</body>
</html>
