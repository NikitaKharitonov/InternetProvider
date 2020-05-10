package ru.internetprovider.servlets;

import ru.internetprovider.model.PhoneDao;
import ru.internetprovider.model.services.Phone;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "PhoneHistory", urlPatterns = "/phoneHistory")
public class PhoneHistory extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long phoneId = Long.parseLong(request.getParameter("phoneId"));
        PhoneDao phoneDao = new PhoneDao();
        List<Phone> phoneList = phoneDao.getHistory(phoneId);
        request.setAttribute("phoneId", phoneId);
        request.setAttribute("phoneList", phoneList);
        request.getRequestDispatcher("view/phoneHistory.jsp").forward(request, response);
    }
}
