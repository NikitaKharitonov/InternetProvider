package ru.internetprovider.controller;

import ru.internetprovider.model.services.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

@WebServlet(name = "ShowPhone", urlPatterns = "/showPhone")
public class ShowPhone extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("delete") != null) {
            int id = Integer.parseInt(request.getParameter("delete"));
            DataAccess.getPhoneDao().delete(id);
        } else if (request.getParameter("phoneId") != null) {
            int id = Integer.parseInt(request.getParameter("phoneId"));
            Status status = DataAccess.getPhoneDao().get(id).getStatus();
            if (status.equals(Status.ACTIVE))
                DataAccess.getPhoneDao().suspend(id);
            else if (status.equals(Status.SUSPENDED)) {
                DataAccess.getPhoneDao().activate(id);
            }
        }
        response.sendRedirect(request.getContextPath() + "/showPhone");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int clientId;
        if (request.getParameter("clientId") != null)
            clientId = Integer.parseInt(request.getParameter("clientId"));
        else clientId = (int) request.getSession().getAttribute("clientId");
        request.getSession().setAttribute("clientId", clientId);
        List<Phone> phoneList;
        phoneList = DataAccess.getPhoneDao().getAll(clientId);
        phoneList.sort(Comparator.comparing(Service::getId));
        for (Phone phone : phoneList) {
            phone.getHistory().sort(Comparator.comparing(PhoneState::getBeginDate));
        }
        request.setAttribute("phoneList", phoneList);
        request.getRequestDispatcher("view/showPhone.jsp").forward(request, response);
    }
}
