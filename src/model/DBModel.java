package model;

import model.exceptions.InvalidUserDataException;
import model.exceptions.ServiceNotFoundException;
import model.exceptions.UserNotFoundException;
import model.services.Internet;
import model.services.Service;

import java.sql.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class DBModel implements Model {
    private static final String URL = "jdbc:postgresql://localhost:5432/provider_db";
    private static final String LOGIN = "postgres";
    private static final String PASSWORD = "root";

    public User getUser(long id) throws UserNotFoundException {
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
            PreparedStatement pstmt = connection.prepareStatement(
                    "SELECT * FROM client WHERE id = ?"
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
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
            PreparedStatement pstmt = connection.prepareStatement(
                    "INSERT INTO client (name, phone_number, email_address) VALUES (?, ?, ?)"
            );
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getPhone());
            pstmt.setString(3, user.getEmail());
            if (pstmt.executeUpdate() == 0) {
                throw new InvalidUserDataException();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeUser(long id) throws UserNotFoundException {
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
            PreparedStatement pstmt = connection.prepareStatement(
                    "DELETE FROM client WHERE id = ?"
            );
            pstmt.setLong(1, id);
            if (pstmt.executeUpdate() == 0) {
                throw new UserNotFoundException("User not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getUserCount() {
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM client");
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public Service[] getUserServicesByType(long userID, String serviceType) throws ServiceNotFoundException, UserNotFoundException {
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
            PreparedStatement pstmt1 = connection.prepareStatement("SELECT * FROM client WHERE id = ?");
            pstmt1.setLong(1, userID);
            ResultSet rs1 = pstmt1.executeQuery();
            if (!rs1.next()) {
                throw new UserNotFoundException("User not found");
            }
            PreparedStatement pstmt2 = connection.prepareStatement(
                    "SELECT * FROM internet_history " +
                    "WHERE id_internet IN (SELECT id FROM internet WHERE id_client = ?) " +
                    "AND date_begin < NOW() AND (date_end > NOW() OR date_end IS NULL);"
            );
            pstmt2.setLong(1, userID);
            ResultSet rs2 = pstmt2.executeQuery();
            Set<Service> setServices = new HashSet<>();
            while (rs2.next()) {
                long internetID = rs2.getLong("id_internet");
                Date dateBegin = rs2.getDate("date_begin");
                //Date dateEnd = rs.getDate("date_end");
                String strStatus = rs2.getString("status");
                Service.Status status;
                switch (strStatus) {
                    case "PLANNED":
                        status = Service.Status.PLANNED;
                        break;
                    case "ACTIVE":
                        status = Service.Status.ACTIVE;
                        break;
                    case "SUSPENDED":
                        status = Service.Status.SUSPENDED;
                        break;
                    default:
                        status = Service.Status.DISCONNECTED;
                }
                int speed = rs2.getInt("speed");
                boolean antivirus = rs2.getBoolean("antivirus");
                String strConnectionType = rs2.getString("connection_type");
                Internet.ConnectionType connectionType;
                switch (strConnectionType) {
                    case "ADSL":
                        connectionType = Internet.ConnectionType.ADSL;
                        break;
                    case "Dial_up":
                        connectionType = Internet.ConnectionType.Dial_up;
                        break;
                    case "ISDN":
                        connectionType = Internet.ConnectionType.ISDN;
                        break;
                    case "Cable":
                        connectionType = Internet.ConnectionType.Cable;
                        break;
                    default:
                        connectionType = Internet.ConnectionType.Fiber;
                }

                setServices.add(new Internet(internetID, dateBegin, status, speed, antivirus, connectionType));
            }
            if (setServices.size() == 0) {
                throw new ServiceNotFoundException("Services not found");
            }
            return setServices.toArray(new Service[setServices.size()]);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void addServiceToUser(long userID, Service service) throws UserNotFoundException {
        // todo
    }

}
