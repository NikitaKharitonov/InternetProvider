package ru.internetprovider.model;

import ru.internetprovider.model.services.ClientPhone;
import ru.internetprovider.model.services.ClientService;
import ru.internetprovider.model.services.Phone;
import ru.internetprovider.model.services.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PhoneDao implements ServiceDao<Phone> {

    @Override
    public List<ClientService> getAll(long clientId) {
        List<ClientService> phoneClientServiceList = new ArrayList<>();
        try (Connection connection = DatabaseHelper.getDataSource().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM phone WHERE client_id = ?"
            );
            preparedStatement.setLong(1, clientId);
            ResultSet resultSet = preparedStatement.executeQuery();
            phoneClientServiceList = new ArrayList<>();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                Date activationDate = resultSet.getTimestamp("activation_date");
                ClientService.Status status = ClientService.Status.valueOf(resultSet.getString("status"));
                List<Phone> phoneList = getHistory(id);
                ClientPhone clientService = new ClientPhone(id, activationDate, status);
                clientService.setServiceList(phoneList);
                phoneClientServiceList.add(clientService);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return phoneClientServiceList;
    }

    @Override
    public List<Phone> getHistory(long id) {
        List<Phone> history = null;
        try (Connection connection = DatabaseHelper.getDataSource().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM phone_history WHERE phone_id = ? ORDER BY begin_date;"
            );
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            history = new ArrayList<>();
            while (resultSet.next()) {
                Date beginDate = resultSet.getTimestamp("begin_date");
                Date endDate = resultSet.getTimestamp("end_date");
                int minsCount = resultSet.getInt("mins_count");
                int smsCount = resultSet.getInt("sms_count");
                history.add(new Phone(beginDate, endDate, minsCount, smsCount));
            }
            return history;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return history;
    }

    @Override
    public void update(long id, Phone phone) {
        try (Connection connection = DatabaseHelper.getDataSource().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT status from phone where id = ?"
            );
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                ClientService.Status status = ClientService.Status.valueOf(resultSet.getString("status"));
                if (status.equals(ClientService.Status.ACTIVE)) {
                    preparedStatement = connection.prepareStatement(
                            "UPDATE phone_history SET end_date = NOW() WHERE begin_date = (SELECT MAX(begin_date) FROM phone_history)"
                    );
                    preparedStatement.executeUpdate();

                } else if (status.equals(ClientService.Status.SUSPENDED)) {

                    preparedStatement = connection.prepareStatement(
                            "UPDATE phone SET status = ?::status WHERE id = ?"
                    );
                    preparedStatement.setLong(2, id);
                    preparedStatement.setString(1, String.valueOf(ClientService.Status.ACTIVE));
                    preparedStatement.executeUpdate();
                }
                preparedStatement = connection.prepareStatement(
                        "INSERT INTO phone_history " +
                                "(phone_id, begin_date, mins_count, sms_count) " +
                                "VALUES (?, NOW(), ?, ?);"
                );
                preparedStatement.setLong(1, id);
                preparedStatement.setInt(2, phone.getMinsCount());
                preparedStatement.setInt(3, phone.getSmsCount());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void save(long clientId, ClientService phoneClientService) {
        try (Connection connection = DatabaseHelper.getDataSource().getConnection()) {
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
            preparedStatement.setInt(2, ((Phone) phoneClientService.getServiceList().get(0)).getSmsCount());
            preparedStatement.setInt(3, ((Phone) phoneClientService.getServiceList().get(0)).getMinsCount());
            if (preparedStatement.executeUpdate() == 0) {
                throw new SQLException("Failed to insert to database");
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public ClientService get(long id) {
        ClientService clientService = null;
        try (Connection connection = DatabaseHelper.getDataSource().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM phone WHERE id = ?"
            );
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Date activationDate = resultSet.getTimestamp("activation_date");
                ClientService.Status status = ClientService.Status.valueOf(resultSet.getString("status"));
                clientService = new ClientPhone(id, activationDate, status);
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
                    "UPDATE phone_history SET end_date = NOW() WHERE begin_date = " +
                            "(SELECT MAX(begin_date) FROM phone_history)"
            );
            preparedStatement.executeUpdate();
            preparedStatement = connection.prepareStatement(
                    "UPDATE phone SET status = ?::status WHERE id = ?"
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
                    "UPDATE phone SET status = ?::status WHERE id = ?"
            );
            preparedStatement.setLong(2, id);
            preparedStatement.setString(1, String.valueOf(ClientService.Status.ACTIVE));
            preparedStatement.executeUpdate();
            preparedStatement = connection.prepareStatement(
                    "INSERT into phone_history (phone_id, begin_date, mins_count, sms_count) " +
                            "select phone_id, now(), mins_count, sms_count " +
                            "from phone_history where begin_date = (SELECT MAX(begin_date) FROM phone_history);"
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
                    "DELETE FROM phone WHERE id = ?"
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
                    "UPDATE phone SET status = ?::status WHERE id = ?"
            );
            preparedStatement.setLong(2, id);
            preparedStatement.setString(1, String.valueOf(ClientService.Status.DISCONNECTED));
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
