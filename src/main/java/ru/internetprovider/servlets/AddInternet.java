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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@WebServlet(name = "AddInternet", urlPatterns = "/addInternet")
public class AddInternet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        DBModel dbModel = new DBModel();
        long clientId = Long.parseLong(request.getSession().getAttribute("clientId").toString());
        int speed = Integer.parseInt(request.getParameter("speed"));
        boolean antivirus = request.getParameter("antivirus") != null;
        Internet.ConnectionType connectionType = Internet.ConnectionType.valueOf(request.getParameter("connectionType"));
        try {
            dbModel.addInternet(clientId, new Internet(new Date(), null, speed, antivirus, connectionType));
        } catch (ClientNotFoundException | SQLException e) {
            e.printStackTrace();
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
