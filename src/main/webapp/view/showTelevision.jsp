<%@ page import="ru.internetprovider.model.services.*" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Television</title>
        <style>
            <%@ include file="../resources/css/style.css"%>
        </style>
    </head>
    <body>
        <%
            long clientId = Long.parseLong(request.getSession().getAttribute("clientId").toString());
            List<Television> televisionList = (List<Television>) request.getAttribute("televisionList");
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        %>
        <div class="container-table">
            <div class="greeting">
                <label>Television of client #<%=clientId%></label>
            </div>

            <table>
<%--                <caption>--%>
<%--                    Television--%>
<%--                </caption>--%>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Activation Date</th>
                    <th>Status</th>
                    <th>Number of channels</th>
                    <th></th>
                    <th></th>
                    <th></th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <%if (televisionList != null) for (Service service : televisionList) { %>
                    <% if (!service.getStatus().equals(Status.DISCONNECTED)) { %>
                        <tr>
                            <td><%=service.getId()%></td>
                            <td><%=formatter.format(service.getActivationDate())%></td>
                            <td><%=service.getStatus()%></td>
                            <% List<TemporalTelevision> history = ((Television) service).getHistory(); %>
                            <% TemporalTelevision current = history.get(history.size() - 1); %>
                            <td><%=current.getChannelsCount()%></td>
                            <td>
                                <form action="${pageContext.request.contextPath}/historyTelevision">
                                    <button name="televisionId" value="<%=service.getId()%>" class="btn">Get history</button>
                                </form>
                            </td>
                            <td>
                                <form action="${pageContext.request.contextPath}/updateTelevision">
                                    <button name="televisionId" value="<%=service.getId()%>" class="btn">Update</button>
                                </form>
                            </td>
                            <td>
                                <form method="post">
                                    <button name="televisionId" value="<%=service.getId()%>" class="btn"><%=service.getStatus().equals(Status.ACTIVE) ? "Suspend" : "Activate"%></button>
                                </form>
                            </td>
                            <td>
                                <form method="post" action="${pageContext.request.contextPath}/disconnectTelevision">
                                    <button name="televisionId" value="<%=service.getId()%>" class="btn" style="color: red">Disconnect</button>
                                </form>
                            </td>
                        </tr>
                    <% } %>
                <% } %>
                </tbody>
            </table>
            <div class="container">
                <form action="${pageContext.request.contextPath}/addTelevision">
                        <button class="btn" name="button" value="television">Add...</button>
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
