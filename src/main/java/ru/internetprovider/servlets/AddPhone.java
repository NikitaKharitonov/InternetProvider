package ru.internetprovider.servlets;

import ru.internetprovider.model.PhoneDao;
import ru.internetprovider.model.services.ClientPhone;
import ru.internetprovider.model.services.ClientService;
import ru.internetprovider.model.services.Phone;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@WebServlet(name = "AddPhone", urlPatterns = "/addPhone")
public class AddPhone extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PhoneDao phoneDao = new PhoneDao();
        long clientId = Long.parseLong(request.getSession().getAttribute("clientId").toString());
        int minsCount = Integer.parseInt(request.getParameter("minsCount"));
        int smsCount = Integer.parseInt(request.getParameter("smsCount"));
        Phone phone = new Phone(new Date(), null, minsCount, smsCount);
        ClientPhone clientService = new ClientPhone(phone.getBeginDate(), ClientService.Status.ACTIVE);
        List<Phone> phoneList = new ArrayList<>();
        phoneList.add(phone);
        clientService.setServiceList(phoneList);
        phoneDao.save(clientId, clientService);
        response.sendRedirect(request.getContextPath() + "/services");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("view/addPhone.jsp").forward(request, response);
    }
}
