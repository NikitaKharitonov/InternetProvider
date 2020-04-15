package ru.internetprovider.controller;

import javax.servlet.ServletException;
import java.io.IOException;

public class IndexServlet extends javax.servlet.http.HttpServlet {

    private static final String url = "WEB-INF/view/index.jsp";

    @Override
    public void init() throws ServletException {

    }

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String clientIdString = request.getParameter("button");
        try {
            long clientId = Long.parseLong(clientIdString);
            request.getSession().setAttribute("client_id", clientId);
            response.sendRedirect("/services");
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        request.getRequestDispatcher(url).forward(request, response);
    }
}
