package ru.internetprovider.controller;

import ru.internetprovider.model.services.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

@WebServlet(name = "ShowInternet", urlPatterns = "/showInternet")
public class ShowInternet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("internetId") != null) {
            int id = Integer.parseInt(request.getParameter("internetId"));
            Status status = DaoUtil.getInternetDao().get(id).getStatus();
            if (status.equals(Status.ACTIVE))
                DaoUtil.getInternetDao().suspend(id);
            else if (status.equals(Status.SUSPENDED)) {
                DaoUtil.getInternetDao().activate(id);
            }
        }
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int clientId;
        if (request.getParameter("clientId") != null)
            clientId = Integer.parseInt(request.getParameter("clientId"));
        else clientId = (int) request.getSession().getAttribute("clientId");
        request.getSession().setAttribute("clientId", clientId);
        List<Internet> internetList;
        internetList = DaoUtil.getInternetDao().getAll(clientId);
        internetList.sort(Comparator.comparing(Service::getId));
        for (Internet internet : internetList) {
            internet.getHistory().sort(Comparator.comparing(TemporalInternet::getBeginDate));
        }
        request.setAttribute("internetList", internetList);
        request.getRequestDispatcher("view/showInternet.jsp").forward(request, response);
    }
}
