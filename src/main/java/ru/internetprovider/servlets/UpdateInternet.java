package ru.internetprovider.servlets;

import ru.internetprovider.model.InternetDao;
import ru.internetprovider.model.services.Internet;

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

@WebServlet(name = "UpdateInternet", urlPatterns = "/updateInternet")
public class UpdateInternet extends HttpServlet {

    static InternetDao internetDao = new InternetDao();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long internetId = Long.parseLong((String) request.getSession().getAttribute("internetId"));
        int speed = Integer.parseInt(request.getParameter("speed"));
        boolean antivirus = request.getParameter("antivirus") != null;
        Internet.ConnectionType connectionType = Internet.ConnectionType.valueOf(request.getParameter("connectionType"));
        internetDao.update(internetId, new Internet(new Date(), null, speed, antivirus, connectionType));
        response.sendRedirect(request.getContextPath() + "/services");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String internetId = request.getParameter("internetId");
        List<Internet> internetList = internetDao.getHistory(Long.parseLong(internetId));
        request.setAttribute("internet", internetList.get(internetList.size() - 1));
        Internet.ConnectionType[] connectionTypeArray = Internet.ConnectionType.values();
        List<Internet.ConnectionType> connectionTypeList = new ArrayList<>(Arrays.asList(connectionTypeArray));
        request.getSession().setAttribute("connectionTypeList", connectionTypeList);
        request.getSession().setAttribute("internetId", internetId);
        request.getRequestDispatcher("view/updateInternet.jsp").forward(request, response);
    }
}
