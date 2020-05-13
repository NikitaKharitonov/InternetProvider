package ru.internetprovider.servlets;

import ru.internetprovider.model.InternetDao;
import ru.internetprovider.model.TelevisionDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "DisconnectInternet", urlPatterns = "/disconnectInternet")
public class DisconnectInternet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long id = Long.parseLong(request.getParameter("internetId"));
        InternetDao internetDao = new InternetDao();
        internetDao.disconnect(id);
        response.sendRedirect(request.getContextPath() + "/services");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
