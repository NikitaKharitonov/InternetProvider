package ru.internetprovider.servlets;

import ru.internetprovider.model.DBModel;
import ru.internetprovider.model.exceptions.ServiceNotFoundException;
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
import java.util.List;

@WebServlet(name = "UpdatePhone", urlPatterns = "/updatePhone")
public class UpdatePhone extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DBModel dbModel = new DBModel();
        long phoneId = Long.parseLong((String) request.getSession().getAttribute("phoneId"));
        int minsCount = Integer.parseInt(request.getParameter("minsCount"));
        int smsCount = Integer.parseInt(request.getParameter("smsCount"));
        try {
            dbModel.updatePhone(phoneId, new Phone(new Date(), null, minsCount, smsCount));
            response.sendRedirect(request.getContextPath() + "/services");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String phoneId = request.getParameter("phoneId");
        DBModel dbModel = new DBModel();
        try {
            List<Phone> phoneList = dbModel.getPhoneHistory(Long.parseLong(phoneId));
            request.setAttribute("phone", phoneList.get(phoneList.size() - 1));
        } catch (SQLException | ServiceNotFoundException e) {
            e.printStackTrace();
        }
        request.getSession().setAttribute("phoneId", phoneId);
        request.getRequestDispatcher("view/updatePhone.jsp").forward(request, response);
    }
}
