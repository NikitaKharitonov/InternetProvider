<%@ page import="java.io.IOException" %>
<%@ page import="ru.internetprovider.model.Client" %>
<%@ page import="java.util.List" %>
<%@ page import="ru.internetprovider.model.DBModel" %>
<%@ page import="ru.internetprovider.model.exceptions.InvalidModelException" %>
<%@ page import="java.util.ArrayList" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Index</title>
        <style>
            <%@ include file="../css/style.css"%>
        </style>
    </head>
    <body>
        <%
            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            DBModel dbModel = new DBModel();
            List<Client> clientList = new ArrayList<>();
            try {
                clientList = dbModel.getClients();
            } catch (InvalidModelException e) {
                e.printStackTrace();
            }
        %>
        <form method="post">
            <div class="container-table">
                <table>
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Phone</th>
                        <th>Email</th>
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
                            <td><button class="btn" name="button" value="<%=client.getId()%>">Get info</button></td>
                        </tr>
                        <% } %>
                    </tbody>
                </table>
                <div class="add">
                    <button class="btn">Add</button>
                </div>
            </div>
        </form>
    </body>
</html>
