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
                <% if (internetList != null) for (Internet internet : internetList) { %>
                    <% if (!internet.getStatus().equals(Status.DELETED)) { %>
                        <tr>
                            <td><%=internet.getId()%></td>
                            <td><%=formatter.format(internet.getActivationDate())%></td>
                            <td><%=internet.getStatus()%></td>
                            <% InternetSpecification current =
                                    internet.getHistory().get(internet.getHistory().size() - 1); %>
                            <td><%=current.getSpeed()%></td>
                            <td><%=current.isAntivirus()%></td>
                            <td><%=current.getConnectionType()%></td>
                            <td>
                                <form action="${pageContext.request.contextPath}/historyInternet">
                                    <button class="btn" name="internetId" value="<%=internet.getId()%>">Get history</button>
                                </form>
                            </td>
                            <td>
                                <form action="${pageContext.request.contextPath}/updateInternet">
                                    <button name="internetId" value="<%=internet.getId()%>" class="btn">Update</button>
                                </form>
                            </td>
                            <td>
                                <form method="post">
                                    <button name="internetId" value="<%=internet.getId()%>" class="btn"><%=internet.getStatus().equals(Status.ACTIVE) ? "Suspend" : "Activate"%></button>
                                </form>
                            </td>
                            <td>
                                <form method="post">
                                    <button name="delete" value="<%=internet.getId()%>" class="btn" style="color: red">Delete</button>
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
