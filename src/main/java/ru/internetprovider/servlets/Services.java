package ru.internetprovider.servlets;

import ru.internetprovider.model.DBModel;
import ru.internetprovider.model.exceptions.ClientNotFoundException;
import ru.internetprovider.model.exceptions.InvalidModelException;
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
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "Services", urlPatterns = "/services")
public class Services extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String param = request.getParameter("button");
//        if (param.equals("internet")) {
//            response.sendRedirect("/provider/addinternet");
//        } else if (param.equals("phone")) {
//            response.sendRedirect("/provider/addphone");
//        } else if (param.equals("television")) {
//            response.sendRedirect("/provider/addtelevision");
//        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long clientId = Long.parseLong(request.getParameter("clientId"));
        DBModel dbModel = new DBModel();
        List<ClientService<Internet>> internetList;
        List<ClientService<Phone>> phoneList;
        List<ClientService<Television>> televisionList;
        try {
            internetList = dbModel.getClientInternets(clientId);
            phoneList = dbModel.getClientPhones(clientId);
            televisionList = dbModel.getClientTelevisions(clientId);
            request.getSession().setAttribute("clientId", clientId);
            request.setAttribute("internetList", internetList);
            request.setAttribute("phoneList", phoneList);
            request.setAttribute("televisionList", televisionList);
        } catch (ClientNotFoundException | InvalidModelException e) {
            throw new ServletException(e.getMessage());
        }
        request.getRequestDispatcher("view/services.jsp").forward(request, response);
    }
}
