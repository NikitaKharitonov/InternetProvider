package model;

import model.exceptions.InvalidClientDataException;
import model.exceptions.InvalidModelException;
import model.exceptions.ServiceNotFoundException;
import model.exceptions.ClientNotFoundException;
import model.services.Internet;
import model.services.Phone;
import model.services.Service;
import model.services.Television;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class DBModel implements Model {
    private static final String URL = "jdbc:postgresql://localhost:5432/provider_db";
    private static final String LOGIN = "postgres";
    private static final String PASSWORD = "root";

    private final Connection connection;

    public DBModel() throws InvalidModelException {
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
            this.connection = connection;
        } catch (SQLException e) {
            throw new InvalidModelException("Failed to get database connection");
        }
    }

    public Client getClient(long id) throws ClientNotFoundException, InvalidModelException {
        try (connection) {
            PreparedStatement pstmt = connection.prepareStatement(
                    "SELECT * FROM client WHERE id = ?"
            );
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                String email = rs.getString("email");
                String phoneNumber = rs.getString("phone");
                return new Client(id, name, phoneNumber, email);
            } else {
                throw new ClientNotFoundException("Client not found");
            }
        } catch (SQLException e) {
            throw new InvalidModelException("Failed to get client from database");
        }
    }

    @Override
    public void addClient(Client client) throws InvalidModelException {
        try (connection) {
            PreparedStatement pstmt = connection.prepareStatement(
                    "INSERT INTO client (name, phone_number, email_address) VALUES (?, ?, ?)"
            );
            pstmt.setString(1, client.getName());
            pstmt.setString(2, client.getPhone());
            pstmt.setString(3, client.getEmail());
            if (pstmt.executeUpdate() == 0) {
                throw new InvalidClientDataException();
            }
        } catch (SQLException e) {
            throw new InvalidModelException("Failed to add client to database");
        }
    }

    @Override
    public void removeClient(long id) throws ClientNotFoundException, InvalidModelException {
        try (connection) {
            PreparedStatement pstmt = connection.prepareStatement(
                    "DELETE FROM client WHERE id = ?"
            );
            pstmt.setLong(1, id);
            if (pstmt.executeUpdate() == 0) {
                throw new ClientNotFoundException("Client not found");
            }
        } catch (SQLException e) {
            throw new InvalidModelException("Failed to remove client from database");
        }
    }

    @Override
    public int getClientsCount() throws InvalidModelException {
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM client");
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new InvalidModelException("Failed to get count of client from database");
        }
    }

    private Service.Status getStatusFromString(String status) {
        switch (status) {
            case "PLANNED":
                return Service.Status.PLANNED;
            case "ACTIVE":
                return Service.Status.ACTIVE;
            case "SUSPENDED":
                return Service.Status.SUSPENDED;
            default:
                return Service.Status.DISCONNECTED;
        }
    }

    private Internet.ConnectionType getConnectionTypeFromString(String connectionType) {
        switch (connectionType) {
            case "ADSL":
                return Internet.ConnectionType.ADSL;
            case "Dial_up":
                return Internet.ConnectionType.Dial_up;
            case "ISDN":
                return Internet.ConnectionType.ISDN;
            case "Cable":
                return Internet.ConnectionType.Cable;
            default:
                return Internet.ConnectionType.Fiber;
        }
    }

    private Internet[] getClientInternets(long clientID)
            throws ServiceNotFoundException, SQLException {
        PreparedStatement pstmt = connection.prepareStatement(
                "SELECT internet.id, " +
                        "internet.activation_date, " +
                        "internet_history.date_begin, " +
                        "internet_history.date_end, " +
                        "internet_history.status, " +
                        "internet_history.speed, " +
                        "internet_history.antivirus, " +
                        "internet_history.connection_type " +
                        "FROM internet INNER JOIN internet_history " +
                        "ON internet.id = internet_history.id_internet " +
                        "WHERE id_internet IN (SELECT id FROM internet WHERE id_client = ?) AND " +
                        "date_begin < NOW() AND (date_end > NOW() OR date_end IS NULL);"
        );
        pstmt.setLong(1, clientID);
        ResultSet rs = pstmt.executeQuery();
        Set<Internet> internets = new HashSet<>();
        while (rs.next()) {
            long internetID = rs.getLong("id_internet");
            Date activationDate = rs.getDate("activationDate");
            Date dateBegin = rs.getDate("date_begin");
            Date dateEnd = rs.getDate("date_end");
            Service.Status status = getStatusFromString(rs.getString("status"));
            int speed = rs.getInt("speed");
            boolean antivirus = rs.getBoolean("antivirus");
            Internet.ConnectionType connectionType = getConnectionTypeFromString(
                    rs.getString("connection_type")
            );
            internets.add(new Internet(internetID, activationDate, dateBegin, dateEnd,
                    status, speed, antivirus, connectionType));
        }
        if (internets.size() == 0) {
            throw new ServiceNotFoundException("Services \"Internet\" not found");
        }
        return internets.toArray(new Internet[0]);
    }

    private Phone[] getClientPhones(long clientID)
            throws ServiceNotFoundException, SQLException {
        PreparedStatement pstmt = connection.prepareStatement(
                "SELECT phone.id, " +
                        "phone.activation_date, " +
                        "phone_history.date_begin, " +
                        "phone_history.date_end, " +
                        "phone_history.status, " +
                        "phone_history.minsCount, " +
                        "phone_history.smsCount, " +
                        "FROM phone INNER JOIN phone_history " +
                        "ON phone.id = phone_history.id_phone " +
                        "WHERE id_phone IN (SELECT id FROM phone WHERE id_client = ?) AND " +
                        "date_begin < NOW() AND (date_end > NOW() OR date_end IS NULL);"
        );
        pstmt.setLong(1, clientID);
        ResultSet rs = pstmt.executeQuery();
        Set<Phone> phones = new HashSet<>();
        while (rs.next()) {
            long phoneID = rs.getLong("id_internet");
            Date activationDate = rs.getDate("activationDate");
            Date dateBegin = rs.getDate("date_begin");
            Date dateEnd = rs.getDate("date_end");
            Service.Status status = getStatusFromString(rs.getString("status"));
            int minsCount = rs.getInt("mins_count");
            int smsCount = rs.getInt("sms_count");
            phones.add(new Phone(phoneID, activationDate, dateBegin, dateEnd, status, minsCount, smsCount));
        }
        if (phones.size() == 0) {
            throw new ServiceNotFoundException("Services \"Phone\" not found");
        }
        return phones.toArray(new Phone[0]);
    }

    private Television[] getClientTelevisions(long clientID)
            throws ServiceNotFoundException, SQLException {
        PreparedStatement pstmt = connection.prepareStatement(
                "SELECT television.id, " +
                        "television.activation_date, " +
                        "television_history.date_begin, " +
                        "television_history.date_end, " +
                        "television_history.status, " +
                        "television_history.channelsCount, " +
                        "FROM television INNER JOIN television_history " +
                        "ON television.id = television_history.id_television " +
                        "WHERE id_television IN (SELECT id FROM television WHERE id_client = ?) AND " +
                        "date_begin < NOW() AND (date_end > NOW() OR date_end IS NULL);"
        );
        pstmt.setLong(1, clientID);
        ResultSet rs = pstmt.executeQuery();
        Set<Television> televisions = new HashSet<>();
        while (rs.next()) {
            long televisionID = rs.getLong("id_television");
            Date activationDate = rs.getDate("activationDate");
            Date dateBegin = rs.getDate("date_begin");
            Date dateEnd = rs.getDate("date_end");
            Service.Status status = getStatusFromString(rs.getString("status"));
            int channelsCount = rs.getInt("channels_count");
            televisions.add(new Television(televisionID, activationDate, dateBegin, dateEnd,
                    status, channelsCount));
        }
        if (televisions.size() == 0) {
            throw new ServiceNotFoundException("Services \"Television\" not found");
        }
        return televisions.toArray(new Television[0]);
    }

    @Override
    public Service[] getClientServicesByType(long clientID, String serviceType)
            throws ServiceNotFoundException, ClientNotFoundException, InvalidModelException {
        try (connection) {
            PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM client WHERE id = ?");
            pstmt.setLong(1, clientID);
            ResultSet rs = pstmt.executeQuery();
            if (!rs.next()) {
                throw new ClientNotFoundException("Client not found");
            }
            switch (serviceType) {
                case "Internet":
                    return getClientInternets(clientID);
                case "Phone":
                    return getClientPhones(clientID);
                default:
                    return getClientTelevisions(clientID);
            }
        } catch (SQLException e) {
            throw new InvalidModelException("Failed to get client services from database");
        }
    }

    private void addInternetToClient(long clientID, Internet internet) throws SQLException {
        //todo
    }

    private void addPhoneToClient(long clientID, Phone phone) throws SQLException {
        //todo
    }

    private void addTelevisionToClient(long clientID, Television television) throws SQLException {
        //todo
    }

    @Override
    public void addServiceToClient(long clientID, Service service)
            throws ClientNotFoundException, InvalidModelException {
        try (connection) {
            if (service instanceof Internet) {
                addInternetToClient(clientID, (Internet) service);
            } else if (service instanceof Phone) {
                addPhoneToClient(clientID, (Phone) service);
            } else {
                addTelevisionToClient(clientID, (Television) service);
            }
        } catch (SQLException e) {
            throw new InvalidModelException("Failed add client service to database");
        }
    }

}
