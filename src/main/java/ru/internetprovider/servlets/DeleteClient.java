package ru.internetprovider.servlets;

import ru.internetprovider.model.DBModel;
import ru.internetprovider.model.exceptions.ClientNotFoundException;
import ru.internetprovider.model.exceptions.InvalidModelException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "DeleteClient", urlPatterns = "/deleteClient")
public class DeleteClient extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long clientId = Long.parseLong(request.getParameter("clientId"));
        DBModel dbModel = new DBModel();
        try {
            dbModel.removeClient(clientId);
        } catch (ClientNotFoundException | SQLException e) {
            throw new ServletException(e.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/");
    }
}
