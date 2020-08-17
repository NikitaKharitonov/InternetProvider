<%@ page import="ru.internetprovider.model.services.Television" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.List" %>
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
        List<Television> televisionList = (List<Television>) request.getAttribute("televisionList");
        int televisionId = (int) request.getAttribute("televisionId");
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
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
                <th>Number of channels</th>
            </tr>
            </thead>
            <tbody>
            <% for (Television television: televisionList) { %>
            <tr>
                <td><%=television.getBeginDate() != null ? formatter.format(television.getBeginDate()) : ""%></td>
                <td><%=television.getEndDate() != null ? formatter.format(television.getEndDate()) : ""%></td>
                <td><%=television.getChannelsCount()%></td>
            </tr>
            <% } %>
            </tbody>
        </table>
        <div>
            <form action="${pageContext.request.contextPath}/showTelevision">
                <button class="btn">Back</button>
            </form>
        </div>
    </div>


</body>
</html>
