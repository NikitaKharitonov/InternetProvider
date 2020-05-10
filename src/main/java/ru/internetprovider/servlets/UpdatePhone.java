package ru.internetprovider.servlets;

import ru.internetprovider.model.PhoneDao;
import ru.internetprovider.model.services.Phone;

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

    static         PhoneDao phoneDao = new PhoneDao();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long phoneId = Long.parseLong((String) request.getSession().getAttribute("phoneId"));
        int minsCount = Integer.parseInt(request.getParameter("minsCount"));
        int smsCount = Integer.parseInt(request.getParameter("smsCount"));
        phoneDao.update(phoneId, new Phone(new Date(), null, minsCount, smsCount));
        response.sendRedirect(request.getContextPath() + "/services");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String phoneId = request.getParameter("phoneId");
        List<Phone> phoneList = phoneDao.getHistory(Long.parseLong(phoneId));
        request.setAttribute("phone", phoneList.get(phoneList.size() - 1));
        request.getSession().setAttribute("phoneId", phoneId);
        request.getRequestDispatcher("view/updatePhone.jsp").forward(request, response);
    }
}
