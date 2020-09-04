package ru.internetprovider.controller;

import ru.internetprovider.model.services.InternetState;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "HistoryInternet", urlPatterns = "/historyInternet")
public class HistoryInternet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int internetId = Integer.parseInt(request.getParameter("internetId"));
        List<InternetState> history = DaoUtil.getInternetDao().getHistory(internetId);
        request.setAttribute("internetId", internetId);
        request.setAttribute("history", history);
        request.getRequestDispatcher("view/historyInternet.jsp").forward(request, response);
    }
}
