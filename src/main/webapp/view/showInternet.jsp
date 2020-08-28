<%@ page import="ru.internetprovider.model.services.*" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Internet</title>
        <style>
            <%@ include file="../resources/css/style.css"%>
        </style>
    </head>
    <body>
        <%
            long clientId = Long.parseLong(request.getSession().getAttribute("clientId").toString());
            List<Internet> internetList = (List<Internet>) request.getAttribute("internetList");
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        %>
        <div class="container-table">
            <div class="greeting">
                <label>Internet of client #<%=clientId%></label>
            </div>
            <table>
<%--                <caption>--%>
<%--                    Internet--%>
<%--                </caption>--%>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Activation Date</th>
                    <th>Status</th>
                    <th>Speed, Mbps</th>
                    <th>Antivirus</th>
                    <th>Connection Type</th>
                    <th></th>
                    <th></th>
                    <th></th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <% if (internetList != null) for (Service service : internetList) { %>
                    <% if (!service.getStatus().equals(Status.DISCONNECTED)) { %>
                        <tr>
                            <td><%=service.getId()%></td>
                            <td><%=formatter.format(service.getActivationDate())%></td>
                            <td><%=service.getStatus()%></td>
                            <% List<TemporalInternet> history = ((Internet) service).getHistory(); %>
<%--                            <% history.sort((i1, i2) -> i1.getBeginDate().compareTo(i2.getBeginDate())); %>--%>
                            <% TemporalInternet current = history.get(history.size() - 1); %>
                            <td><%=current.getSpeed()%></td>
                            <td><%=current.isAntivirus()%></td>
                            <td><%=current.getConnectionType()%></td>
                            <td>
                                <form action="${pageContext.request.contextPath}/historyInternet">
                                    <button class="btn" name="internetId" value="<%=service.getId()%>">Get history</button>
                                </form>
                            </td>
                            <td>
                                <form action="${pageContext.request.contextPath}/updateInternet">
                                    <button name="internetId" value="<%=service.getId()%>" class="btn">Update</button>
                                </form>
                            </td>
                            <td>
                                <form method="post">
                                    <button name="internetId" value="<%=service.getId()%>" class="btn"><%=service.getStatus().equals(Status.ACTIVE) ? "Suspend" : "Activate"%></button>
                                </form>
                            </td>
                            <td>
                                <form method="post" action="${pageContext.request.contextPath}/disconnectInternet">
                                    <button name="internetId" value="<%=service.getId()%>" class="btn" style="color: red">Disconnect</button>
                                </form>
                            </td>
                        </tr>
                    <% } %>
                <% } %>
                </tbody>
            </table>
            <div class="container">
                <form action="${pageContext.request.contextPath}/addInternet">
                    <button class="btn" name="button" value="internet">Add...</button>
                </form>
            </div>

            <br/>
            <div class="container">
                <form action="${pageContext.request.contextPath}/">
                    <button class="btn">Back</button>
                </form>
            </div>
        </div>

    </body>
</html>
