package ru.internetprovider.controller;

import ru.internetprovider.model.services.Television;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@WebServlet(name = "UpdateTelevision", urlPatterns = "/updateTelevision")
public class UpdateTelevision extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int televisionId = Integer.parseInt((String) request.getSession().getAttribute("televisionId"));
        int channelsCount = Integer.parseInt(request.getParameter("channelsCount"));
        DaoUtil.getTelevisionDao().update(televisionId, new Television(new Date(), null, channelsCount));
        response.sendRedirect(request.getContextPath() + "/showTelevision");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String televisionId = request.getParameter("televisionId");
        List<Television> televisionList = DaoUtil.getTelevisionDao().getHistory(Integer.parseInt(televisionId));
        request.setAttribute("television", televisionList.get(televisionList.size() - 1));
        request.getSession().setAttribute("televisionId", televisionId);
        request.getRequestDispatcher("view/updateTelevision.jsp").forward(request, response);

    }
}
