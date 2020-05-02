package ru.internetprovider.servlets;

import ru.internetprovider.model.DBModel;
import ru.internetprovider.model.exceptions.InvalidModelException;
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
import java.util.List;

@WebServlet(name = "TelevisionHistory", urlPatterns = "/televisionHistory")
public class TelevisionHistory extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long televisionId = Long.parseLong(request.getParameter("televisionId"));
        DBModel dbModel = new DBModel();
        try {
            List<Television> televisionList = dbModel.getTelevisionHistory(televisionId);
            request.setAttribute("televisionId", televisionId);
            request.setAttribute("televisionList", televisionList);
            request.getRequestDispatcher("view/televisionHistory.jsp").forward(request, response);
        } catch (SQLException | ServiceNotFoundException exception) {
            exception.printStackTrace();
        }
    }
}
