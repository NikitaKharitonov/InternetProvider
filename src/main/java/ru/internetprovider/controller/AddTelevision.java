package ru.internetprovider.controller;

import ru.internetprovider.model.services.TelevisionState;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@WebServlet(name = "AddTelevision", urlPatterns = "/addTelevision")
public class AddTelevision extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int clientId = Integer.parseInt(request.getSession().getAttribute("clientId").toString());
        int channelsCount = Integer.parseInt(request.getParameter("channelsCount"));
        TelevisionState televisionState = new TelevisionState(new Date(), null, channelsCount);
        DataAccess.getTelevisionDao().add(clientId, televisionState);
        response.sendRedirect(request.getContextPath() + "/showTelevision");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("view/addTelevision.jsp").forward(request, response);
    }
}
