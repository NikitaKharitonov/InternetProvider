package ru.internetprovider.model;

import ru.internetprovider.model.services.ClientService;
import ru.internetprovider.model.services.Internet;
import ru.internetprovider.model.services.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InternetDao implements ServiceDao<Internet> {
    @Override
    public List<Internet> getHistory(long id) {
        List<Internet> history = null;
        try (Connection connection = DatabaseHelper.getDataSource().getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(
                    "SELECT * " +
                            "FROM internet_history WHERE internet_id = ? ORDER BY begin_date;"
            );
            pstmt.setLong(1, id);
            ResultSet resultSet = pstmt.executeQuery();
            history = new ArrayList<>();
            while (resultSet.next()) {
                Date beginDate = resultSet.getTimestamp("begin_date");
                Date endDate = resultSet.getTimestamp("end_date");
                int speed = resultSet.getInt("speed");
                boolean antivirus = resultSet.getBoolean("antivirus");
                Internet.ConnectionType connectionType = Internet.ConnectionType.valueOf(resultSet.getString("connection_type"));
                history.add(new Internet(beginDate, endDate, speed, antivirus, connectionType));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return history;
    }

    @Override
    public ClientService<Service> get(long id) {
        ClientService<Service> clientService = null;
        try (Connection connection = DatabaseHelper.getDataSource().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM internet WHERE id = ?"
            );
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Date activationDate = resultSet.getTimestamp("activation_date");
                ClientService.Status status = ClientService.Status.valueOf(resultSet.getString("status"));
                clientService = new ClientService<>(id, activationDate, status);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return clientService;
    }

    @Override
    public List<ClientService<Internet>> getAll(long clientId) {
        List<ClientService<Internet>> clientServiceList = null;
        try (Connection connection = DatabaseHelper.getDataSource().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM internet WHERE client_id = ?"
            );
            preparedStatement.setLong(1, clientId);
            ResultSet resultSet = preparedStatement.executeQuery();
            clientServiceList = new ArrayList<>();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                Date activationDate = resultSet.getTimestamp("activation_date");
                ClientService.Status status = ClientService.Status.valueOf(resultSet.getString("status"));
                List<Internet> internetList = getHistory(id);
                ClientService<Internet> clientService = new ClientService<>(id, activationDate, status);
                clientService.setServiceList(internetList);
                clientServiceList.add(clientService);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return clientServiceList;
    }

    @Override
    public void update(long id, Internet internet) {
        try (Connection connection = DatabaseHelper.getDataSource().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT status from internet where id = ?"
            );
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                ClientService.Status status = ClientService.Status.valueOf(resultSet.getString("status"));
                if (status.equals(ClientService.Status.ACTIVE)) {
                    preparedStatement = connection.prepareStatement(
                            "UPDATE internet_history SET end_date = NOW() WHERE begin_date = (SELECT MAX(begin_date) FROM internet_history)"
                    );
                    preparedStatement.executeUpdate();

                } else if (status.equals(ClientService.Status.SUSPENDED)) {

                    preparedStatement = connection.prepareStatement(
                            "UPDATE internet SET status = ?::status WHERE id = ?"
                    );
                    preparedStatement.setLong(2, id);
                    preparedStatement.setString(1, String.valueOf(ClientService.Status.ACTIVE));
                    preparedStatement.executeUpdate();
                }
                preparedStatement = connection.prepareStatement(
                        "INSERT INTO internet_history " +
                                "(internet_id, begin_date, speed, antivirus, connection_type) " +
                                "VALUES (?, NOW(), ?, ?, ?::connection_type);"
                );
                preparedStatement.setLong(1, id);
                preparedStatement.setInt(2, internet.getSpeed());
                preparedStatement.setBoolean(3, internet.isAntivirus());
                preparedStatement.setString(4, String.valueOf(internet.getConnectionType()));
                preparedStatement.executeUpdate();
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void save(long clientId, ClientService<Internet> internetClientService) {
        try (Connection connection = DatabaseHelper.getDataSource().getConnection()) {
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
            preparedStatement.setInt(2, internetClientService.getServiceList().get(0).getSpeed());
            preparedStatement.setBoolean(3, internetClientService.getServiceList().get(0).isAntivirus());
            preparedStatement.setString(4, String.valueOf(internetClientService.getServiceList().get(0).getConnectionType()));
            if (preparedStatement.executeUpdate() == 0) {
                throw new SQLException("Failed to insert to database");
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void suspend(long id) {
        try (Connection connection = DatabaseHelper.getDataSource().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE internet_history SET end_date = NOW() WHERE begin_date = " +
                            "(SELECT MAX(begin_date) FROM internet_history)"
            );
            preparedStatement.executeUpdate();
            preparedStatement = connection.prepareStatement(
                    "UPDATE internet SET status = ?::status WHERE id = ?"
            );
            preparedStatement.setLong(2, id);
            preparedStatement.setString(1, String.valueOf(ClientService.Status.SUSPENDED));
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void activate(long id) {
        try (Connection connection = DatabaseHelper.getDataSource().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE internet SET status = ?::status WHERE id = ?"
            );
            preparedStatement.setLong(2, id);
            preparedStatement.setString(1, String.valueOf(ClientService.Status.ACTIVE));
            preparedStatement.executeUpdate();
            preparedStatement = connection.prepareStatement(
                    "INSERT into internet_history (internet_id, begin_date, speed, antivirus, connection_type) " +
                            "select internet_id, now(), speed, antivirus, connection_type " +
                            "from internet_history where begin_date = (SELECT MAX(begin_date) FROM internet_history);"
            );
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void disconnect(long id) {
        try (Connection connection = DatabaseHelper.getDataSource().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE internet SET status = ?::status WHERE id = ?"
            );
            preparedStatement.setLong(2, id);
            preparedStatement.setString(1, String.valueOf(ClientService.Status.DISCONNECTED));
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void delete(long id) {
        try (Connection connection = DatabaseHelper.getDataSource().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM internet WHERE id = ?"
            );
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
