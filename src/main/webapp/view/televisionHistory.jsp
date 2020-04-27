<%@ page import="ru.internetprovider.model.services.Internet" %>
<%@ page import="ru.internetprovider.model.services.ClientService" %>
<%@ page import="ru.internetprovider.model.services.Condition" %>
<%@ page import="java.util.List" %>
<%@ page import="ru.internetprovider.model.services.Television" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Television History</title>
    <style>
        <%@ include file="../resources/css/style.css"%>
    </style>
</head>
<body>
    <%
        List<Condition<Television>> televisionHistoryList = (List<Condition<Television>>) request.getAttribute("televisionHistoryList");
        long televisionId = (long) request.getAttribute("televisionId");
    %>
    <div class="container-table">
        <div class="greeting">
            <label>History of Television #<%=televisionId%> of Client #<%=request.getSession().getAttribute("clientId")%></label>
        </div>
        <table>
            <thead>
            <tr>
                <th>Begin date</th>
                <th>End date</th>
                <th>Status</th>
                <th>Number of channels</th>
            </tr>
            </thead>
            <tbody>
            <% for (Condition<Television> condition: televisionHistoryList) { %>
            <tr>
                <td><%=condition.getDateBegin()%></td>
                <td><%=condition.getDateEnd()%></td>
                <td><%=condition.getStatus()%></td>
                <td><%=condition.getService().getChannelsCount()%></td>
            </tr>
            <% } %>
            </tbody>
        </table>

    </div>


</body>
</html>
