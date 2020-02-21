package model.models;

import model.exceptions.UserNotFoundException;
import model.users.User;

import java.sql.*;

public class DBModel {
    private static final String URL = "jdbc:postgresql://localhost:5432/provider_db";
    private static final String LOGIN = "postgres";
    private static final String PASSWORD = "root";

    public User getUserById(long id) throws UserNotFoundException {
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
            PreparedStatement pstmt = connection.prepareStatement(
                    "SELECT * FROM public.user WHERE id = ?"
            );
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String username = rs.getString("username");
                String firstName = rs.getString("first_name");
                String email = rs.getString("email");
                String phoneNumber = rs.getString("phone_number");
                return new User(id, username, firstName, phoneNumber, email, null);
            } else {
                throw new UserNotFoundException("User not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
