<%@ page import="ru.internetprovider.model.services.*" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Services</title>
        <style>
            <%@ include file="../resources/css/style.css"%>
        </style>
    </head>
    <body>
        <%
            long clientId = Long.parseLong(request.getSession().getAttribute("clientId").toString());
            List<ClientService> internetList = (List<ClientService>) request.getAttribute("internetList");
            List<ClientService> phoneList = (List<ClientService>) request.getAttribute("phoneList");
            List<ClientService> televisionList = (List<ClientService>) request.getAttribute("televisionList");
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        %>
        <div class="container-table">
            <div class="greeting">
                <label>Services of client #<%=clientId%></label>
            </div>
            <table>
                <caption>
                    Internet
                </caption>
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
                <% if (internetList != null) for (ClientService clientService: internetList) { %>
                    <% if (!clientService.getStatus().equals(Status.DISCONNECTED)) { %>
                        <tr>
                            <td><%=clientService.getId()%></td>
                            <td><%=formatter.format(clientService.getActivationDate())%></td>
                            <td><%=clientService.getStatus()%></td>
                            <% List<Internet> history = ((ClientInternet)clientService).getHistory(); %>
<%--                            <% history.sort((i1, i2) -> i1.getBeginDate().compareTo(i2.getBeginDate())); %>--%>
                            <% Internet current = history.get(history.size() - 1); %>
                            <td><%=current.getSpeed()%></td>
                            <td><%=current.isAntivirus()%></td>
                            <td><%=current.getConnectionType()%></td>
                            <td>
                                <form action="${pageContext.request.contextPath}/internetHistory">
                                    <button class="btn" name="internetId" value="<%=clientService.getId()%>">Get history</button>
                                </form>
                            </td>
                            <td>
                                <form action="${pageContext.request.contextPath}/updateInternet">
                                    <button name="internetId" value="<%=clientService.getId()%>" class="btn">Update</button>
                                </form>
                            </td>
                            <td>
                                <form method="post">
                                    <button name="internetId" value="<%=clientService.getId()%>" class="btn"><%=clientService.getStatus().equals(Status.ACTIVE) ? "Suspend" : "Activate"%></button>
                                </form>
                            </td>
                            <td>
                                <form method="post" action="${pageContext.request.contextPath}/disconnectInternet">
                                    <button name="internetId" value="<%=clientService.getId()%>" class="btn" style="color: red">Disconnect</button>
                                </form>
                            </td>
                        </tr>
                    <% } %>
                <% } %>
                </tbody>
            </table>
            <div class="add">
                <form action="${pageContext.request.contextPath}/addInternet">
                <button class="btn" name="button" value="internet">Add...</button>
                </form>
            </div>

            <table>
                <caption>
                    Phone
                </caption>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Activation Date</th>
                    <th>Status</th>
                    <th>Number of minutes</th>
                    <th>Number of SMS</th>
                    <th></th>
                    <th></th>
                    <th></th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <% if (phoneList != null) for (ClientService clientService: phoneList) { %>
                    <% if (!clientService.getStatus().equals(Status.DISCONNECTED)) { %>
                        <tr>
                            <td><%=clientService.getId()%></td>
                            <td><%=formatter.format(clientService.getActivationDate())%></td>
                            <td><%=clientService.getStatus()%></td>
                            <% List<Phone> history = ((ClientPhone)clientService).getHistory(); %>
                            <% Phone current = history.get(history.size() - 1); %>
                            <td><%=current.getMinsCount()%></td>
                            <td><%=current.getSmsCount()%></td>
                            <td>
                                <form action="${pageContext.request.contextPath}/phoneHistory">
                                    <button name="phoneId" value="<%=clientService.getId()%>" class="btn">Get history</button>
                                </form>
                            </td>
                            <td>
                                <form action="${pageContext.request.contextPath}/updatePhone">
                                    <button name="phoneId" value="<%=clientService.getId()%>" class="btn">Update</button>
                                </form>
                            </td>
                            <td>
                                <form method="post">
                                    <button name="phoneId" value="<%=clientService.getId()%>" class="btn"><%=clientService.getStatus().equals(Status.ACTIVE) ? "Suspend" : "Activate"%></button>
                                </form>
                            </td>
                            <td>
                                <form method="post" action="${pageContext.request.contextPath}/disconnectPhone">
                                    <button name="phoneId" value="<%=clientService.getId()%>" class="btn" style="color: red">Disconnect</button>
                                </form>
                            </td>
                        </tr>
                    <% } %>
                <% } %>
                </tbody>
            </table>
            <div class="add">
                <form action="${pageContext.request.contextPath}/addPhone">
                <button class="btn" name="button" value="phone">Add...</button>
                </form>
            </div>

            <table>
                <caption>
                    Television
                </caption>
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
                <%if (televisionList != null) for (ClientService clientService: televisionList) { %>
                    <% if (!clientService.getStatus().equals(Status.DISCONNECTED)) { %>
                        <tr>
                            <td><%=clientService.getId()%></td>
                            <td><%=formatter.format(clientService.getActivationDate())%></td>
                            <td><%=clientService.getStatus()%></td>
                            <% List<Television> history = ((ClientTelevision)clientService).getHistory(); %>
                            <% Television current = history.get(history.size() - 1); %>
                            <td><%=current.getChannelsCount()%></td>
                            <td>
                                <form action="${pageContext.request.contextPath}/televisionHistory">
                                    <button name="televisionId" value="<%=clientService.getId()%>" class="btn">Get history</button>
                                </form>
                            </td>
                            <td>
                                <form action="${pageContext.request.contextPath}/updateTelevision">
                                    <button name="televisionId" value="<%=clientService.getId()%>" class="btn">Update</button>
                                </form>
                            </td>
                            <td>
                                <form method="post">
                                    <button name="televisionId" value="<%=clientService.getId()%>" class="btn"><%=clientService.getStatus().equals(Status.ACTIVE) ? "Suspend" : "Activate"%></button>
                                </form>
                            </td>
                            <td>
                                <form method="get" action="${pageContext.request.contextPath}/disconnect">
                                    <button name="televisionId" value="<%=clientService.getId()%>" class="btn" style="color: red">Disconnect</button>
                                </form>
                            </td>
                        </tr>
                    <% } %>
                <% } %>
                </tbody>
            </table>
            <div class="add">
                <form action="${pageContext.request.contextPath}/addTelevision">
                        <button class="btn" name="button" value="television">Add...</button>
                </form>
            </div>
            <br/>
        </div>
        <div>
            <form action="${pageContext.request.contextPath}/">
                <button class="btn">Back</button>
            </form>
        </div>
    </body>
</html>
