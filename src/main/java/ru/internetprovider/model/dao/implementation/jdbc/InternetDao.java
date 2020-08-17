package ru.internetprovider.model.dao.implementation.jdbc;

import ru.internetprovider.model.dao.ServiceDao;
import ru.internetprovider.model.services.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InternetDao implements ServiceDao<Internet> {

    @Override
    public List<Internet> getHistory(int id) {
        List<Internet> history = null;
        try (Connection connection = JdbcUtil.getDataSource().getConnection()) {
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
                ConnectionType connectionType = ConnectionType.valueOf(resultSet.getString("connection_type"));
                history.add(new Internet(beginDate, endDate, speed, antivirus, connectionType));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return history;
    }

    @Override
    public ClientService get(int id) {
        ClientService clientService = null;
        try (Connection connection = JdbcUtil.getDataSource().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM internet WHERE id = ?"
            );
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Date activationDate = resultSet.getTimestamp("activation_date");
                Status status = Status.valueOf(resultSet.getString("status"));
                clientService = new ClientInternet(id, activationDate, status);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return clientService;
    }

    @Override
    public List<ClientService> getAll(int clientId) {
        List<ClientService> clientServiceList = null;
        try (Connection connection = JdbcUtil.getDataSource().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM internet WHERE client_id = ?"
            );
            preparedStatement.setLong(1, clientId);
            ResultSet resultSet = preparedStatement.executeQuery();
            clientServiceList = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                Date activationDate = resultSet.getTimestamp("activation_date");
                Status status = Status.valueOf(resultSet.getString("status"));
                List<Internet> internetList = getHistory(id);
                ClientInternet clientService = new ClientInternet(id, activationDate, status);
                clientService.setHistory(internetList);
                clientServiceList.add(clientService);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return clientServiceList;
    }

    @Override
    public void update(int id, Internet internet) {
        try (Connection connection = JdbcUtil.getDataSource().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT status from internet where id = ?"
            );
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Status status = Status.valueOf(resultSet.getString("status"));
                if (status.equals(Status.ACTIVE)) {
                    preparedStatement = connection.prepareStatement(
                            "UPDATE internet_history SET end_date = NOW() WHERE begin_date " +
                                    "= (SELECT MAX(begin_date) FROM internet_history WHERE internet_id = ?)"
                    );
                    preparedStatement.setInt(1, id);
                    preparedStatement.executeUpdate();

                } else if (status.equals(Status.SUSPENDED)) {

                    preparedStatement = connection.prepareStatement(
                            "UPDATE internet SET status = ?::status WHERE id = ?"
                    );
                    preparedStatement.setLong(2, id);
                    preparedStatement.setString(1, String.valueOf(Status.ACTIVE));
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
    public void save(int clientId, Internet internet) {
        try (Connection connection = JdbcUtil.getDataSource().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO internet (client_id, activation_date, status) VALUES (?, NOW(), ?::status) RETURNING id;"
            );
            preparedStatement.setLong(1, clientId);
            preparedStatement.setString(2, String.valueOf(Status.ACTIVE));
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
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void suspend(int id) {
        try (Connection connection = JdbcUtil.getDataSource().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE internet_history SET end_date = NOW() WHERE begin_date = " +
                            "(SELECT MAX(begin_date) FROM internet_history WHERE internet_id = ?)"
            );
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            preparedStatement = connection.prepareStatement(
                    "UPDATE internet SET status = ?::status WHERE id = ?"
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
                    "UPDATE internet SET status = ?::status WHERE id = ?"
            );
            preparedStatement.setLong(2, id);
            preparedStatement.setString(1, String.valueOf(Status.ACTIVE));
            preparedStatement.executeUpdate();
            preparedStatement = connection.prepareStatement(
                    "INSERT into internet_history (internet_id, begin_date, speed, antivirus, connection_type) " +
                            "select internet_id, now(), speed, antivirus, connection_type " +
                            "from internet_history where begin_date = (SELECT MAX(begin_date) FROM internet_history " +
                            "WHERE internet_id = ?);"
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
                    "SELECT status from internet where id = ?"
            );
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Status status = Status.valueOf(resultSet.getString("status"));
                if (status.equals(Status.ACTIVE)) {
                    preparedStatement = connection.prepareStatement(
                            "UPDATE internet_history SET end_date = NOW() WHERE begin_date = " +
                                    "(SELECT MAX(begin_date) FROM internet_history WHERE internet_id = ?)"
                    );
                    preparedStatement.setInt(1, id);
                    preparedStatement.executeUpdate();
                }
            }
            preparedStatement = connection.prepareStatement(
                    "UPDATE internet SET status = ?::status WHERE id = ?"
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
