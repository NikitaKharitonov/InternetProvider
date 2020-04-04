package ru.internetprovider.model;

import ru.internetprovider.model.exceptions.ClientNotFoundException;
import ru.internetprovider.model.exceptions.InvalidClientDataException;
import ru.internetprovider.model.exceptions.InvalidModelException;
import ru.internetprovider.model.exceptions.ServiceNotFoundException;
import ru.internetprovider.model.services.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DBModel implements Model {

    private static final String URL = "jdbc:postgresql://localhost:5432/provider_db";
    private static final String LOGIN = "postgres";
    private static final String PASSWORD = "root";

    @Override
    public Client getClient(long id) throws ClientNotFoundException, InvalidModelException {
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
            PreparedStatement pstmt = connection.prepareStatement(
                    "SELECT * FROM client WHERE id = ?"
            );
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                String email = rs.getString("email_address");
                String phoneNumber = rs.getString("phone_number");
                return new Client(id, name, phoneNumber, email);
            } else {
                throw new ClientNotFoundException("Client not found");
            }
        } catch (SQLException e) {
            throw new InvalidModelException(e);
        }
    }

    @Override
    public List<Client> getClients() throws InvalidModelException {
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM client");
            List<Client> clients = new ArrayList<>();
            while (rs.next()) {
                long id = rs.getLong("id");
                String name = rs.getString("name");
                String phoneNumber = rs.getString("phone_number");
                String emailAddress = rs.getString("email_address");
                clients.add(new Client(id, name, phoneNumber, emailAddress));
            }
            return clients;
        } catch (SQLException e) {
            throw new InvalidModelException(e);
        }
    }

    @Override
    public void addClient(Client client) throws InvalidModelException {
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
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
            throw new InvalidModelException(e);
        }
    }

    @Override
    public void removeClient(long id) throws ClientNotFoundException, InvalidModelException {
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
            PreparedStatement pstmt = connection.prepareStatement(
                    "DELETE FROM client WHERE id = ?"
            );
            pstmt.setLong(1, id);
            if (pstmt.executeUpdate() == 0) {
                throw new ClientNotFoundException("Client not found");
            }
        } catch (SQLException e) {
            throw new InvalidModelException(e);
        }
    }

    private boolean isExistsClient(Connection connection, long clientId) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement(
                "SELECT COUNT(*) FROM client WHERE id = ?"
        );
        pstmt.setLong(1, clientId);
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        return rs.getInt(1) > 0;
    }

    private boolean isExistsService(Connection connection, String typename, long serviceId)
            throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement(
                "SELECT COUNT(*) FROM " + typename.toLowerCase() + " WHERE id = ?"
        );
        pstmt.setLong(1, serviceId);
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        return rs.getInt(1) > 0;
    }

    private Condition.Status getStatusFromString(String status) {
        switch (status) {
            case "SUSPENDED":
                return Condition.Status.SUSPENDED;
            case "ACTIVE":
                return Condition.Status.ACTIVE;
            default:
                return Condition.Status.DISCONNECTED;
        }
    }

    private Internet.ConnectionType getConnectionTypeFromString(String connectionType) {
        switch (connectionType) {
            case "ADSL":
                return Internet.ConnectionType.ADSL;
            case "DialUp":
                return Internet.ConnectionType.DialUp;
            case "ISDN":
                return Internet.ConnectionType.ISDN;
            case "Cable":
                return Internet.ConnectionType.Cable;
            default:
                return Internet.ConnectionType.Fiber;
        }
    }

    @Override
    public List<Condition<Internet>> getInternetHistory(long internetId)
            throws ServiceNotFoundException, InvalidModelException {
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
            if (!isExistsService(connection, "Internet", internetId)) {
                throw new ServiceNotFoundException("Internet not found");
            }
            PreparedStatement pstmt = connection.prepareStatement(
                    "SELECT id, date_begin, date_end, status, speed, antivirus, connection_type " +
                            "FROM internet_history WHERE id_internet = ? ORDER BY date_begin;"
            );
            pstmt.setLong(1, internetId);
            ResultSet rs = pstmt.executeQuery();
            List<Condition<Internet>> history = new ArrayList<>();
            while (rs.next()) {
                long id = rs.getLong("id");
                Date dateBegin = rs.getDate("date_begin");
                Date dateEnd = rs.getDate("date_end");
                Condition.Status status = getStatusFromString(rs.getString("status"));
                int speed = rs.getInt("speed");
                boolean antivirus = rs.getBoolean("antivirus");
                Internet.ConnectionType connectionType = getConnectionTypeFromString(
                        rs.getString("connection_type")
                );
                history.add(
                        new Condition<>(id, dateBegin, dateEnd, status,
                                new Internet(speed, antivirus, connectionType)
                        )
                );
            }
            return history;
        } catch (SQLException e) {
            throw new InvalidModelException(e);
        }
    }

    @Override
    public List<ClientService<Internet>> getClientInternets(long clientId)
            throws ClientNotFoundException, InvalidModelException {
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
            if (!isExistsClient(connection, clientId)) {
                throw new ClientNotFoundException("Client not found");
            }
            PreparedStatement pstmt = connection.prepareStatement(
                    "SELECT internet.id, " +
                            "internet.activation_date, " +
                            "internet_history.id, " +
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
            pstmt.setLong(1, clientId);
            ResultSet rs = pstmt.executeQuery();
            List<ClientService<Internet>> internets = new ArrayList<>();
            while (rs.next()) {
                long internetId = rs.getLong(1);
                Date activationDate = rs.getDate("activation_date");
                long historyId = rs.getLong(3);
                Date dateBegin = rs.getDate("date_begin");
                Date dateEnd = rs.getDate("date_end");
                Condition.Status status = getStatusFromString(rs.getString("status"));
                int speed = rs.getInt("speed");
                boolean antivirus = rs.getBoolean("antivirus");
                Internet.ConnectionType connectionType = getConnectionTypeFromString(
                        rs.getString("connection_type")
                );
                internets.add(
                        new ClientService<>(internetId, activationDate,
                                new Condition<>(historyId, dateBegin, dateEnd, status,
                                        new Internet(speed, antivirus, connectionType)
                                )
                        )
                );
            }
            return internets;
        } catch (SQLException e) {
            throw new InvalidModelException(e);
        }
    }

    @Override
    public void extendInternet(long internetId, Date date_end)
            throws ServiceNotFoundException, InvalidModelException {
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
            if (!isExistsService(connection, "Internet", internetId)) {
                throw new ServiceNotFoundException("Internet not found");
            }
            //Condition.Status currentStatus = get;
        } catch (SQLException e) {
            throw new InvalidModelException(e);
        }
    }

    @Override
    public List<ClientService<Phone>> getClientPhones(long clientId)
            throws ClientNotFoundException, InvalidModelException {
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
            if (!isExistsClient(connection, clientId)) {
                throw new ClientNotFoundException("Client not found");
            }
            PreparedStatement pstmt = connection.prepareStatement(
                    "SELECT phone.id, " +
                            "phone.activation_date, " +
                            "phone_history.date_begin, " +
                            "phone_history.date_end, " +
                            "phone_history.status, " +
                            "phone_history.mins_count, " +
                            "phone_history.sms_count " +
                            "FROM phone INNER JOIN phone_history " +
                            "ON phone.id = phone_history.id_phone " +
                            "WHERE id_phone IN (SELECT id FROM phone WHERE id_client = ?) AND " +
                            "date_begin < NOW() AND (date_end > NOW() OR date_end IS NULL);"
            );
            pstmt.setLong(1, clientId);
            ResultSet rs = pstmt.executeQuery();
            List<ClientService<Phone>> phones = new ArrayList<>();
            while (rs.next()) {
                long phoneId = rs.getLong("id");
                Date activationDate = rs.getDate("activation_date");
                Date dateBegin = rs.getDate("date_begin");
                Date dateEnd = rs.getDate("date_end");
                Condition.Status status = getStatusFromString(rs.getString("status"));
                int minsCount = rs.getInt("mins_count");
                int smsCount = rs.getInt("sms_count");
                phones.add(
                        new ClientService<>(phoneId, activationDate,
                                new Condition<>(dateBegin, dateEnd, status,
                                        new Phone(minsCount, smsCount)
                                )
                        )
                );
            }
            return phones;
        } catch (SQLException e) {
            throw new InvalidModelException(e);
        }
    }

    @Override
    public List<ClientService<Television>> getClientTelevisions(long clientId)
            throws ClientNotFoundException, InvalidModelException {
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
            if (!isExistsClient(connection, clientId)) {
                throw new ClientNotFoundException("Client not found");
            }
            PreparedStatement pstmt = connection.prepareStatement(
                    "SELECT television.id, " +
                            "television.activation_date, " +
                            "television_history.date_begin, " +
                            "television_history.date_end, " +
                            "television_history.status, " +
                            "television_history.channels_count " +
                            "FROM television INNER JOIN television_history " +
                            "ON television.id = television_history.id_television " +
                            "WHERE id_television IN (SELECT id FROM television WHERE id_client = ?) AND " +
                            "date_begin < NOW() AND (date_end > NOW() OR date_end IS NULL);"
            );
            pstmt.setLong(1, clientId);
            ResultSet rs = pstmt.executeQuery();
            List<ClientService<Television>> televisions = new ArrayList<>();
            while (rs.next()) {
                long televisionId = rs.getLong("id");
                Date activationDate = rs.getDate("activation_date");
                Date dateBegin = rs.getDate("date_begin");
                Date dateEnd = rs.getDate("date_end");
                Condition.Status status = getStatusFromString(rs.getString("status"));
                int channelsCount = rs.getInt("channels_count");
                televisions.add(
                        new ClientService<>(televisionId, activationDate,
                                new Condition<>(dateBegin, dateEnd, status,
                                        new Television(channelsCount)
                                )
                        )
                );
            }
            return televisions;
        } catch (SQLException e) {
            throw new InvalidModelException(e);
        }
    }

    @Override
    public void addInternetToClient(long clientId, Internet internet)
            throws ClientNotFoundException, InvalidModelException {
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
            if (!isExistsClient(connection, clientId)) {
                throw new ClientNotFoundException("Client not found");
            }
            PreparedStatement pstmtInsertIntoInternet = connection.prepareStatement(
                    "INSERT INTO internet (id_client, activation_date) VALUES (?, NOW()) RETURNING id;"
            );
            pstmtInsertIntoInternet.setLong(1, clientId);
            ResultSet rs = pstmtInsertIntoInternet.executeQuery();
            rs.next();
            long internetId = rs.getLong("id");
            PreparedStatement pstmtInsertIntoHistory = connection.prepareStatement(
                    "INSERT INTO internet_history " +
                            "(id_internet, date_begin, status, speed, antivirus, connection_type) " +
                            "VALUES (?, NOW(), ?::status, ?, ?, ?::connection_type);"
            );
            pstmtInsertIntoHistory.setLong(1, internetId);
            pstmtInsertIntoHistory.setString(2, "SUSPENDED");
            pstmtInsertIntoHistory.setInt(3, internet.getSpeed());
            pstmtInsertIntoHistory.setBoolean(4, internet.isAntivirus());
            pstmtInsertIntoHistory.setString(5, internet.getConnectionType().toString());
            if (pstmtInsertIntoHistory.executeUpdate() == 0) {
                throw new SQLException("Failed to insert to database");
            }
        } catch (SQLException e) {
            throw new InvalidModelException(e);
        }
    }
}



