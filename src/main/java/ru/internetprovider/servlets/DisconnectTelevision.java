package ru.internetprovider.servlets;

import ru.internetprovider.model.TelevisionDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "DisconnectTelevision", urlPatterns = "/disconnect")
public class DisconnectTelevision extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long id = Long.parseLong(request.getParameter("televisionId"));
        TelevisionDao televisionDao = new TelevisionDao();
        televisionDao.disconnect(id);
        response.sendRedirect(request.getContextPath() + "/services");
    }
}