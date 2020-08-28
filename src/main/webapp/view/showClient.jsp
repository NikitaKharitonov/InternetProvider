<%@ page import="ru.internetprovider.model.Client" %>
<%@ page import="java.util.List" %>
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
            <br/>
            <%
                String nameSearch = (String) request.getAttribute("name-search");
                String phoneSearch = (String) request.getAttribute("phone-search");
                String emailSearch = (String) request.getAttribute("email-search");
            %>
            <br/>
            <div class="container">
                <%if(!(nameSearch == null && phoneSearch == null && emailSearch == null)) {%>
                    Results for:
                    <%=nameSearch != null && nameSearch.equals("") ? "" : "Name: " + "\"" + nameSearch + "\" "%><br>
                    <%=phoneSearch != null && phoneSearch.equals("") ? "" : "Phone number: " + "\"" + phoneSearch + "\" "%><br>
                    <%=emailSearch != null && emailSearch.equals("") ? "" : "Email address: " + "\"" + emailSearch + "\" "%><br>
                <%}%>
            </div>
            <br/>
            <div class="container">
                <form action="${pageContext.request.contextPath}/searchByCriteria">
                    <input type="text" name="name-search" placeholder="Name"/>
                    <input type="text" name="phone-search" placeholder="Phone number"/>
                    <input type="text" name="email-search" placeholder="Email address"/>
                    <input type="submit" class="btn" value="Search"/>
                </form>
            </div>
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Phone number</th>
                    <th>Email address</th>
                    <th></th>
                    <th></th>
                    <th></th>
                    <th></th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                    <%for (Client client: clientList) {%>
                    <tr>
                        <td><%=client.getId()%></td>
                        <td><%=client.getName()%></td>
                        <td><%=client.getPhoneNumber()%></td>
                        <td><%=client.getEmailAddress()%></td>
                        <td>
                            <form action="${pageContext.request.contextPath}/showInternet">
                                <button class="btn" name="clientId" value="<%=client.getId()%>">Internet</button>
                            </form>
                        </td>
                        <td>
                            <form action="${pageContext.request.contextPath}/showPhone">
                                <button class="btn" name="clientId" value="<%=client.getId()%>">Phone</button>
                            </form>
                        </td>
                        <td>
                            <form action="${pageContext.request.contextPath}/showTelevision">
                                <button class="btn" name="clientId" value="<%=client.getId()%>">Television</button>
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
                    <%}%>
                </tbody>
            </table>
            <div class="container">
                <form action="${pageContext.request.contextPath}/addClient">
                    <button class="btn" name="button" value="add">Add...</button>
                </form>
            </div>
        </div>
    </body>
</html>
