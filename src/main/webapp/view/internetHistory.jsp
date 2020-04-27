<%@ page import="ru.internetprovider.model.services.Internet" %>
<%@ page import="ru.internetprovider.model.services.ClientService" %>
<%@ page import="ru.internetprovider.model.services.Condition" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Internet History</title>
    <style>
        <%@ include file="../resources/css/style.css"%>
    </style>
</head>
<body>
    <%
        List<Condition<Internet>> internetHistoryList = (List<Condition<Internet>>) request.getAttribute("internetHistoryList");
        long internetId = (long) request.getAttribute("internetId");
    %>
    <div class="container-table">
        <div class="greeting">
            <label>History of Internet #<%=internetId%> of Client #<%=request.getSession().getAttribute("clientId")%></label>
        </div>
        <table>
            <thead>
            <tr>
                <th>Begin date</th>
                <th>End date</th>
                <th>Status</th>
                <th>Speed, Mbps</th>
                <th>Antivirus</th>
                <th>Connection Type</th>
            </tr>
            </thead>
            <tbody>
            <% for (Condition<Internet> condition: internetHistoryList) { %>
            <tr>
                <td><%=condition.getDateBegin()%></td>
                <td><%=condition.getDateEnd()%></td>
                <td><%=condition.getStatus()%></td>
                <td><%=condition.getService().getSpeed()%></td>
                <td><%=condition.getService().isAntivirus()%></td>
                <td><%=condition.getService().getConnectionType()%></td>
            </tr>
            <% } %>
            </tbody>
        </table>

    </div>


</body>
</html>
