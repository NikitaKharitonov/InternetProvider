package ru.internetprovider.servlets;

import ru.internetprovider.model.DBModel;
import ru.internetprovider.model.exceptions.ClientNotFoundException;
import ru.internetprovider.model.exceptions.InvalidModelException;
import ru.internetprovider.model.services.Internet;
import ru.internetprovider.model.services.Phone;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

@WebServlet(name = "AddPhone", urlPatterns = "/addPhone")
public class AddPhone extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DBModel dbModel = new DBModel();
        long clientId = Long.parseLong(request.getSession().getAttribute("clientId").toString());
        int minsCount = Integer.parseInt(request.getParameter("minsCount"));
        int smsCount = Integer.parseInt(request.getParameter("smsCount"));
        try {
            dbModel.addPhone(clientId, new Phone(new Date(), null, minsCount, smsCount));
        } catch (SQLException | ClientNotFoundException exception) {
            exception.printStackTrace();
        }
        response.sendRedirect(request.getContextPath() + "/services");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("view/addPhone.jsp").forward(request, response);
    }
}
