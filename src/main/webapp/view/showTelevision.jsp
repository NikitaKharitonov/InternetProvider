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
                <%if (televisionList != null) for (Television television : televisionList) { %>
                    <% if (!television.getStatus().equals(Status.DELETED)) { %>
                        <tr>
                            <td><%=television.getId()%></td>
                            <td><%=formatter.format(television.getActivationDate())%></td>
                            <td><%=television.getStatus()%></td>
                            <% List<TelevisionState> history = television.getHistory(); %>
                            <% TelevisionState current = history.get(history.size() - 1); %>
                            <td><%=current.getChannelsCount()%></td>
                            <td>
                                <form action="${pageContext.request.contextPath}/historyTelevision">
                                    <button name="televisionId" value="<%=television.getId()%>" class="btn">Get history</button>
                                </form>
                            </td>
                            <td>
                                <form action="${pageContext.request.contextPath}/updateTelevision">
                                    <button name="televisionId" value="<%=television.getId()%>" class="btn">Update</button>
                                </form>
                            </td>
                            <td>
                                <form method="post">
                                    <button name="televisionId" value="<%=television.getId()%>" class="btn"><%=television.getStatus().equals(Status.ACTIVE) ? "Suspend" : "Activate"%></button>
                                </form>
                            </td>
                            <td>
                                <form method="post">
                                    <button name="delete" value="<%=television.getId()%>" class="btn" style="color: red">Delete</button>
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
