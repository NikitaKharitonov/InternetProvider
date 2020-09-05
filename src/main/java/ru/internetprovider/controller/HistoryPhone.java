package ru.internetprovider.controller;

import ru.internetprovider.model.services.PhoneState;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "HistoryPhone", urlPatterns = "/historyPhone")
public class HistoryPhone extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int phoneId = Integer.parseInt(request.getParameter("phoneId"));
        List<PhoneState> history = DataAccess.getPhoneDao().getHistory(phoneId);
        request.setAttribute("phoneId", phoneId);
        request.setAttribute("history", history);
        request.getRequestDispatcher("view/historyPhone.jsp").forward(request, response);
    }
}
