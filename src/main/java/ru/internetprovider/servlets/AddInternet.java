package ru.internetprovider.servlets;

import ru.internetprovider.model.Client;
import ru.internetprovider.model.DBModel;
import ru.internetprovider.model.exceptions.ClientNotFoundException;
import ru.internetprovider.model.exceptions.InvalidModelException;
import ru.internetprovider.model.services.Internet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebServlet(name = "AddInternet", urlPatterns = "/addInternet")
public class AddInternet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DBModel dbModel = new DBModel();
        long clientId = (long) request.getSession().getAttribute("clientId");
        int speed = Integer.parseInt(request.getParameter("speed"));
        boolean antivirus = Boolean.parseBoolean(request.getParameter("antivirus"));
        Internet.ConnectionType connectionType = Internet.ConnectionType.valueOf(request.getParameter("connectionType"));
        try {
            dbModel.addInternetToClient(clientId, new Internet(speed, antivirus, connectionType));
        } catch (ClientNotFoundException | InvalidModelException e) {
            throw new ServletException(e.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/services");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Internet.ConnectionType[] connectionTypeArray = Internet.ConnectionType.values();
        List<Internet.ConnectionType> connectionTypeList = new ArrayList<>(Arrays.asList(connectionTypeArray));
        request.getSession().setAttribute("connectionTypeList", connectionTypeList);
        request.getRequestDispatcher("view/addInternet.jsp").forward(request, response);
    }
}