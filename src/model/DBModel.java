package model;

import model.exceptions.ServiceNotFoundException;
import model.exceptions.UserNotFoundException;
import model.services.Service;

import java.sql.*;

public class DBModel implements Model {
    private static final String URL = "jdbc:postgresql://localhost:5432/provider_db";
    private static final String LOGIN = "postgres";
    private static final String PASSWORD = "root";

    public User getUser(long id) throws UserNotFoundException {
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
            PreparedStatement pstmt = connection.prepareStatement(
                    "SELECT * FROM public.user WHERE id = ?"
            );
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                String email = rs.getString("email");
                String phoneNumber = rs.getString("phone");
                return new User(id, name, phoneNumber, email);
            } else {
                throw new UserNotFoundException("User not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void addUser(User user) {
        // todo
    }

    @Override
    public void removeUser(long id) throws UserNotFoundException {
        // todo
    }

    @Override
    public int getUserCount() {
        // todo
        return 0;
    }

    @Override
    public Service[] getUserServicesByType(long userID, String serviceType) throws ServiceNotFoundException, UserNotFoundException {
        // todo
        return null;
    }

    @Override
    public void addServiceToUser(long userID, Service service) throws UserNotFoundException {
        // todo
    }

}
