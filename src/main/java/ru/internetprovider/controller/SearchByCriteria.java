package ru.internetprovider.controller;

import ru.internetprovider.model.Client;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "SearchByCriteria", urlPatterns = "/searchByCriteria")
public class SearchByCriteria extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nameSearch = request.getParameter("name-search");
        String phoneSearch = request.getParameter("phone-search");
        String emailSearch = request.getParameter("email-search");
        request.setAttribute("name-search", nameSearch);
        request.setAttribute("phone-search", phoneSearch);
        request.setAttribute("email-search", emailSearch);
        List<Client> clientList = (List<Client>) request.getSession().getAttribute("clientList");
        List<Client> filteredClientList = new ArrayList<>();
        for (Client client: clientList) {
            if ((nameSearch.equals("") || client.getName().contains(nameSearch))
                    && (phoneSearch.equals("") || client.getPhoneNumber().contains(phoneSearch))
                    && (emailSearch.equals("") || client.getEmailAddress().contains(emailSearch))) {
                filteredClientList.add(client);
            }
        }
        request.setAttribute("clientList", filteredClientList);
        request.getRequestDispatcher("view/clients.jsp").forward(request, response);
    }
}
