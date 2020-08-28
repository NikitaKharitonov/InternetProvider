<%@ page import="java.util.List" %>
<%@ page import="ru.internetprovider.model.services.*" %>
<%@ page import="java.text.SimpleDateFormat" %>
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
        List<TemporalPhone> history = (List<TemporalPhone>) request.getAttribute("history");
        int phoneId = (int) request.getAttribute("phoneId");
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
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
                <th>Number of minutes</th>
                <th>Number of SMS</th>
            </tr>
            </thead>
            <tbody>
            <% for (TemporalPhone temporalPhone : history) { %>
            <tr>
                <td><%=temporalPhone.getBeginDate() != null ? formatter.format(temporalPhone.getBeginDate()) : ""%></td>
                <td><%=temporalPhone.getEndDate() != null ? formatter.format(temporalPhone.getEndDate()) : ""%></td>
                <td><%=temporalPhone.getMinsCount()%></td>
                <td><%=temporalPhone.getSmsCount()%></td>
            </tr>
            <% } %>
            </tbody>
        </table>
        <div>
            <form action="${pageContext.request.contextPath}/showPhone">
                <button class="btn">Back</button>
            </form>
        </div>
    </div>


</body>
</html>
