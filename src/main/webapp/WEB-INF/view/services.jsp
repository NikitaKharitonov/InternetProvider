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
            <%@ include file="../css/style.css"%>
        </style>
    </head>
    <body>
        <%
            long clientId = (long)session.getAttribute("client_id");
            DBModel dbModel = new DBModel();
            List<ClientService<Internet>> internetList = new ArrayList<>();
            List<ClientService<Phone>> phoneList = new ArrayList<>();
            List<ClientService<Television>> televisionList = new ArrayList<>();
            try {
                internetList = dbModel.getClientInternets(clientId);
                phoneList = dbModel.getClientPhones(clientId);
                televisionList = dbModel.getClientTelevisions(clientId);
            } catch (ClientNotFoundException | InvalidModelException e) {
                e.printStackTrace();
            }
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
                    </tr>
                </thead>
                <tbody>
                    <% for (ClientService<Internet> clientService: internetList) { %>
                    <tr>
                        <td><%=clientService.getId()%></td>
                        <td><%=clientService.getActivationDate()%></td>
                        <td><button class="btn">Get history</button></td>
                    </tr>
                    <% } %>
                </tbody>
            </table>
            <div class="add">
                <button class="btn">Add</button>
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
                </tr>
                </thead>
                <tbody>
                <% for (ClientService<Phone> clientService: phoneList) { %>
                <tr>
                    <td><%=clientService.getId()%></td>
                    <td><%=clientService.getActivationDate()%></td>
                    <td><button class="btn">Get history</button></td>
                </tr>
                <% } %>
                </tbody>
            </table>
            <div class="add">
                <button class="btn">Add</button>
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
                </tr>
                </thead>
                <tbody>
                <% for (ClientService<Television> clientService: televisionList) { %>
                <tr>
                    <td><%=clientService.getId()%></td>
                    <td><%=clientService.getActivationDate()%></td>
                    <td><button class="btn">Get history</button></td>
                </tr>
                <% } %>
                </tbody>
            </table>
            <div class="add">
                <button class="btn">Add</button>
            </div>
        </div>
    </body>
</html>
