package ru.internetprovider.servlets;

import ru.internetprovider.model.DBModel;
import ru.internetprovider.model.exceptions.ServiceNotFoundException;
import ru.internetprovider.model.services.Internet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "InternetHistory", urlPatterns = "/internetHistory")
public class InternetHistory extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long internetId = Long.parseLong(request.getParameter("internetId"));
        DBModel dbModel = new DBModel();
        try {
            List<Internet> internetList = dbModel.getInternetHistory(internetId);
            request.setAttribute("internetId", internetId);
            request.setAttribute("internetList", internetList);
            request.getRequestDispatcher("view/internetHistory.jsp").forward(request, response);
        } catch (ServiceNotFoundException | SQLException e) {
            throw new ServletException(e.getMessage());
        }
    }
}
