package ru.internetprovider.servlets;

import ru.internetprovider.model.Client;
import ru.internetprovider.model.DBModel;
import ru.internetprovider.model.exceptions.InvalidModelException;

import javax.servlet.ServletException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class Clients extends javax.servlet.http.HttpServlet {

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        DBModel dbModel = new DBModel();
        List<Client> clientList;
        try {
            clientList = dbModel.getClientList();
            request.setAttribute("clientList", clientList);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        request.getRequestDispatcher("view/clients.jsp").forward(request, response);
    }
}
