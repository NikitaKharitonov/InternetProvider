package ru.internetprovider.servlets;

import ru.internetprovider.model.DBModel;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "UpdateTelevision", urlPatterns = "/updateTelevision")
public class UpdateTelevision extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DBModel dbModel = new DBModel();
        long televisionId = (long) request.getSession().getAttribute("televisionId");
        int channelsCount = Integer.parseInt(request.getParameter("channelsCount"));
        // todo add television
        response.sendRedirect(request.getContextPath() + "/services");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String televisionId = request.getParameter("televisionId");
        request.getSession().setAttribute("televisionId", televisionId);
        request.getRequestDispatcher("/updateTelevision.jsp").forward(request, response);

    }
}
