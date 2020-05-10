package ru.internetprovider.servlets;

import ru.internetprovider.model.TelevisionDao;
import ru.internetprovider.model.services.Television;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "TelevisionHistory", urlPatterns = "/televisionHistory")
public class TelevisionHistory extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long televisionId = Long.parseLong(request.getParameter("televisionId"));
        TelevisionDao televisionDao = new TelevisionDao();
        List<Television> televisionList = televisionDao.getHistory(televisionId);
        request.setAttribute("televisionId", televisionId);
        request.setAttribute("televisionList", televisionList);
        request.getRequestDispatcher("view/televisionHistory.jsp").forward(request, response);
    }
}
