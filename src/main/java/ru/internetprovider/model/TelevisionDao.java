package ru.internetprovider.model;

import ru.internetprovider.model.services.ClientService;
import ru.internetprovider.model.services.Service;
import ru.internetprovider.model.services.Television;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TelevisionDao implements ServiceDao<Television> {
    @Override
    public List<ClientService<Television>> getAll(long clientId) {
        List<ClientService<Television>> televisionClientServiceList = null;
        try (Connection connection = DatabaseHelper.getDataSource().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM television WHERE client_id = ?"
            );
            preparedStatement.setLong(1, clientId);
            ResultSet resultSet = preparedStatement.executeQuery();
            televisionClientServiceList = new ArrayList<>();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                Date activationDate = resultSet.getTimestamp("activation_date");
                ClientService.Status status = ClientService.Status.valueOf(resultSet.getString("status"));
                List<Television> televisionList = getHistory(id);
                ClientService<Television> clientService = new ClientService<>(id, activationDate, status);
                clientService.setServiceList(televisionList);
                televisionClientServiceList.add(clientService);
            }
            return televisionClientServiceList;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return televisionClientServiceList;
    }

    @Override
    public List<Television> getHistory(long id) {
        List<Television> history = null;
        try (Connection connection = DatabaseHelper.getDataSource().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM television_history WHERE television_id = ? ORDER BY begin_date;"
            );
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            history = new ArrayList<>();
            while (resultSet.next()) {
                Date beginDate = resultSet.getTimestamp("begin_date");
                Date endDate = resultSet.getTimestamp("end_date");
                int channelsCount = resultSet.getInt("channels_count");
                history.add(new Television(beginDate, endDate, channelsCount));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return history;
    }

    @Override
    public void update(long id, Television television) {
        try (Connection connection = DatabaseHelper.getDataSource().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT status from television where id = ?"
            );
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                ClientService.Status status = ClientService.Status.valueOf(resultSet.getString("status"));
                if (status.equals(ClientService.Status.ACTIVE)) {
                    preparedStatement = connection.prepareStatement(
                            "UPDATE television_history SET end_date = NOW() WHERE begin_date = " +
                                    "(SELECT MAX(begin_date) FROM television_history)"
                    );
                    preparedStatement.executeUpdate();

                } else if (status.equals(ClientService.Status.SUSPENDED)) {

                    preparedStatement = connection.prepareStatement(
                            "UPDATE television SET status = ?::status WHERE id = ?"
                    );
                    preparedStatement.setLong(2, id);
                    preparedStatement.setString(1, String.valueOf(ClientService.Status.ACTIVE));
                    preparedStatement.executeUpdate();
                }
                preparedStatement = connection.prepareStatement(
                        "INSERT INTO television_history " +
                                "(television_id, begin_date, channels_count) " +
                                "VALUES (?, NOW(), ?);"
                );
                preparedStatement.setLong(1, id);
                preparedStatement.setInt(2, television.getChannelsCount());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void save(long clientId, ClientService<Television> televisionClientService) {
        try (Connection connection = DatabaseHelper.getDataSource().getConnection()) {
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
            preparedStatement.setInt(2, televisionClientService.getServiceList().get(0).getChannelsCount());
            if (preparedStatement.executeUpdate() == 0) {
                throw new SQLException("Failed to insert to database");
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public ClientService<Service> get(long id) {
        ClientService<Service> clientService = null;
        try (Connection connection = DatabaseHelper.getDataSource().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM television WHERE id = ?"
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
    public void suspend(long id) {
        try (Connection connection = DatabaseHelper.getDataSource().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE television_history SET end_date = NOW() WHERE begin_date = " +
                            "(SELECT MAX(begin_date) FROM television_history)"
            );
            preparedStatement.executeUpdate();
            preparedStatement = connection.prepareStatement(
                    "UPDATE television SET status = ?::status WHERE id = ?"
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
                    "UPDATE television SET status = ?::status WHERE id = ?"
            );
            preparedStatement.setLong(2, id);
            preparedStatement.setString(1, String.valueOf(ClientService.Status.ACTIVE));
            preparedStatement.executeUpdate();
            preparedStatement = connection.prepareStatement(
                    "INSERT into television_history (television_id, begin_date, channels_count) " +
                            "select television_id, now(), channels_count " +
                            "from television_history where begin_date = (SELECT MAX(begin_date) FROM television_history);"
            );
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void delete(long id) {
        try (Connection connection = DatabaseHelper.getDataSource().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM television WHERE id = ?"
            );
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void disconnect(long id) {
        try (Connection connection = DatabaseHelper.getDataSource().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE television SET status = ?::status WHERE id = ?"
            );
            preparedStatement.setLong(2, id);
            preparedStatement.setString(1, String.valueOf(ClientService.Status.DISCONNECTED));
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
