<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>InternetProvider</title>
    </head>
    <body>
        <h2>All clients</h2>

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

    </body>
</html>
