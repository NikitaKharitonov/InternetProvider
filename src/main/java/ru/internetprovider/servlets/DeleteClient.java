package ru.internetprovider.servlets;

import ru.internetprovider.model.ClientDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "DeleteClient", urlPatterns = "/deleteClient")
public class DeleteClient extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long clientId = Long.parseLong(request.getParameter("clientId"));
        ClientDao clientDao = new ClientDao();
        clientDao.delete(clientId);
        response.sendRedirect(request.getContextPath() + "/");
    }
}