//package ru.internetprovider.model;
//
//import ru.internetprovider.model.exceptions.InvalidClientDataException;
//import ru.internetprovider.model.exceptions.InvalidModelException;
//import ru.internetprovider.model.exceptions.ServiceNotFoundException;
//import ru.internetprovider.model.exceptions.ClientNotFoundException;
//import ru.internetprovider.model.services.Internet;
//import ru.internetprovider.model.services.Phone;
//import ru.internetprovider.model.services.Service;
//import ru.internetprovider.model.services.Television;
//
//import java.sql.*;
//import java.util.LinkedList;
//import java.util.List;
//
//public class DBModel implements Model {
//    private static final String URL = "jdbc:postgresql://localhost:5432/provider_db";
//    private static final String LOGIN = "postgres";
//    private static final String PASSWORD = "root";
//
//    public Client getClient(long id) throws ClientNotFoundException, InvalidModelException {
//        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
//            PreparedStatement pstmt = connection.prepareStatement(
//                    "SELECT * FROM client WHERE id = ?"
//            );
//            pstmt.setLong(1, id);
//            ResultSet rs = pstmt.executeQuery();
//            if (rs.next()) {
//                String name = rs.getString("name");
//                String email = rs.getString("email_address");
//                String phoneNumber = rs.getString("phone_number");
//                return new Client(id, name, phoneNumber, email);
//            } else {
//                throw new ClientNotFoundException("Client not found");
//            }
//        } catch (SQLException e) {
//            throw new InvalidModelException(e);
//        }
//    }
//
//    public List<Client> getClients() throws InvalidModelException {
//        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
//            Statement stmt = connection.createStatement();
//            ResultSet rs = stmt.executeQuery("SELECT * FROM client");
//            List<Client> clients = new LinkedList<>();
//            while (rs.next()) {
//                long id = rs.getLong("id");
//                String name = rs.getString("name");
//                String phoneNumber = rs.getString("phone_number");
//                String emailAddress = rs.getString("email_address");
//                clients.add(new Client(id, name, phoneNumber, emailAddress));
//            }
//            return clients;
//        } catch (SQLException e) {
//            throw new InvalidModelException(e);
//        }
//    }
//
//    @Override
//    public void addClient(Client client) throws InvalidModelException {
//        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
//            PreparedStatement pstmt = connection.prepareStatement(
//                    "INSERT INTO client (name, phone_number, email_address) VALUES (?, ?, ?)"
//            );
//            pstmt.setString(1, client.getName());
//            pstmt.setString(2, client.getPhone());
//            pstmt.setString(3, client.getEmail());
//            if (pstmt.executeUpdate() == 0) {
//                throw new InvalidClientDataException();
//            }
//        } catch (SQLException e) {
//            throw new InvalidModelException(e);
//        }
//    }
//
//    @Override
//    public void removeClient(long id) throws ClientNotFoundException, InvalidModelException {
//        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
//            PreparedStatement pstmt = connection.prepareStatement(
//                    "DELETE FROM client WHERE id = ?"
//            );
//            pstmt.setLong(1, id);
//            if (pstmt.executeUpdate() == 0) {
//                throw new ClientNotFoundException("Client not found");
//            }
//        } catch (SQLException e) {
//            throw new InvalidModelException(e);
//        }
//    }
//
//    @Override
//    public int getClientsCount() throws InvalidModelException {
//        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
//            Statement stmt = connection.createStatement();
//            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM client");
//            rs.next();
//            return rs.getInt(1);
//        } catch (SQLException e) {
//            throw new InvalidModelException(e);
//        }
//    }
//

