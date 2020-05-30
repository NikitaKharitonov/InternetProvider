package ru.internetprovider.servlets;

import ru.internetprovider.model.InternetDao;
import ru.internetprovider.model.PhoneDao;
import ru.internetprovider.model.TelevisionDao;
import ru.internetprovider.model.services.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "Services", urlPatterns = "/services")
public class Services extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("internetId") != null) {
            long id = Long.parseLong(request.getParameter("internetId"));
            InternetDao internetDao = new InternetDao();
            ClientService.Status status = internetDao.get(id).getStatus();
            if (status.equals(ClientService.Status.ACTIVE))
                internetDao.suspend(id);
            else if (status.equals(ClientService.Status.SUSPENDED)) {
                internetDao.activate(id);
            }
        } else         if (request.getParameter("phoneId") != null) {
            long id = Long.parseLong(request.getParameter("phoneId"));
            PhoneDao phoneDao = new PhoneDao();
            ClientService.Status status = phoneDao.get(id).getStatus();
            if (status.equals(ClientService.Status.ACTIVE))
                phoneDao.suspend(id);
            else if (status.equals(ClientService.Status.SUSPENDED)) {
                phoneDao.activate(id);
            }
        } else         if (request.getParameter("televisionId") != null) {
            long id = Long.parseLong(request.getParameter("televisionId"));
            TelevisionDao televisionDao = new TelevisionDao();
            ClientService televisionClientService = televisionDao.get(id);
            ClientService.Status status = televisionClientService.getStatus();
            if (status.equals(ClientService.Status.ACTIVE))
                televisionDao.suspend(id);
            else if (status.equals(ClientService.Status.SUSPENDED)) {
                televisionDao.activate(id);
            }
        }
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long clientId;
        if (request.getParameter("clientId") != null)
            clientId = Long.parseLong(request.getParameter("clientId"));
        else clientId = (long) request.getSession().getAttribute("clientId");
        request.getSession().setAttribute("clientId", clientId);
        InternetDao internetDao = new InternetDao();
        PhoneDao phoneDao = new PhoneDao();
        TelevisionDao televisionDao = new TelevisionDao();
        List<ClientService> internetList;
        List<ClientService> phoneList;
        List<ClientService> televisionList;
        internetList = internetDao.getAll(clientId);
        phoneList = phoneDao.getAll(clientId);
        televisionList = televisionDao.getAll(clientId);
        request.setAttribute("internetList", internetList);
        request.setAttribute("phoneList", phoneList);
        request.setAttribute("televisionList", televisionList);
        request.getRequestDispatcher("view/services.jsp").forward(request, response);
    }
}
