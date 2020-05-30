package ru.internetprovider.servlets;

import ru.internetprovider.model.TelevisionDao;
import ru.internetprovider.model.services.ClientService;
import ru.internetprovider.model.services.ClientTelevision;
import ru.internetprovider.model.services.Television;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@WebServlet(name = "AddTelevision", urlPatterns = "/addTelevision")
public class AddTelevision extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        TelevisionDao televisionDao = new TelevisionDao();
        long clientId = Long.parseLong(request.getSession().getAttribute("clientId").toString());
        int channelsCount = Integer.parseInt(request.getParameter("channelsCount"));
        Television television = new Television(new Date(), null, channelsCount);
        ClientTelevision clientService = new ClientTelevision(television.getBeginDate(), ClientService.Status.ACTIVE);
        List<Television> internetList = new ArrayList<>();
        internetList.add(television);
        clientService.setServiceList(internetList);
        televisionDao.save(clientId, clientService);
        response.sendRedirect(request.getContextPath() + "/services");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("view/addTelevision.jsp").forward(request, response);
    }
}
