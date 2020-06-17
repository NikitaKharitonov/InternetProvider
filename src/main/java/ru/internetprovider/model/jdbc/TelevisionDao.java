package ru.internetprovider.model.jdbc;

import ru.internetprovider.model.ServiceDao;
import ru.internetprovider.model.services.ClientService;
import ru.internetprovider.model.services.ClientTelevision;
import ru.internetprovider.model.services.Status;
import ru.internetprovider.model.services.Television;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TelevisionDao implements ServiceDao<Television> {

    @Override
    public List<ClientService> getAll(int clientId) {
        List<ClientService> televisionClientServiceList = null;
        try (Connection connection = JdbcUtil.getDataSource().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM television WHERE client_id = ?"
            );
            preparedStatement.setLong(1, clientId);
            ResultSet resultSet = preparedStatement.executeQuery();
            televisionClientServiceList = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                Date activationDate = resultSet.getTimestamp("activation_date");
                Status status = Status.valueOf(resultSet.getString("status"));
                List<Television> televisionList = getHistory(id);
                ClientTelevision clientService = new ClientTelevision(id, activationDate, status);
                clientService.setHistory(televisionList);
                televisionClientServiceList.add(clientService);
            }
            return televisionClientServiceList;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return televisionClientServiceList;
    }

    @Override
    public List<Television> getHistory(int id) {
        List<Television> history = null;
        try (Connection connection = JdbcUtil.getDataSource().getConnection()) {
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
    public void update(int id, Television television) {
        try (Connection connection = JdbcUtil.getDataSource().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT status from television where id = ?"
            );
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Status status = Status.valueOf(resultSet.getString("status"));
                if (status.equals(Status.ACTIVE)) {
                    preparedStatement = connection.prepareStatement(
                            "UPDATE television_history SET end_date = NOW() WHERE begin_date = " +
                                    "(SELECT MAX(begin_date) FROM television_history WHERE television_id = ?)"
                    );
                    preparedStatement.setInt(1, id);
                    preparedStatement.executeUpdate();

                } else if (status.equals(Status.SUSPENDED)) {

                    preparedStatement = connection.prepareStatement(
                            "UPDATE television SET status = ?::status WHERE id = ?"
                    );
                    preparedStatement.setLong(2, id);
                    preparedStatement.setString(1, String.valueOf(Status.ACTIVE));
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
    public void save(int clientId, Television television) {
        try (Connection connection = JdbcUtil.getDataSource().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO television (client_id, activation_date, status) VALUES (?, NOW(), ?::status) RETURNING id;"
            );
            preparedStatement.setLong(1, clientId);
            preparedStatement.setString(2, String.valueOf(Status.ACTIVE));
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
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public ClientService get(int id) {
        ClientService clientService = null;
        try (Connection connection = JdbcUtil.getDataSource().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM television WHERE id = ?"
            );
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Date activationDate = resultSet.getTimestamp("activation_date");
                Status status = Status.valueOf(resultSet.getString("status"));
                clientService = new ClientTelevision(id, activationDate, status);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return clientService;
    }

    @Override
    public void suspend(int id) {
        try (Connection connection = JdbcUtil.getDataSource().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE television_history SET end_date = NOW() WHERE begin_date = " +
                            "(SELECT MAX(begin_date) FROM television_history WHERE television_id = ?)"
            );
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            preparedStatement = connection.prepareStatement(
                    "UPDATE television SET status = ?::status WHERE id = ?"
            );
            preparedStatement.setLong(2, id);
            preparedStatement.setString(1, String.valueOf(Status.SUSPENDED));
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void activate(int id) {
        try (Connection connection = JdbcUtil.getDataSource().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE television SET status = ?::status WHERE id = ?"
            );
            preparedStatement.setLong(2, id);
            preparedStatement.setString(1, String.valueOf(Status.ACTIVE));
            preparedStatement.executeUpdate();
            preparedStatement = connection.prepareStatement(
                    "INSERT into television_history (television_id, begin_date, channels_count) " +
                            "select television_id, now(), channels_count " +
                            "from television_history where begin_date = (SELECT MAX(begin_date) FROM television_history " +
                            "WHERE television_id = ?);"
            );
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void disconnect(int id) {
        try (Connection connection = JdbcUtil.getDataSource().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT status from television where id = ?"
            );
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Status status = Status.valueOf(resultSet.getString("status"));
                if (status.equals(Status.ACTIVE)) {
                    preparedStatement = connection.prepareStatement(
                            "UPDATE television_history SET end_date = NOW() WHERE begin_date = " +
                                    "(SELECT MAX(begin_date) FROM television_history WHERE television_id = ?)"
                    );
                    preparedStatement.setInt(1, id);
                    preparedStatement.executeUpdate();
                }
            }
            preparedStatement = connection.prepareStatement(
                    "UPDATE television SET status = ?::status WHERE id = ?"
            );
            preparedStatement.setLong(2, id);
            preparedStatement.setString(1, String.valueOf(Status.DISCONNECTED));
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        // todo?
    }
}
