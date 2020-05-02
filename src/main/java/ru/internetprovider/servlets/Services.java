package ru.internetprovider.servlets;

import ru.internetprovider.model.DBModel;
import ru.internetprovider.model.exceptions.ClientNotFoundException;
import ru.internetprovider.model.exceptions.ServiceNotFoundException;
import ru.internetprovider.model.services.ClientService;
import ru.internetprovider.model.services.Internet;
import ru.internetprovider.model.services.Phone;
import ru.internetprovider.model.services.Television;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "Services", urlPatterns = "/services")
public class Services extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long clientId;
        if (request.getParameter("clientId") != null)
            clientId = Long.parseLong(request.getParameter("clientId"));
        else clientId = (long) request.getSession().getAttribute("clientId");
        request.getSession().setAttribute("clientId", clientId);
        DBModel dbModel = new DBModel();
        List<ClientService<Internet>> internetList;
        List<ClientService<Phone>> phoneList;
        List<ClientService<Television>> televisionList;
        try {
            internetList = dbModel.getInternetList(clientId);
            phoneList = dbModel.getPhoneList(clientId);
            televisionList = dbModel.getTelevisionList(clientId);
            request.setAttribute("internetList", internetList);
            request.setAttribute("phoneList", phoneList);
            request.setAttribute("televisionList", televisionList);
        } catch (ClientNotFoundException | SQLException | ServiceNotFoundException e) {
            e.printStackTrace();
        }
        request.getRequestDispatcher("view/services.jsp").forward(request, response);
    }
}
