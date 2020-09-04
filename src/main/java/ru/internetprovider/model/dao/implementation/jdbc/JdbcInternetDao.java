package ru.internetprovider.model.dao.implementation.jdbc;

import ru.internetprovider.model.dao.InternetDao;
import ru.internetprovider.model.services.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The implementation of the Data Access Object pattern for Internet services
 * using the JDBC technology.
 */
public class JdbcInternetDao implements InternetDao {

    @Override
    public List<InternetState> getHistory(int id) {
        List<InternetState> history = null;
        try (Connection connection = JdbcUtil.getDataSource().getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(
                    "SELECT * " +
                            "FROM internet_state WHERE internet_id = ? ORDER BY begin_date;"
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
                history.add(new InternetState(beginDate, endDate, speed, antivirus, connectionType));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return history;
    }

    @Override
    public Internet get(int id) {
        Internet internet = null;
        try (Connection connection = JdbcUtil.getDataSource().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM internet WHERE id = ?"
            );
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Date activationDate = resultSet.getTimestamp("activation_date");
                Status status = Status.valueOf(resultSet.getString("status"));
                internet = new Internet(id, activationDate, status);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return internet;
    }

    @Override
    public List<Internet> getAll(int clientId) {
        List<Internet> internetList = null;
        try (Connection connection = JdbcUtil.getDataSource().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM internet WHERE client_id = ?"
            );
            preparedStatement.setLong(1, clientId);
            ResultSet resultSet = preparedStatement.executeQuery();
            internetList = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                Date activationDate = resultSet.getTimestamp("activation_date");
                Status status = Status.valueOf(resultSet.getString("status"));
                List<InternetState> history = getHistory(id);
                Internet internet = new Internet(id, activationDate, status);
                internet.setHistory(history);
                internetList.add(internet);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return internetList;
    }

    @Override
    public void update(int id, InternetState internetState) {
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
                            "UPDATE internet_state SET end_date = NOW() WHERE begin_date " +
                                    "= (SELECT MAX(begin_date) FROM internet_state WHERE internet_id = ?)"
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
                        "INSERT INTO internet_state " +
                                "(internet_id, begin_date, speed, antivirus, connection_type) " +
                                "VALUES (?, NOW(), ?, ?, ?::connection_type);"
                );
                preparedStatement.setLong(1, id);
                preparedStatement.setInt(2, internetState.getSpeed());
                preparedStatement.setBoolean(3, internetState.isAntivirus());
                preparedStatement.setString(4, String.valueOf(internetState.getConnectionType()));
                preparedStatement.executeUpdate();
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void add(int clientId, InternetState internetState) {
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
                    "INSERT INTO internet_state " +
                            "(internet_id, begin_date, speed, antivirus, connection_type) " +
                            "VALUES (?, NOW(), ?, ?, ?::connection_type);"
            );
            preparedStatement.setLong(1, internetId);
            preparedStatement.setInt(2, internetState.getSpeed());
            preparedStatement.setBoolean(3, internetState.isAntivirus());
            preparedStatement.setString(4, String.valueOf(internetState.getConnectionType()));
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
                    "UPDATE internet_state SET end_date = NOW() WHERE begin_date = " +
                            "(SELECT MAX(begin_date) FROM internet_state WHERE internet_id = ?)"
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
                    "INSERT into internet_state (internet_id, begin_date, speed, antivirus, connection_type) " +
                            "select internet_id, now(), speed, antivirus, connection_type " +
                            "from internet_state where begin_date = (SELECT MAX(begin_date) FROM internet_state " +
                            "WHERE internet_id = ?);"
            );
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
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
                            "UPDATE internet_state SET end_date = NOW() WHERE begin_date = " +
                                    "(SELECT MAX(begin_date) FROM internet_state WHERE internet_id = ?)"
                    );
                    preparedStatement.setInt(1, id);
                    preparedStatement.executeUpdate();
                }
            }
            preparedStatement = connection.prepareStatement(
                    "UPDATE internet SET status = ?::status WHERE id = ?"
            );
            preparedStatement.setLong(2, id);
            preparedStatement.setString(1, String.valueOf(Status.DELETED));
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
