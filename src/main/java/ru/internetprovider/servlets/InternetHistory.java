package ru.internetprovider.servlets;

import ru.internetprovider.model.InternetDao;
import ru.internetprovider.model.services.Internet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "InternetHistory", urlPatterns = "/internetHistory")
public class InternetHistory extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long internetId = Long.parseLong(request.getParameter("internetId"));
        InternetDao internetDao = new InternetDao();
        List<Internet> internetList = internetDao.getHistory(internetId);
        request.setAttribute("internetId", internetId);
        request.setAttribute("internetList", internetList);
        request.getRequestDispatcher("view/internetHistory.jsp").forward(request, response);
    }
}
