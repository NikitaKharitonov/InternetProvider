package ru.internetprovider.model;

import ru.internetprovider.model.exceptions.ClientNotFoundException;
import ru.internetprovider.model.exceptions.InvalidClientDataException;
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

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Client getClient(long id) throws ClientNotFoundException, SQLException {
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM client WHERE id = ?"
            );
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String email = resultSet.getString("email_address");
                String phoneNumber = resultSet.getString("phone_number");
                return new Client(id, name, phoneNumber, email);
            } else {
                throw new ClientNotFoundException();
            }
        }
    }

    @Override
    public List<Client> getClientList() throws SQLException {
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM client");
            List<Client> clientList = new ArrayList<>();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String phoneNumber = resultSet.getString("phone_number");
                String emailAddress = resultSet.getString("email_address");
                clientList.add(new Client(id, name, phoneNumber, emailAddress));
            }
            return clientList;
        }
    }

    @Override
    public void updateClient(long id, Client client) throws SQLException {
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE client SET name = ?, phone_number = ?, email_address = ? WHERE id = ?"
            );
            preparedStatement.setString(1, client.getName());
            preparedStatement.setString(2, client.getPhone());
            preparedStatement.setString(3, client.getEmail());
            preparedStatement.setLong(4, id);
            if (preparedStatement.executeUpdate() == 0) {
                throw new InvalidClientDataException();
            }
        }
    }

    @Override
    public void addClient(Client client) throws SQLException {
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO client (name, phone_number, email_address) VALUES (?, ?, ?)"
            );
            preparedStatement.setString(1, client.getName());
            preparedStatement.setString(2, client.getPhone());
            preparedStatement.setString(3, client.getEmail());
            if (preparedStatement.executeUpdate() == 0) {
                throw new InvalidClientDataException();
            }
        }
    }

    @Override
    public void removeClient(long id) throws ClientNotFoundException, SQLException {
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM client WHERE id = ?"
            );
            preparedStatement.setLong(1, id);
            if (preparedStatement.executeUpdate() == 0) {
                throw new ClientNotFoundException();
            }
        }
    }

    @Override
    public ClientService<Internet> getInternet(long internetId) throws SQLException, ServiceNotFoundException {
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM internet WHERE internetId = ?"
            );
            preparedStatement.setLong(1, internetId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                long id = resultSet.getLong("id");
                Date activationDate = resultSet.getDate("activation_date");
                ClientService.Status status = ClientService.Status.valueOf(resultSet.getString("status"));
                return new ClientService<>(id, activationDate, status);
            } else throw new ServiceNotFoundException();
        }
    }

    @Override
    public List<ClientService<Internet>> getInternetList(long clientId) throws ClientNotFoundException, SQLException, ServiceNotFoundException {
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
            if (!clientExists(connection, clientId)) {
                throw new ClientNotFoundException("clientId: " + clientId);
            }
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM internet WHERE client_id = ?"
            );
            preparedStatement.setLong(1, clientId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<ClientService<Internet>> internetClientService = new ArrayList<>();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                Date activationDate = resultSet.getDate("activation_date");
                ClientService.Status status = ClientService.Status.valueOf(resultSet.getString("status"));
                List<Internet> internetList = getInternetHistory(id);
                ClientService<Internet> clientService = new ClientService<>(id, activationDate, status);
                clientService.setServiceList(internetList);
                internetClientService.add(clientService);
            }
            return internetClientService;
        }
    }

    @Override
    public void updateInternet(long internetId, Internet internet) throws SQLException {
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE internet_history SET end_date = NOW() WHERE begin_date = (SELECT MAX(begin_date) FROM internet_history)"
            );
            preparedStatement.executeUpdate();
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO internet_history " +
                            "(internet_id, begin_date, speed, antivirus, connection_type) " +
                            "VALUES (?, NOW(), ?, ?, ?::connection_type);"
            );
            preparedStatement.setLong(1, internetId);
            preparedStatement.setInt(2, internet.getSpeed());
            preparedStatement.setBoolean(3, internet.isAntivirus());
            preparedStatement.setString(4, String.valueOf(internet.getConnectionType()));
            if (preparedStatement.executeUpdate() == 0) {
                throw new SQLException("Failed to insert to database");
            }
        }
    }

    @Override
    public void addInternet(long clientId, Internet internet) throws ClientNotFoundException, SQLException {
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
            if (!clientExists(connection, clientId)) {
                throw new ClientNotFoundException();
            }
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO internet (client_id, activation_date, status) VALUES (?, NOW(), ?::status) RETURNING id;"
            );
            preparedStatement.setLong(1, clientId);
            preparedStatement.setString(2, String.valueOf(ClientService.Status.ACTIVE));
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            long internetId = resultSet.getLong("id");
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO internet_history " +
                            "(internet_id, begin_date, speed, antivirus, connection_type) " +
                            "VALUES (?, NOW(), ?, ?, ?::connection_type);"
            );
            preparedStatement.setLong(1, internetId);
            preparedStatement.setInt(2, internet.getSpeed());
            preparedStatement.setBoolean(3, internet.isAntivirus());
            preparedStatement.setString(4, String.valueOf(internet.getConnectionType()));
            if (preparedStatement.executeUpdate() == 0) {
                throw new SQLException("Failed to insert to database");
            }
        }
    }

    @Override
    public void removeInternet(long internetId) throws SQLException {
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM internet WHERE id = ?"
            );
            preparedStatement.setLong(1, internetId);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public List<Internet> getInternetHistory(long internetId) throws ServiceNotFoundException, SQLException {
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
            if (!serviceExists(connection, "Internet", internetId)) {
                throw new ServiceNotFoundException("Internet not found");
            }
            PreparedStatement pstmt = connection.prepareStatement(
                    "SELECT * " +
                            "FROM internet_history WHERE internet_id = ? ORDER BY begin_date;"
            );
            pstmt.setLong(1, internetId);
            ResultSet resultSet = pstmt.executeQuery();
            List<Internet> history = new ArrayList<>();
            while (resultSet.next()) {
                Date beginDate = resultSet.getDate("begin_date");
                Date endDate = resultSet.getDate("end_date");
                int speed = resultSet.getInt("speed");
                boolean antivirus = resultSet.getBoolean("antivirus");
                Internet.ConnectionType connectionType = Internet.ConnectionType.valueOf(resultSet.getString("connection_type"));
                history.add(new Internet(beginDate, endDate, speed, antivirus, connectionType));
            }
            return history;
        }
    }

    @Override
    public ClientService<Phone> getPhone(long phoneId) throws SQLException, ServiceNotFoundException {
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM phone WHERE phone_id = ?"
            );
            preparedStatement.setLong(1, phoneId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                long id = resultSet.getLong("id");
                Date activationDate = resultSet.getDate("activation_date");
                ClientService.Status status = ClientService.Status.valueOf(resultSet.getString("status"));
                return new ClientService<>(id, activationDate, status);
            } else throw new ServiceNotFoundException();
        }
    }

    @Override
    public List<ClientService<Phone>> getPhoneList(long clientId) throws ClientNotFoundException, SQLException, ServiceNotFoundException {
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
            if (!clientExists(connection, clientId)) {
                throw new ClientNotFoundException();
            }
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM phone WHERE client_id = ?"
            );
            preparedStatement.setLong(1, clientId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<ClientService<Phone>> phoneClientServiceList = new ArrayList<>();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                Date activationDate = resultSet.getDate("activation_date");
                ClientService.Status status = ClientService.Status.valueOf(resultSet.getString("status"));
                List<Phone> phoneList = getPhoneHistory(id);
                ClientService<Phone> clientService = new ClientService<>(id, activationDate, status);
                clientService.setServiceList(phoneList);
                phoneClientServiceList.add(clientService);
            }
            return phoneClientServiceList;
        }
    }

    @Override
    public void updatePhone(long phoneId, Phone internetClientService) throws SQLException {
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE phone_history SET end_date = NOW() WHERE begin_date = (SELECT MAX(begin_date) FROM phone_history)"
            );
            preparedStatement.executeUpdate();
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO phone_history " +
                            "(phone_id, begin_date, mins_count, sms_count) " +
                            "VALUES (?, NOW(), ?, ?);"
            );
            preparedStatement.setLong(1, phoneId);
            preparedStatement.setInt(2, internetClientService.getMinsCount());
            preparedStatement.setInt(3, internetClientService.getSmsCount());
            if (preparedStatement.executeUpdate() == 0) {
                throw new SQLException("Failed to insert to database");
            }
        }
    }

    @Override
    public void addPhone(long clientId, Phone phone) throws SQLException, ClientNotFoundException {
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
            if (!clientExists(connection, clientId)) {
                throw new ClientNotFoundException();
            }
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO phone (client_id, activation_date, status) VALUES (?, NOW(), ?::status) RETURNING id;"
            );
            preparedStatement.setLong(1, clientId);
            preparedStatement.setString(2, String.valueOf(ClientService.Status.ACTIVE));
            ResultSet rs = preparedStatement.executeQuery();
            rs.next();
            long phoneId = rs.getLong("id");
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO phone_history " +
                            "(phone_id, begin_date, mins_count, sms_count) " +
                            "VALUES (?, NOW(), ?, ?);"
            );
            preparedStatement.setLong(1, phoneId);
            preparedStatement.setInt(2, phone.getSmsCount());
            preparedStatement.setInt(3, phone.getMinsCount());
            if (preparedStatement.executeUpdate() == 0) {
                throw new SQLException("Failed to insert to database");
            }
        }
    }

    @Override
    public void removePhone(long phoneId) throws SQLException {
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM phone WHERE id = ?"
            );
            preparedStatement.setLong(1, phoneId);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public List<Phone> getPhoneHistory(long phoneId) throws ServiceNotFoundException, SQLException {
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
            if (!serviceExists(connection, "Phone", phoneId)) {
                throw new ServiceNotFoundException();
            }
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM phone_history WHERE phone_id = ? ORDER BY begin_date;"
            );
            preparedStatement.setLong(1, phoneId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Phone> history = new ArrayList<>();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                Date beginDate = resultSet.getDate("begin_date");
                Date endDate = resultSet.getDate("end_date");
                int minsCount = resultSet.getInt("mins_count");
                int smsCount = resultSet.getInt("sms_count");
                history.add(new Phone(beginDate, endDate, minsCount, smsCount));
            }
            return history;
        }
    }

    @Override
    public ClientService<Television> getTelevision(long televisionId) throws SQLException, ServiceNotFoundException {
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM television WHERE client_id = ?"
            );
            preparedStatement.setLong(1, televisionId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                long id = resultSet.getLong("id");
                Date activationDate = resultSet.getDate("activation_date");
                ClientService.Status status = ClientService.Status.valueOf(resultSet.getString("status"));
                return new ClientService<>(id, activationDate, status);
            } else throw new ServiceNotFoundException();
        }
    }

    @Override
    public List<ClientService<Television>> getTelevisionList(long clientId)
            throws ClientNotFoundException, SQLException, ServiceNotFoundException {
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
            if (!clientExists(connection, clientId)) {
                throw new ClientNotFoundException();
            }
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM television WHERE client_id = ?"
            );
            preparedStatement.setLong(1, clientId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<ClientService<Television>> televisionClientServiceList = new ArrayList<>();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                Date activationDate = resultSet.getDate("activation_date");
                ClientService.Status status = ClientService.Status.valueOf(resultSet.getString("status"));
                List<Television> televisionList = getTelevisionHistory(id);
                ClientService<Television> clientService = new ClientService<>(id, activationDate, status);
                clientService.setServiceList(televisionList);
                televisionClientServiceList.add(clientService);
            }
            return televisionClientServiceList;
        }
    }

    @Override
    public void updateTelevision(long televisionId, Television internetClientService) throws SQLException {
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE television_history SET end_date = NOW() WHERE begin_date = (SELECT MAX(begin_date) FROM television_history)"
            );
            preparedStatement.executeUpdate();
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO television_history " +
                            "(television_id, begin_date, channels_count) " +
                            "VALUES (?, NOW(), ?);"
            );
            preparedStatement.setLong(1, televisionId);
            preparedStatement.setInt(2, internetClientService.getChannelsCount());
            if (preparedStatement.executeUpdate() == 0) {
                throw new SQLException("Failed to insert to database");
            }
        }
    }

    @Override
    public void addTelevision(long clientId, Television television) throws SQLException, ClientNotFoundException {
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
            if (!clientExists(connection, clientId)) {
                throw new ClientNotFoundException();
            }
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO television (client_id, activation_date, status) VALUES (?, NOW(), ?::status) RETURNING id;"
            );
            preparedStatement.setLong(1, clientId);
            preparedStatement.setString(2, String.valueOf(ClientService.Status.ACTIVE));
            ResultSet rs = preparedStatement.executeQuery();
            rs.next();
            long televisionId = rs.getLong("id");
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO television_history " +
                            "(television_id, begin_date, channels_count) " +
                            "VALUES (?, NOW(), ?);"
            );
            preparedStatement.setLong(1, televisionId);
            preparedStatement.setInt(2, television.getChannelsCount());
            if (preparedStatement.executeUpdate() == 0) {
                throw new SQLException("Failed to insert to database");
            }
        }
    }

    @Override
    public void removeTelevision(long televisionId) throws SQLException {
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM television WHERE id = ?"
            );
            preparedStatement.setLong(1, televisionId);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public List<Television> getTelevisionHistory(long televisionId) throws ServiceNotFoundException, SQLException {
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
            if (!serviceExists(connection, "Television", televisionId)) {
                throw new ServiceNotFoundException();
            }
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM television_history WHERE television_id = ? ORDER BY begin_date;"
            );
            preparedStatement.setLong(1, televisionId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Television> history = new ArrayList<>();
            while (resultSet.next()) {
                Date beginDate = resultSet.getDate("begin_date");
                Date endDate = resultSet.getDate("end_date");
                int channelsCount = resultSet.getInt("channels_count");
                history.add(new Television(beginDate, endDate, channelsCount));
            }
            return history;
        }
    }

    private boolean clientExists(Connection connection, long clientId) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement(
                "SELECT COUNT(*) FROM client WHERE id = ?"
        );
        pstmt.setLong(1, clientId);
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        return rs.getInt(1) > 0;
    }

    private boolean serviceExists(Connection connection, String typename, long serviceId)
            throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement(
                "SELECT COUNT(*) FROM " + typename.toLowerCase() + " WHERE id = ?"
        );
        pstmt.setLong(1, serviceId);
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        return rs.getInt(1) > 0;
    }

}
