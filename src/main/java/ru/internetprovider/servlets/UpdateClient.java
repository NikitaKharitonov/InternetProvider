package ru.internetprovider.servlets;

import ru.internetprovider.model.Client;
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

@WebServlet(name = "UpdateClient", urlPatterns = "/updateClient")
public class UpdateClient extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DBModel dbModel = new DBModel();
        long clientId = Long.parseLong((String) request.getSession().getAttribute("clientId"));
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        try {
            dbModel.updateClient(clientId, new Client(name, phone, email));
            response.sendRedirect(request.getContextPath() + "/");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String clientId = request.getParameter("clientId");
        request.getSession().setAttribute("clientId", clientId);
        DBModel dbModel = new DBModel();
        try {
            Client client = dbModel.getClient(Long.parseLong(clientId));
            request.setAttribute("client", client);
        } catch (ClientNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        request.getRequestDispatcher("view/updateClient.jsp").forward(request, response);
    }
}
