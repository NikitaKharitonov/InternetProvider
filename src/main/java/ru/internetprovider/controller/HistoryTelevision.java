package ru.internetprovider.controller;

import ru.internetprovider.model.services.TelevisionState;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "HistoryTelevision", urlPatterns = "/historyTelevision")
public class HistoryTelevision extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int televisionId = Integer.parseInt(request.getParameter("televisionId"));
        List<TelevisionState> history = DataAccess.getTelevisionDao().getHistory(televisionId);
        request.setAttribute("televisionId", televisionId);
        request.setAttribute("history", history);
        request.getRequestDispatcher("view/historyTelevision.jsp").forward(request, response);
    }
}
