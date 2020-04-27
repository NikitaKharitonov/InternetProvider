<%@ page import="ru.internetprovider.model.DBModel" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="ru.internetprovider.model.services.ClientService" %>
<%@ page import="ru.internetprovider.model.services.Internet" %>
<%@ page import="java.util.List" %>
<%@ page import="ru.internetprovider.model.exceptions.InvalidModelException" %>
<%@ page import="ru.internetprovider.model.exceptions.ClientNotFoundException" %>
<%@ page import="ru.internetprovider.model.services.Phone" %>
<%@ page import="ru.internetprovider.model.services.Television" %>
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
            long clientId = (long) request.getSession().getAttribute("clientId");
            List<ClientService<Internet>> internetList = (List<ClientService<Internet>>) request.getAttribute("internetList");
            List<ClientService<Phone>> phoneList = (List<ClientService<Phone>>) request.getAttribute("phoneList");
            List<ClientService<Television>> televisionList = (List<ClientService<Television>>) request.getAttribute("televisionList");

        %>
        <div class="container-table">
            <div class="greeting">
                <label>Client <%=clientId%></label>
            </div>
            <table>
                <caption>
                    Internet
                </caption>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Activation Date</th>
                    <th></th>
                    <th></th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <% for (ClientService<Internet> clientService: internetList) { %>
                <tr>
                    <td><%=clientService.getId()%></td>
                    <td><%=clientService.getActivationDate()%></td>
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
                        <form>
                            <button name="internetId" value="<%=clientService.getId()%>" class="btn">Disconnect</button>
                        </form>
                    </td>
                </tr>
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
                    <th></th>
                    <th></th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <% for (ClientService<Phone> clientService: phoneList) { %>
                <tr>
                    <td><%=clientService.getId()%></td>
                    <td><%=clientService.getActivationDate()%></td>
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
                        <form>
                            <button name="phoneId" value="<%=clientService.getId()%>" class="btn">Disconnect</button>
                        </form>
                    </td>
                </tr>
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
                    <th></th>
                    <th></th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <% for (ClientService<Television> clientService: televisionList) { %>
                <tr>
                    <td><%=clientService.getId()%></td>
                    <td><%=clientService.getActivationDate()%></td>
                    <td>
                        <form action="${pageContext.request.contextPath}/televisionHistory">
                            <button class="btn">Get history</button>
                        </form>
                    </td>
                    <td>
                        <form action="${pageContext.request.contextPath}/updateTelevision">
                            <button name="televisionId" value="<%=clientService.getId()%>" class="btn">Update</button>
                        </form>
                    </td>
                    <td>
                        <form>
                            <button name="televisionId" value="<%=clientService.getId()%>" class="btn">Disconnect</button>
                        </form>
                    </td>
                </tr>
                <% } %>
                </tbody>
            </table>
            <div class="add">
                <form action="${pageContext.request.contextPath}/addTelevision">
                        <button class="btn" name="button" value="television">Add...</button>
                </form>
            </div>

        </div>
    </body>
</html>
