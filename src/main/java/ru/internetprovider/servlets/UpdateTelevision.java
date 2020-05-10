package ru.internetprovider.servlets;

import ru.internetprovider.model.TelevisionDao;
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
    static         TelevisionDao televisionDao = new TelevisionDao();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long televisionId = Long.parseLong((String) request.getSession().getAttribute("televisionId"));
        int channelsCount = Integer.parseInt(request.getParameter("channelsCount"));
        televisionDao.update(televisionId, new Television(new Date(), null, channelsCount));
        response.sendRedirect(request.getContextPath() + "/services");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String televisionId = request.getParameter("televisionId");
        List<Television> televisionList = televisionDao.getHistory(Long.parseLong(televisionId));
        request.setAttribute("television", televisionList.get(televisionList.size() - 1));
        request.getSession().setAttribute("televisionId", televisionId);
        request.getRequestDispatcher("view/updateTelevision.jsp").forward(request, response);

    }
}
