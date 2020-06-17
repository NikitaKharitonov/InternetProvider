package ru.internetprovider.controller;

import ru.internetprovider.model.services.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@WebServlet(name = "AddInternet", urlPatterns = "/addInternet")
public class AddInternet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int clientId = Integer.parseInt(request.getSession().getAttribute("clientId").toString());
        int speed = Integer.parseInt(request.getParameter("speed"));
        boolean antivirus = request.getParameter("antivirus") != null;
        ConnectionType connectionType = ConnectionType.valueOf(request.getParameter("connectionType"));
        Internet internet = new Internet(new Date(), null, speed, antivirus, connectionType);
        DaoUtil.getInternetDao().save(clientId, internet);
        response.sendRedirect(request.getContextPath() + "/services");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ConnectionType[] connectionTypeArray = ConnectionType.values();
        List<ConnectionType> connectionTypeList = new ArrayList<>(Arrays.asList(connectionTypeArray));
        request.getSession().setAttribute("connectionTypeList", connectionTypeList);
        request.getRequestDispatcher("view/addInternet.jsp").forward(request, response);
    }
}
