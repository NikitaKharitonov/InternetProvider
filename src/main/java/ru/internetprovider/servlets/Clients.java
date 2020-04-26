package ru.internetprovider.servlets;

import ru.internetprovider.model.Client;
import ru.internetprovider.model.DBModel;
import ru.internetprovider.model.exceptions.InvalidModelException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Clients extends javax.servlet.http.HttpServlet {

//    private static final String url = "/WEB-INF/view/clients.jsp";


    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
//        String clientIdString = request.getParameter("button");
//        if (clientIdString.equals("add")) {
//            response.sendRedirect("/provider/addclient");
//        }
//        else try {
//            long clientId = Long.parseLong(clientIdString);
//            request.getSession().setAttribute("client_id", clientId);
//            response.sendRedirect("/provider/services");
//        } catch (NumberFormatException e) {
//            e.printStackTrace();
//        }
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        DBModel dbModel = new DBModel();
        List<Client> clientList;
        try {
            clientList = dbModel.getClients();
            request.setAttribute("clientList", clientList);
        } catch (InvalidModelException e) {
            throw new ServletException(e.getMessage());
        }
        request.getRequestDispatcher("view/clients.jsp").forward(request, response);
    }
}
