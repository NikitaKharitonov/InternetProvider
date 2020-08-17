package ru.internetprovider.controller;

import ru.internetprovider.model.Client;

import java.io.IOException;
import java.util.List;

public class ShowClient extends javax.servlet.http.HttpServlet {

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        List<Client> clientList;
        clientList = DaoUtil.getClientDao().getAll();
        request.getSession().setAttribute("clientList", clientList);
        request.setAttribute("clientList", clientList);
        request.getRequestDispatcher("view/showClient.jsp").forward(request, response);
    }
}
