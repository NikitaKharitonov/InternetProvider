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

@WebServlet(name = "Services", urlPatterns = "/services")
public class Services extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("internetId") != null) {
            int id = Integer.parseInt(request.getParameter("internetId"));
            Status status = DaoUtil.getInternetDao().get(id).getStatus();
            if (status.equals(Status.ACTIVE))
                DaoUtil.getInternetDao().suspend(id);
            else if (status.equals(Status.SUSPENDED)) {
                DaoUtil.getInternetDao().activate(id);
            }
        } else         if (request.getParameter("phoneId") != null) {
            int id = Integer.parseInt(request.getParameter("phoneId"));
            Status status = DaoUtil.getPhoneDao().get(id).getStatus();
            if (status.equals(Status.ACTIVE))
                DaoUtil.getPhoneDao().suspend(id);
            else if (status.equals(Status.SUSPENDED)) {
                DaoUtil.getPhoneDao().activate(id);
            }
        } else         if (request.getParameter("televisionId") != null) {
            int id = Integer.parseInt(request.getParameter("televisionId"));
            ClientService televisionClientService = DaoUtil.getTelevisionDao().get(id);
            Status status = televisionClientService.getStatus();
            if (status.equals(Status.ACTIVE))
                DaoUtil.getTelevisionDao().suspend(id);
            else if (status.equals(Status.SUSPENDED)) {
                DaoUtil.getTelevisionDao().activate(id);
            }
        }
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int clientId;
        if (request.getParameter("clientId") != null)
            clientId = Integer.parseInt(request.getParameter("clientId"));
        else clientId = (int) request.getSession().getAttribute("clientId");
        request.getSession().setAttribute("clientId", clientId);
        List<ClientService> internetList;
        List<ClientService> phoneList;
        List<ClientService> televisionList;
        internetList = DaoUtil.getInternetDao().getAll(clientId);
        phoneList = DaoUtil.getPhoneDao().getAll(clientId);
        televisionList = DaoUtil.getTelevisionDao().getAll(clientId);
        internetList.sort(Comparator.comparing(ClientService::getId));
        phoneList.sort(Comparator.comparing(ClientService::getId));
        televisionList.sort(Comparator.comparing(ClientService::getId));
        for (ClientService clientService: internetList) {
            ((ClientInternet)clientService).getHistory().sort(Comparator.comparing(Internet::getBeginDate));
        }
        for (ClientService clientService: phoneList) {
            ((ClientPhone)clientService).getHistory().sort(Comparator.comparing(Phone::getBeginDate));
        }
        for (ClientService clientService: televisionList) {
            ((ClientTelevision)clientService).getHistory().sort(Comparator.comparing(Television::getBeginDate));
        }
        request.setAttribute("internetList", internetList);
        request.setAttribute("phoneList", phoneList);
        request.setAttribute("televisionList", televisionList);
        request.getRequestDispatcher("view/services.jsp").forward(request, response);
    }
}
