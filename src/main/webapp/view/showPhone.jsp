<%@ page import="ru.internetprovider.model.services.*" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Phone</title>
        <style>
            <%@ include file="../resources/css/style.css"%>
        </style>
    </head>
    <body>
        <%
            long clientId = Long.parseLong(request.getSession().getAttribute("clientId").toString());
            List<Phone> phoneList = (List<Phone>) request.getAttribute("phoneList");
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        %>
        <div class="container-table">
            <div class="greeting">
                <label>Phone of client #<%=clientId%></label>
            </div>

            <table>
<%--                <caption>--%>
<%--                    Phone--%>
<%--                </caption>--%>
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
                <% if (phoneList != null) for (Service service : phoneList) { %>
                    <% if (!service.getStatus().equals(Status.DISCONNECTED)) { %>
                        <tr>
                            <td><%=service.getId()%></td>
                            <td><%=formatter.format(service.getActivationDate())%></td>
                            <td><%=service.getStatus()%></td>
                            <% List<TemporalPhone> history = ((Phone) service).getHistory(); %>
                            <% TemporalPhone current = history.get(history.size() - 1); %>
                            <td><%=current.getMinsCount()%></td>
                            <td><%=current.getSmsCount()%></td>
                            <td>
                                <form action="${pageContext.request.contextPath}/historyPhone">
                                    <button name="phoneId" value="<%=service.getId()%>" class="btn">Get history</button>
                                </form>
                            </td>
                            <td>
                                <form action="${pageContext.request.contextPath}/updatePhone">
                                    <button name="phoneId" value="<%=service.getId()%>" class="btn">Update</button>
                                </form>
                            </td>
                            <td>
                                <form method="post">
                                    <button name="phoneId" value="<%=service.getId()%>" class="btn"><%=service.getStatus().equals(Status.ACTIVE) ? "Suspend" : "Activate"%></button>
                                </form>
                            </td>
                            <td>
                                <form method="post" action="${pageContext.request.contextPath}/disconnectPhone">
                                    <button name="phoneId" value="<%=service.getId()%>" class="btn" style="color: red">Disconnect</button>
                                </form>
                            </td>
                        </tr>
                    <% } %>
                <% } %>
                </tbody>
            </table>
            <div class="container">
                <form action="${pageContext.request.contextPath}/addPhone">
                <button class="btn" name="button" value="phone">Add...</button>
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
