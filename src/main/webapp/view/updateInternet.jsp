<%@ page import="ru.internetprovider.model.services.InternetState" %>
<%@ page import="java.util.List" %>
<%@ page import="ru.internetprovider.model.services.ConnectionType" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Update Internet</title>
    <style>
        <%@ include file="../resources/css/style.css"%>
    </style>
</head>
<body>
    <%
        InternetState internetState = (InternetState) request.getAttribute("internet");
    %>
    <div class="form-container">
        <form method="post" action="${pageContext.request.contextPath}/updateInternet">
            <h1>Update Internet #<%=request.getSession().getAttribute("internetId")%></h1>
            <input name="speed" type="number" min="1" value="<%=internetState.getSpeed()%>" required/>
            <label>Antivirus:
                <input name="antivirus" type="checkbox" <%=internetState.isAntivirus() ? "checked" : ""%>>
            </label>
            <label>Connection Type:
                <select name="connectionType">
                    <option selected><%=internetState.getConnectionType()%></option>
                    <%List<ConnectionType> connectionTypeList = (List<ConnectionType>) request.getSession().getAttribute("connectionTypeList");%>
                    <%for(ConnectionType connectionType: connectionTypeList) {%>
                    <option><%=connectionType.toString()%></option>
                    <%}%>
                </select>
            </label>
            <input type="submit" value="Update" class="btn">
        </form>
        <form action="${pageContext.request.contextPath}/showInternet">
            <button class="btn">Back</button>
        </form>
    </div>

</body>
</html>
