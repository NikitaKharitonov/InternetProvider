package ru.internetprovider.servlets;

import ru.internetprovider.model.DBModel;
import ru.internetprovider.model.exceptions.InvalidModelException;
import ru.internetprovider.model.exceptions.ServiceNotFoundException;
import ru.internetprovider.model.services.Condition;
import ru.internetprovider.model.services.Internet;
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
        DBModel dbModel = new DBModel();
        // todo
//        List<Condition<Phone>> phoneHistoryList = dbModel.getPhoneHistory(phoneId);
//        request.setAttribute("phoneHistoryList", phoneHistoryList);
        request.getRequestDispatcher("view/phoneHistory.jsp").forward(request, response);
    }
}
