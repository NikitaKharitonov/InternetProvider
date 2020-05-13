<%@ page import="ru.internetprovider.model.services.Internet" %>
<%@ page import="ru.internetprovider.model.services.ClientService" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
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
        List<Internet> internetList = (List<Internet>) request.getAttribute("internetList");
        long internetId = (long) request.getAttribute("internetId");
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
            <% for (Internet internet: internetList) { %>
            <tr>
                <td><%=internet.getBeginDate() != null ? formatter.format(internet.getBeginDate()) : ""%></td>
                <td><%=internet.getEndDate() != null ? formatter.format(internet.getEndDate()) : ""%></td>
                <td><%=internet.getSpeed()%></td>
                <td><%=internet.isAntivirus()%></td>
                <td><%=internet.getConnectionType()%></td>
            </tr>
            <% } %>
            </tbody>
        </table>
        <div>
            <form action="${pageContext.request.contextPath}/services">
                <button class="btn">Back</button>
            </form>
        </div>
    </div>


</body>
</html>
