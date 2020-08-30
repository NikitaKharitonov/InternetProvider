package ru.internetprovider.model.dao.implementation.jdbc;

import ru.internetprovider.model.dao.PhoneDao;
import ru.internetprovider.model.services.Phone;
import ru.internetprovider.model.services.PhoneSpecification;
import ru.internetprovider.model.services.Status;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The implementation of the Data Access Object pattern for Phone services
 * using the JDBC technology.
 */
public class JdbcPhoneDao implements PhoneDao {

    @Override
    public List<Phone> getAll(int clientId) {
        List<Phone> phoneList = new ArrayList<>();
        try (Connection connection = JdbcUtil.getDataSource().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM phone WHERE client_id = ?"
            );
            preparedStatement.setLong(1, clientId);
            ResultSet resultSet = preparedStatement.executeQuery();
            phoneList = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                Date activationDate = resultSet.getTimestamp("activation_date");
                Status status = Status.valueOf(resultSet.getString("status"));
                List<PhoneSpecification> temporalPhoneList = getHistory(id);
                Phone phone = new Phone(id, activationDate, status);
                phone.setHistory(temporalPhoneList);
                phoneList.add(phone);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return phoneList;
    }

    @Override
    public List<PhoneSpecification> getHistory(int id) {
        List<PhoneSpecification> history = null;
        try (Connection connection = JdbcUtil.getDataSource().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM phone_specification WHERE phone_id = ? ORDER BY begin_date;"
            );
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            history = new ArrayList<>();
            while (resultSet.next()) {
                Date beginDate = resultSet.getTimestamp("begin_date");
                Date endDate = resultSet.getTimestamp("end_date");
                int minsCount = resultSet.getInt("mins_count");
                int smsCount = resultSet.getInt("sms_count");
                history.add(new PhoneSpecification(beginDate, endDate, minsCount, smsCount));
            }
            return history;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return history;
    }

    @Override
    public void update(int id, PhoneSpecification temporalPhone) {
        try (Connection connection = JdbcUtil.getDataSource().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT status from phone where id = ?"
            );
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Status status = Status.valueOf(resultSet.getString("status"));
                if (status.equals(Status.ACTIVE)) {
                    preparedStatement = connection.prepareStatement(
                            "UPDATE phone_specification SET end_date = NOW() WHERE begin_date " +
                                    "= (SELECT MAX(begin_date) FROM phone_specification WHERE phone_id = ?)"
                    );
                    preparedStatement.setInt(1, id);
                    preparedStatement.executeUpdate();

                } else if (status.equals(Status.SUSPENDED)) {

                    preparedStatement = connection.prepareStatement(
                            "UPDATE phone SET status = ?::status WHERE id = ?"
                    );
                    preparedStatement.setLong(2, id);
                    preparedStatement.setString(1, String.valueOf(Status.ACTIVE));
                    preparedStatement.executeUpdate();
                }
                preparedStatement = connection.prepareStatement(
                        "INSERT INTO phone_specification " +
                                "(phone_id, begin_date, mins_count, sms_count) " +
                                "VALUES (?, NOW(), ?, ?);"
                );
                preparedStatement.setLong(1, id);
                preparedStatement.setInt(2, temporalPhone.getMinsCount());
                preparedStatement.setInt(3, temporalPhone.getSmsCount());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void add(int clientId, PhoneSpecification temporalPhone) {
        try (Connection connection = JdbcUtil.getDataSource().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO phone (client_id, activation_date, status) VALUES (?, NOW(), ?::status) RETURNING id;"
            );
            preparedStatement.setLong(1, clientId);
            preparedStatement.setString(2, String.valueOf(Status.ACTIVE));
            ResultSet rs = preparedStatement.executeQuery();
            rs.next();
            long phoneId = rs.getLong("id");
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO phone_specification " +
                            "(phone_id, begin_date, mins_count, sms_count) " +
                            "VALUES (?, NOW(), ?, ?);"
            );
            preparedStatement.setLong(1, phoneId);
            preparedStatement.setInt(2, temporalPhone.getSmsCount());
            preparedStatement.setInt(3, temporalPhone.getMinsCount());
            if (preparedStatement.executeUpdate() == 0) {
                throw new SQLException("Failed to insert to database");
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public Phone get(int id) {
        Phone phone = null;
        try (Connection connection = JdbcUtil.getDataSource().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM phone WHERE id = ?"
            );
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Date activationDate = resultSet.getTimestamp("activation_date");
                Status status = Status.valueOf(resultSet.getString("status"));
                phone = new Phone(id, activationDate, status);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return phone;
    }

    @Override
    public void suspend(int id) {
        try (Connection connection = JdbcUtil.getDataSource().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE phone_specification SET end_date = NOW() where begin_date = " +
                            "(SELECT MAX(begin_date) FROM phone_specification WHERE phone_id = ? )"
            );
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            preparedStatement = connection.prepareStatement(
                    "UPDATE phone SET status = ?::status WHERE id = ?"
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
                    "UPDATE phone SET status = ?::status WHERE id = ?"
            );
            preparedStatement.setLong(2, id);
            preparedStatement.setString(1, String.valueOf(Status.ACTIVE));
            preparedStatement.executeUpdate();
            preparedStatement = connection.prepareStatement(
                    "INSERT into phone_specification (phone_id, begin_date, mins_count, sms_count) " +
                            "select phone_id, now(), mins_count, sms_count " +
                            "from phone_specification where begin_date = (SELECT MAX(begin_date) FROM phone_specification " +
                            "WHERE phone_id = ?);"
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
                    "SELECT status from phone where id = ?"
            );
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Status status = Status.valueOf(resultSet.getString("status"));
                if (status.equals(Status.ACTIVE)) {
                    preparedStatement = connection.prepareStatement(
                            "UPDATE phone_specification SET end_date = NOW() WHERE begin_date = " +
                                    "(SELECT MAX(begin_date) FROM phone_specification WHERE phone_id = ?)"
                    );
                    preparedStatement.setInt(1, id);
                    preparedStatement.executeUpdate();
                }
            }
            preparedStatement = connection.prepareStatement(
                    "UPDATE phone SET status = ?::status WHERE id = ?"
            );
            preparedStatement.setLong(2, id);
            preparedStatement.setString(1, String.valueOf(Status.DELETED));
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
