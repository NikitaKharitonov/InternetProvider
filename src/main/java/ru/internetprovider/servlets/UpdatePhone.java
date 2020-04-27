package ru.internetprovider.servlets;

import ru.internetprovider.model.DBModel;
import ru.internetprovider.model.services.Internet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "UpdatePhone", urlPatterns = "/updatePhone")
public class UpdatePhone extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DBModel dbModel = new DBModel();
        long phoneId = (long) request.getSession().getAttribute("phoneId");
        int minsCount = Integer.parseInt(request.getParameter("minsCount"));
        int smsCount = Integer.parseInt(request.getParameter("smsCount"));
        Internet.ConnectionType connectionType = Internet.ConnectionType.valueOf(request.getParameter("connectionType"));
        // todo add phone
        response.sendRedirect(request.getContextPath() + "/services");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String phoneId = request.getParameter("phoneId");
        request.getSession().setAttribute("phoneId", phoneId);
        request.getRequestDispatcher("view/updatePhone.jsp").forward(request, response);
    }
}