//
//    private List<Service> getClientInternets(Connection connection, long clientID)
//            throws ServiceNotFoundException, SQLException {
//        PreparedStatement pstmt = connection.prepareStatement(
//                "SELECT internet.id, " +
//                        "internet.activation_date, " +
//                        "internet_history.date_begin, " +
//                        "internet_history.date_end, " +
//                        "internet_history.status, " +
//                        "internet_history.speed, " +
//                        "internet_history.antivirus, " +
//                        "internet_history.connection_type " +
//                        "FROM internet INNER JOIN internet_history " +
//                        "ON internet.id = internet_history.id_internet " +
//                        "WHERE id_internet IN (SELECT id FROM internet WHERE id_client = ?) AND " +
//                        "date_begin < NOW() AND (date_end > NOW() OR date_end IS NULL);"
//        );
//        pstmt.setLong(1, clientID);
//        ResultSet rs = pstmt.executeQuery();
//        List<Service> internets = new LinkedList<>();
//        while (rs.next()) {
//            long internetID = rs.getLong("id");
//            Date activationDate = rs.getDate("activation_date");
//            Date dateBegin = rs.getDate("date_begin");
//            Date dateEnd = rs.getDate("date_end");
//            Service.Status status = getStatusFromString(rs.getString("status"));
//            int speed = rs.getInt("speed");
//            boolean antivirus = rs.getBoolean("antivirus");
//            Internet.ConnectionType connectionType = getConnectionTypeFromString(
//                    rs.getString("connection_type")
//            );
//            internets.add(new Internet(internetID, activationDate, dateBegin, dateEnd,
//                    status, speed, antivirus, connectionType));
//        }
//        if (internets.size() == 0) {
//            throw new ServiceNotFoundException("Services \"Internet\" not found");
//        }
//        return internets;
//    }
//
//    private List<Service> getClientPhones(Connection connection, long clientID)
//            throws ServiceNotFoundException, SQLException {
//        PreparedStatement pstmt = connection.prepareStatement(
//                "SELECT phone.id, " +
//                        "phone.activation_date, " +
//                        "phone_history.date_begin, " +
//                        "phone_history.date_end, " +
//                        "phone_history.status, " +
//                        "phone_history.minsCount, " +
//                        "phone_history.smsCount, " +
//                        "FROM phone INNER JOIN phone_history " +
//                        "ON phone.id = phone_history.id_phone " +
//                        "WHERE id_phone IN (SELECT id FROM phone WHERE id_client = ?) AND " +
//                        "date_begin < NOW() AND (date_end > NOW() OR date_end IS NULL);"
//        );
//        pstmt.setLong(1, clientID);
//        ResultSet rs = pstmt.executeQuery();
//        List<Service> phones = new LinkedList<>();
//        while (rs.next()) {
//            long phoneID = rs.getLong("id");
//            Date activationDate = rs.getDate("activation_date");
//            Date dateBegin = rs.getDate("date_begin");
//            Date dateEnd = rs.getDate("date_end");
//            Service.Status status = getStatusFromString(rs.getString("status"));
//            int minsCount = rs.getInt("mins_count");
//            int smsCount = rs.getInt("sms_count");
//            phones.add(new Phone(phoneID, activationDate, dateBegin, dateEnd, status, minsCount, smsCount));
//        }
//        if (phones.size() == 0) {
//            throw new ServiceNotFoundException("Services \"Phone\" not found");
//        }
//        return phones;
//    }
//
//    private List<Service> getClientTelevisions(Connection connection, long clientID)
//            throws ServiceNotFoundException, SQLException {
//        PreparedStatement pstmt = connection.prepareStatement(
//                "SELECT television.id, " +
//                        "television.activation_date, " +
//                        "television_history.date_begin, " +
//                        "television_history.date_end, " +
//                        "television_history.status, " +
//                        "television_history.channelsCount, " +
//                        "FROM television INNER JOIN television_history " +
//                        "ON television.id = television_history.id_television " +
//                        "WHERE id_television IN (SELECT id FROM television WHERE id_client = ?) AND " +
//                        "date_begin < NOW() AND (date_end > NOW() OR date_end IS NULL);"
//        );
//        pstmt.setLong(1, clientID);
//        ResultSet rs = pstmt.executeQuery();
//        List<Service> televisions = new LinkedList<>();
//        while (rs.next()) {
//            long televisionID = rs.getLong("id");
//            Date activationDate = rs.getDate("activation_date");
//            Date dateBegin = rs.getDate("date_begin");
//            Date dateEnd = rs.getDate("date_end");
//            Service.Status status = getStatusFromString(rs.getString("status"));
//            int channelsCount = rs.getInt("channels_count");
//            televisions.add(new Television(televisionID, activationDate, dateBegin, dateEnd,
//                    status, channelsCount));
//        }
//        if (televisions.size() == 0) {
//            throw new ServiceNotFoundException("Services \"Television\" not found");
//        }
//        return televisions;
//    }
//
//    @Override
//    public List<Service> getClientServicesByType(long clientID, String serviceType)
//            throws ServiceNotFoundException, ClientNotFoundException, InvalidModelException {
//        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
//            PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM client WHERE id = ?");
//            pstmt.setLong(1, clientID);
//            ResultSet rs = pstmt.executeQuery();
//            if (!rs.next()) {
//                throw new ClientNotFoundException("Client not found");
//            }
//            switch (serviceType) {
//                case "Internet":
//                    return getClientInternets(connection, clientID);
//                case "Phone":
//                    return getClientPhones(connection, clientID);
//                default:
//                    return getClientTelevisions(connection, clientID);
//            }
//        } catch (SQLException e) {
//            throw new InvalidModelException(e);
//        }
//    }
//
//    private List<Service> getInternetHistory(Connection connection, long id, Date activationDate)
//            throws SQLException {
//        PreparedStatement pstmt = connection.prepareStatement(
//                "SELECT * FROM internet_history WHERE id_internet = ? ORDER BY date_begin;"
//        );
//        pstmt.setLong(1, id);
//        ResultSet rs = pstmt.executeQuery();
//        List<Service> internets = new LinkedList<>();
//        while (rs.next()) {
//            Date dateBegin = rs.getDate("date_begin");
//            Date dateEnd = rs.getDate("date_end");
//            Service.Status status = getStatusFromString(rs.getString("status"));
//            int speed = rs.getInt("speed");
//            boolean antivirus = rs.getBoolean("antivirus");
//            Internet.ConnectionType connectionType = getConnectionTypeFromString(
//                    rs.getString("connection_type")
//            );
//            internets.add(new Internet(id, activationDate, dateBegin, dateEnd,
//                    status, speed, antivirus, connectionType));
//        }
//        return internets;
//    }
//
//    private List<Service> getPhoneHistory(Connection connection, long id, Date activationDate)
//            throws SQLException {
//        PreparedStatement pstmt = connection.prepareStatement(
//                "SELECT * FROM phone_history WHERE id_phone = ? ORDER BY date_begin;"
//        );
//        pstmt.setLong(1, id);
//        ResultSet rs = pstmt.executeQuery();
//        List<Service> phones = new LinkedList<>();
//        while (rs.next()) {
//            Date dateBegin = rs.getDate("date_begin");
//            Date dateEnd = rs.getDate("date_end");
//            Service.Status status = getStatusFromString(rs.getString("status"));
//            int minsCount = rs.getInt("mins_count");
//            int smsCount = rs.getInt("sms_count");
//            phones.add(new Phone(id, activationDate, dateBegin, dateEnd, status, minsCount, smsCount));
//        }
//        return phones;
//    }
//
//    private List<Service> getTelevisionHistory(Connection connection, long id, Date activationDate)
//            throws SQLException {
//        PreparedStatement pstmt = connection.prepareStatement(
//                "SELECT * FROM television_history WHERE id_television = ? ORDER BY date_begin;"
//        );
//        pstmt.setLong(1, id);
//        ResultSet rs = pstmt.executeQuery();
//        List<Service> televisions = new LinkedList<>();
//        while (rs.next()) {
//            Date dateBegin = rs.getDate("date_begin");
//            Date dateEnd = rs.getDate("date_end");
//            Service.Status status = getStatusFromString(rs.getString("status"));
//            int channelsCount = rs.getInt("channels_count");
//            televisions.add(new Television(id, activationDate, dateBegin, dateEnd, status, channelsCount));
//        }
//        return televisions;
//    }
//
//    @Override
//    public List<Service> getServiceHistoryByType(long serviceID, String serviceType)
//            throws ServiceNotFoundException, InvalidModelException {
//        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
//            switch (serviceType) {
//                case "Internet": {
//                    PreparedStatement pstmt = connection.prepareStatement(
//                            "SELECT activation_date FROM internet WHERE id = ?;"
//                    ); // SELECT COUNT(*)
//                    pstmt.setLong(1, serviceID);
//                    ResultSet rs = pstmt.executeQuery();
//                    if (rs.next()) {
//                        return getInternetHistory(connection, serviceID,
//                                rs.getDate("activation_date"));
//                    }
//                }
//                case "Phone": {
//                    PreparedStatement pstmt = connection.prepareStatement(
//                            "SELECT activation_date FROM phone WHERE id = ?;"
//                    );
//                    pstmt.setLong(1, serviceID);
//                    ResultSet rs = pstmt.executeQuery();
//                    if (rs.next()) {
//                        return getPhoneHistory(connection, serviceID, rs.getDate("activation_date"));
//                    }
//                }
//                case "Television": {
//                    PreparedStatement pstmt = connection.prepareStatement(
//                            "SELECT activation_date FROM television WHERE id = ?;"
//                    );
//                    pstmt.setLong(1, serviceID);
//                    ResultSet rs = pstmt.executeQuery();
//                    if (rs.next()) {
//                        return getTelevisionHistory(connection, serviceID,
//                                rs.getDate("activation_date"));
//                    }
//                }
//            }
//            throw new ServiceNotFoundException("Service not found");
//        } catch (SQLException e) {
//            throw new InvalidModelException(e);
//        }
//    }
//
//    private void addInternetToClient(Connection connection, long clientID, Internet internet) throws SQLException {
//        PreparedStatement pstmt1 = connection.prepareStatement(
//                "INSERT INTO internet (id_client, activation_date) VALUES (?, ?) RETURNING id"
//        );
//        pstmt1.setLong(1, clientID);
//        pstmt1.setTimestamp(2, new Timestamp(internet.getActivationDate().getTime()));
//        ResultSet rs = pstmt1.executeQuery();
//        rs.next();
//        long internetID = rs.getLong("id");
//
//        PreparedStatement pstmt2 = connection.prepareStatement(
//                "INSERT INTO internet_history (id_internet, date_begin, date_end, status, " +
//                        "speed, antivirus, connection_type) VALUES (?, ?, ?, ?::status, ?, ?, ?::connection_type)"
//        );
//        pstmt2.setLong(1, internetID);
//        pstmt2.setTimestamp(2, new Timestamp(internet.getDateBegin().getTime()));
//        pstmt2.setTimestamp(3, new Timestamp(internet.getDateEnd().getTime()));
//        pstmt2.setString(4, internet.getStatus().toString());
//        pstmt2.setInt(5, internet.getSpeed());
//        pstmt2.setBoolean(6, internet.isAntivirus());
//        pstmt2.setString(7, internet.getConnectionType().toString());
//        if (pstmt2.executeUpdate() == 0) {
//            throw new SQLException("Failed insert to database");
//        }
//    }
//
//    private void addPhoneToClient(Connection connection, long clientID, Phone phone) throws SQLException {
//        PreparedStatement pstmt1 = connection.prepareStatement(
//                "INSERT INTO phone (id_client, activation_date) VALUES (?, ?) RETURNING id"
//        );
//        pstmt1.setLong(1, clientID);
//        pstmt1.setTimestamp(2, new Timestamp(phone.getActivationDate().getTime()));
//        ResultSet rs = pstmt1.executeQuery();
//        rs.next();
//        long phoneID = rs.getLong("id");
//
//        PreparedStatement pstmt2 = connection.prepareStatement(
//                "INSERT INTO phone_history (id_phone, date_begin, date_end, status, " +
//                        "mins_count, sms_count) VALUES (?, ?, ?, ?::status, ?, ?)"
//        );
//        pstmt2.setLong(1, phoneID);
//        pstmt2.setTimestamp(2, new Timestamp(phone.getDateBegin().getTime()));
//        pstmt2.setTimestamp(3, new Timestamp(phone.getDateEnd().getTime()));
//        pstmt2.setString(4, phone.getStatus().toString());
//        pstmt2.setInt(5, phone.getMinsCount());
//        pstmt2.setInt(6, phone.getSmsCount());
//        if (pstmt2.executeUpdate() == 0) {
//            throw new SQLException("Failed insert to database");
//        }
//    }
//
//    private void addTelevisionToClient(Connection connection, long clientID, Television television)
//            throws SQLException {
//        PreparedStatement pstmt1 = connection.prepareStatement(
//                "INSERT INTO television (id_client, activation_date) VALUES (?, ?) RETURNING id"
//        );
//        pstmt1.setLong(1, clientID);
//        pstmt1.setTimestamp(2, new Timestamp(television.getActivationDate().getTime()));
//        ResultSet rs = pstmt1.executeQuery();
//        rs.next();
//        long televisionID = rs.getLong("id");
//
//        PreparedStatement pstmt2 = connection.prepareStatement(
//                "INSERT INTO television_history (id_television, date_begin, date_end, status, " +
//                        "channels_count) VALUES (?, ?, ?, ?::status, ?)"
//        );
//        pstmt2.setLong(1, televisionID);
//        pstmt2.setTimestamp(2, new Timestamp(television.getDateBegin().getTime()));
//        pstmt2.setTimestamp(3, new Timestamp(television.getDateEnd().getTime()));
//        pstmt2.setString(4, television.getStatus().toString());
//        pstmt2.setInt(5, television.getChannelsCount());
//        if (pstmt2.executeUpdate() == 0) {
//            throw new SQLException("Failed insert to database");
//        }
//    }
//
//    @Override
//    public void addServiceToClient(long clientID, Service service)
//            throws ClientNotFoundException, InvalidModelException {
//        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
//            PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM client WHERE id = ?");
//            pstmt.setLong(1, clientID);
//            ResultSet rs = pstmt.executeQuery();
//            if (!rs.next()) {
//                throw new ClientNotFoundException("Client not found");
//            }
//            if (service instanceof Internet) {
//                addInternetToClient(connection, clientID, (Internet) service);
//            } else if (service instanceof Phone) {
//                addPhoneToClient(connection, clientID, (Phone) service);
//            } else {
//                addTelevisionToClient(connection, clientID, (Television) service);
//            }
//        } catch (SQLException e) {
//            throw new InvalidModelException(e);
//        }
//    }
//
//}
