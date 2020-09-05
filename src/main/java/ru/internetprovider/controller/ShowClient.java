package ru.internetprovider.controller;

import ru.internetprovider.model.Client;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ShowClient extends javax.servlet.http.HttpServlet {

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("clientId") != null) {
            int clientId = Integer.parseInt(request.getParameter("clientId"));
            DataAccess.getClientDao().delete(clientId);
        }
        response.sendRedirect(request.getContextPath() + "/");
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        if (request.getParameter("search") != null) {
            String nameSearch = request.getParameter("name-search");
            String phoneSearch = request.getParameter("phone-search");
            String emailSearch = request.getParameter("email-search");
            request.setAttribute("name-search", nameSearch);
            request.setAttribute("phone-search", phoneSearch);
            request.setAttribute("email-search", emailSearch);
            List<Client> clientList = (List<Client>) request.getSession().getAttribute("clientList");
            List<Client> filteredClientList = new ArrayList<>();
            for (Client client: clientList) {
                if ((nameSearch.equals("") || client.getName().toLowerCase().contains(nameSearch.toLowerCase()))
                        && (phoneSearch.equals("") || client.getPhoneNumber().contains(phoneSearch))
                        && (emailSearch.equals("") || client.getEmailAddress().toLowerCase().contains(emailSearch.toLowerCase()))) {
                    filteredClientList.add(client);
                }
            }
            request.setAttribute("clientList", filteredClientList);
        } else {
            List<Client> clientList;
            clientList = DataAccess.getClientDao().getAll();
            request.getSession().setAttribute("clientList", clientList);
            request.setAttribute("clientList", clientList);
        }
        request.getRequestDispatcher("view/showClient.jsp").forward(request, response);
    }
}
