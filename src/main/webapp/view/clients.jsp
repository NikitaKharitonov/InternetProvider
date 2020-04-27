<%@ page import="java.io.IOException" %>
<%@ page import="ru.internetprovider.model.Client" %>
<%@ page import="java.util.List" %>
<%@ page import="ru.internetprovider.model.DBModel" %>
<%@ page import="ru.internetprovider.model.exceptions.InvalidModelException" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="ru.internetprovider.servlets.Clients" %>
<%@ page import="ru.internetprovider.servlets.AddClient" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Index</title>
        <style>
            <%@ include file="../resources/css/style.css"%>
        </style>
    </head>
    <body>
        <%
            List<Client> clientList = (List<Client>) request.getAttribute("clientList");
        %>
        <div class="container-table">
            <div class="greeting">
                Client list
            </div>
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Phone</th>
                    <th>Email</th>
                    <th></th>
                    <th></th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                    <% for (Client client: clientList) { %>
                    <tr>
                        <td><%=client.getId()%></td>
                        <td><%=client.getName()%></td>
                        <td><%=client.getPhone()%></td>
                        <td><%=client.getEmail()%></td>
                        <td>
                            <form action="${pageContext.request.contextPath}/services">
                                <button class="btn" name="clientId" value="<%=client.getId()%>">Get services</button>
                            </form>
                        </td>
                        <td>
                            <form action="${pageContext.request.contextPath}/updateClient">
                                <button class="btn" name="clientId" value="<%=client.getId()%>">Update</button>
                            </form>
                        </td>
                        <td>
                            <form method="post" action="${pageContext.request.contextPath}/deleteClient">
                                <button class="btn" name="clientId" value="<%=client.getId()%>" style="color: red">Delete</button>
                            </form>
                        </td>
                    </tr>
                    <% } %>
                </tbody>
            </table>
            <div class="add">
                <form action="${pageContext.request.contextPath}/addClient">
                    <button class="btn" name="button" value="add">Add...</button>
                </form>
            </div>
        </div>
    </body>
</html>
