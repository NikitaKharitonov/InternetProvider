package ru.internetprovider.controller;

import ru.internetprovider.model.Client;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "UpdateClient", urlPatterns = "/updateClient")
public class UpdateClient extends HttpServlet {


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int clientId = Integer.parseInt((String) request.getSession().getAttribute("clientId"));
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        DataAccess.getClientDao().update(clientId, new Client(name, phone, email));
        response.sendRedirect(request.getContextPath() + "/");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String clientId = request.getParameter("clientId");
        request.getSession().setAttribute("clientId", clientId);
        Client client = DataAccess.getClientDao().get(Integer.parseInt(clientId));
        request.setAttribute("client", client);
        request.getRequestDispatcher("view/updateClient.jsp").forward(request, response);
    }
}
