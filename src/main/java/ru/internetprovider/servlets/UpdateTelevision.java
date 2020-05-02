package ru.internetprovider.servlets;

import ru.internetprovider.model.DBModel;
import ru.internetprovider.model.exceptions.ServiceNotFoundException;
import ru.internetprovider.model.services.Internet;
import ru.internetprovider.model.services.Television;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@WebServlet(name = "UpdateTelevision", urlPatterns = "/updateTelevision")
public class UpdateTelevision extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DBModel dbModel = new DBModel();
        long televisionId = Long.parseLong((String) request.getSession().getAttribute("televisionId"));
        int channelsCount = Integer.parseInt(request.getParameter("channelsCount"));
        try {
            dbModel.updateTelevision(televisionId, new Television(new Date(), null, channelsCount));
            response.sendRedirect(request.getContextPath() + "/services");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String televisionId = request.getParameter("televisionId");
        DBModel dbModel = new DBModel();
        try {
            List<Television> televisionList = dbModel.getTelevisionHistory(Long.parseLong(televisionId));
            request.setAttribute("television", televisionList.get(televisionList.size() - 1));
        } catch (SQLException | ServiceNotFoundException e) {
            e.printStackTrace();
        }request.getSession().setAttribute("televisionId", televisionId);
        request.getRequestDispatcher("view/updateTelevision.jsp").forward(request, response);

    }
}
