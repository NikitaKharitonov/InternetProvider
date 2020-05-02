package ru.internetprovider.servlets;

import ru.internetprovider.model.Client;
import ru.internetprovider.model.DBModel;
import ru.internetprovider.model.exceptions.InvalidModelException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "AddClient", urlPatterns = "/addClient")
public class AddClient extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        DBModel dbModel = new DBModel();
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        try {
            dbModel.addClient(new Client(name, phone, email));
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        response.sendRedirect(request.getContextPath() + "/");
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("view/addClient.jsp").forward(request, response);
    }
}
