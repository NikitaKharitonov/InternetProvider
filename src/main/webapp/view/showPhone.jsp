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
                <% if (phoneList != null) for (Phone phone : phoneList) { %>
                    <% if (!phone.getStatus().equals(Status.DELETED)) { %>
                        <tr>
                            <td><%=phone.getId()%></td>
                            <td><%=formatter.format(phone.getActivationDate())%></td>
                            <td><%=phone.getStatus()%></td>
                            <% List<PhoneSpecification> history = phone.getHistory(); %>
                            <% PhoneSpecification current = history.get(history.size() - 1); %>
                            <td><%=current.getMinsCount()%></td>
                            <td><%=current.getSmsCount()%></td>
                            <td>
                                <form action="${pageContext.request.contextPath}/historyPhone">
                                    <button name="phoneId" value="<%=phone.getId()%>" class="btn">Get history</button>
                                </form>
                            </td>
                            <td>
                                <form action="${pageContext.request.contextPath}/updatePhone">
                                    <button name="phoneId" value="<%=phone.getId()%>" class="btn">Update</button>
                                </form>
                            </td>
                            <td>
                                <form method="post">
                                    <button name="phoneId" value="<%=phone.getId()%>" class="btn"><%=phone.getStatus().equals(Status.ACTIVE) ? "Suspend" : "Activate"%></button>
                                </form>
                            </td>
                            <td>
                                <form method="post">
                                    <button name="delete" value="<%=phone.getId()%>" class="btn" style="color: red">Delete</button>
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
