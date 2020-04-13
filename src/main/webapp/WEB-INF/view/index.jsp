
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
    <title>Result</title>
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
<div class="container-table">

        <table>
            <thead>
                <tr>
                    <td>
                        Name
                    </td>
                    <td>
                        Phone
                    </td>
                    <td>
                        Email
                    </td>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="client" items="${requestScope.clients}">
                    <tr>
                        <td>
                            <c:out value="${client.name}"/>
                        </td>
                        <td>
                            <c:out value="${client.phone}"/>
                        </td>
                        <td>
                            <c:out value="${client.email}"/>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

</div>

</body>
</html>
