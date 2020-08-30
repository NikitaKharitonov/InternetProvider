package ru.internetprovider.controller;

import ru.internetprovider.model.services.PhoneSpecification;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@WebServlet(name = "AddPhone", urlPatterns = "/addPhone")
public class AddPhone extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int clientId = Integer.parseInt(request.getSession().getAttribute("clientId").toString());
        int minsCount = Integer.parseInt(request.getParameter("minsCount"));
        int smsCount = Integer.parseInt(request.getParameter("smsCount"));
        PhoneSpecification phoneSpecification = new PhoneSpecification(new Date(), null, minsCount, smsCount);
        DaoUtil.getPhoneDao().add(clientId, phoneSpecification);
        response.sendRedirect(request.getContextPath() + "/showPhone");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("view/addPhone.jsp").forward(request, response);
    }
}
