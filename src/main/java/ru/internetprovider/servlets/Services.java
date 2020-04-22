package ru.internetprovider.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "Services", urlPatterns = "/services")
public class Services extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String param = request.getParameter("button");
        if (param.equals("internet")) {
            response.sendRedirect("/provider/addinternet");
        } else if (param.equals("phone")) {
            response.sendRedirect("/provider/addphone");
        } else if (param.equals("television")) {
            response.sendRedirect("/provider/addtelevision");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("WEB-INF/view/services.jsp").forward(request, response);
    }
}
