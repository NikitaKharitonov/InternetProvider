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

@WebServlet(name = "ShowTelevision", urlPatterns = "/showTelevision")
public class ShowTelevision extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("televisionId") != null) {
            int id = Integer.parseInt(request.getParameter("televisionId"));
            Television television = DaoUtil.getTelevisionDao().get(id);
            Status status = television.getStatus();
            if (status.equals(Status.ACTIVE))
                DaoUtil.getTelevisionDao().suspend(id);
            else if (status.equals(Status.SUSPENDED)) {
                DaoUtil.getTelevisionDao().activate(id);
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
        List<Television> televisionList;
        televisionList = DaoUtil.getTelevisionDao().getAll(clientId);
        televisionList.sort(Comparator.comparing(Service::getId));
        for (Television television : televisionList) {
            television.getHistory().sort(Comparator.comparing(TelevisionSpecification::getBeginDate));
        }
        request.setAttribute("televisionList", televisionList);
        request.getRequestDispatcher("view/showTelevision.jsp").forward(request, response);
    }
}
