package ru.internetprovider.controller;

import ru.internetprovider.model.Client;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

public class IndexServlet extends javax.servlet.http.HttpServlet {

    private static final String url = "WEB-INF/view/index.jsp";
    private List<Client> clients;

    @Override
    public void init() throws ServletException {
        clients = new java.util.concurrent.CopyOnWriteArrayList<>();
        clients.add(new Client("Nikita", "123", "nikita@mail.ru"));
        clients.add(new Client("Denis", "234", "denis@mail.ru"));
        clients.add(new Client("Andrey", "345", "andrey@mail.eu"));
    }

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        request.setAttribute("clients", clients);
        request.getRequestDispatcher(url).forward(request, response);
    }
}
