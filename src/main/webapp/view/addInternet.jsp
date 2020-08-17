<%@ page import="ru.internetprovider.model.services.Internet" %>
<%@ page import="java.util.List" %>
<%@ page import="ru.internetprovider.model.services.ConnectionType" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add internet</title>
    <style>
        <%@ include file="../resources/css/style.css"%>
    </style>
</head>
<body>
    <div class="form-container">
        <form method="post" action="${pageContext.request.contextPath}/addInternet">
            <h1>Add internet to client #<%=request.getSession().getAttribute("clientId")%></h1>
            <input name="speed" type="number" min="1" placeholder="Speed" required/>
            <label>Antivirus:
                <input name="antivirus" type="checkbox">
            </label>
            <label>Connection Type:
                <select name="connectionType">
                    <%List<ConnectionType> connectionTypeList = (List<ConnectionType>) request.getSession().getAttribute("connectionTypeList");%>
                    <%for(ConnectionType connectionType: connectionTypeList) {%>
                    <option selected><%=connectionType.toString()%></option>
                    <%}%>
                </select>
            </label>
            <input type="submit" value="Add" class="btn">
        </form>
        <form action="${pageContext.request.contextPath}/showInternet">
            <button class="btn">Back</button>
        </form>
    </div>
</body>
</html>
