package ru.internetprovider.servlets;

import ru.internetprovider.model.Client;
import ru.internetprovider.model.ClientDao;

import java.io.IOException;
import java.util.List;

public class Clients extends javax.servlet.http.HttpServlet {

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        ClientDao clientDao = new ClientDao();
        List<Client> clientList;
        clientList = clientDao.getAll();
        request.setAttribute("clientList", clientList);
        request.getRequestDispatcher("view/clients.jsp").forward(request, response);
    }
}
