package ru.internetprovider.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;

public class Clients extends javax.servlet.http.HttpServlet {

    private static final String url = "/WEB-INF/view/clients.jsp";

    @Override
    public void init() throws ServletException {

    }

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String clientIdString = request.getParameter("button");
        if (clientIdString.equals("add")) {
            response.sendRedirect("/provider/addclient");
        }
        else try {
            long clientId = Long.parseLong(clientIdString);
            request.getSession().setAttribute("client_id", clientId);
            response.sendRedirect("/provider/services");
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        request.getRequestDispatcher(url).forward(request, response);
    }
}
