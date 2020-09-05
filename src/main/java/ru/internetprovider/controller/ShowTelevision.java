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
        if (request.getParameter("delete") != null) {
            int id = Integer.parseInt(request.getParameter("delete"));
            DataAccess.getTelevisionDao().delete(id);
        } else if (request.getParameter("televisionId") != null) {
            int id = Integer.parseInt(request.getParameter("televisionId"));
            Television television = DataAccess.getTelevisionDao().get(id);
            Status status = television.getStatus();
            if (status.equals(Status.ACTIVE))
                DataAccess.getTelevisionDao().suspend(id);
            else if (status.equals(Status.SUSPENDED)) {
                DataAccess.getTelevisionDao().activate(id);
            }
        }
        response.sendRedirect(request.getContextPath() + "/showTelevision");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int clientId;
        if (request.getParameter("clientId") != null)
            clientId = Integer.parseInt(request.getParameter("clientId"));
        else clientId = (int) request.getSession().getAttribute("clientId");
        request.getSession().setAttribute("clientId", clientId);
        List<Television> televisionList;
        televisionList = DataAccess.getTelevisionDao().getAll(clientId);
        televisionList.sort(Comparator.comparing(Service::getId));
        for (Television television : televisionList) {
            television.getHistory().sort(Comparator.comparing(TelevisionState::getBeginDate));
        }
        request.setAttribute("televisionList", televisionList);
        request.getRequestDispatcher("view/showTelevision.jsp").forward(request, response);
    }
}
