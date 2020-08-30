<%@ page import="ru.internetprovider.model.services.InternetSpecification" %>
<%@ page import="java.text.SimpleDateFormat" %>
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
        List<InternetSpecification> history = (List<InternetSpecification>) request.getAttribute("history");
        int internetId = (int) request.getAttribute("internetId");
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
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
                <th>Speed, Mbps</th>
                <th>Antivirus</th>
                <th>Connection Type</th>
            </tr>
            </thead>
            <tbody>
            <% for (InternetSpecification internetSpecification : history) { %>
            <tr>
                <td><%=internetSpecification.getBeginDate() != null ? formatter.format(internetSpecification.getBeginDate()) : ""%></td>
                <td><%=internetSpecification.getEndDate() != null ? formatter.format(internetSpecification.getEndDate()) : ""%></td>
                <td><%=internetSpecification.getSpeed()%></td>
                <td><%=internetSpecification.isAntivirus()%></td>
                <td><%=internetSpecification.getConnectionType()%></td>
            </tr>
            <% } %>
            </tbody>
        </table>
        <div>
            <form action="${pageContext.request.contextPath}/showInternet">
                <button class="btn">Back</button>
            </form>
        </div>
    </div>


</body>
</html>
