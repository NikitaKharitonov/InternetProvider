package ru.internetprovider.controller;

import ru.internetprovider.model.services.PhoneState;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@WebServlet(name = "UpdatePhone", urlPatterns = "/updatePhone")
public class UpdatePhone extends HttpServlet {


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int phoneId = Integer.parseInt((String) request.getSession().getAttribute("phoneId"));
        int minsCount = Integer.parseInt(request.getParameter("minsCount"));
        int smsCount = Integer.parseInt(request.getParameter("smsCount"));
        DaoUtil.getPhoneDao().update(phoneId, new PhoneState(new Date(), null, minsCount, smsCount));
        response.sendRedirect(request.getContextPath() + "/showPhone");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String phoneId = request.getParameter("phoneId");
        List<PhoneState> history = DaoUtil.getPhoneDao().getHistory(Integer.parseInt(phoneId));
        request.setAttribute("phone", history.get(history.size() - 1));
        request.getSession().setAttribute("phoneId", phoneId);
        request.getRequestDispatcher("view/updatePhone.jsp").forward(request, response);
    }
}
