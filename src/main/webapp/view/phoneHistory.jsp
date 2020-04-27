<%@ page import="java.util.List" %>
<%@ page import="ru.internetprovider.model.services.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Phone History</title>
    <style>
        <%@ include file="../resources/css/style.css"%>
    </style>
</head>
<body>
    <%
        List<Condition<Phone>> phoneHistoryList = (List<Condition<Phone>>) request.getAttribute("phoneHistoryList");
        long phoneId = (long) request.getAttribute("phoneId");
    %>
    <div class="container-table">
        <div class="greeting">
            <label>History of Phone #<%=phoneId%> of Client #<%=request.getSession().getAttribute("clientId")%></label>
        </div>
        <table>
            <thead>
            <tr>
                <th>Begin date</th>
                <th>End date</th>
                <th>Status</th>
                <th>Number of minutes</th>
                <th>Number of SMS</th>
            </tr>
            </thead>
            <tbody>
            <% for (Condition<Phone> condition: phoneHistoryList) { %>
            <tr>
                <td><%=condition.getDateBegin()%></td>
                <td><%=condition.getDateEnd()%></td>
                <td><%=condition.getStatus()%></td>
                <td><%=condition.getService().getMinsCount()%></td>
                <td><%=condition.getService().getSmsCount()%></td>
            </tr>
            <% } %>
            </tbody>
        </table>

    </div>


</body>
</html>
