package ru.internetprovider.model.dao.implementation.jdbc;

import ru.internetprovider.model.Client;
import ru.internetprovider.model.dao.ClientDao;
import ru.internetprovider.model.exceptions.InvalidClientDataException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcClientDao implements ClientDao {


    @Override
    public Client get(int id) {
        Client client = null;
        try (Connection connection = JdbcUtil.getDataSource().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM client WHERE id = ?"
            );
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String email = resultSet.getString("email_address");
                String phoneNumber = resultSet.getString("phone_number");
                client = new Client(id, name, phoneNumber, email);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return client;
    }

    @Override
    public List<Client> getAll() {
        List<Client> clientList = null;
        try (Connection connection = JdbcUtil.getDataSource().getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM client");
            clientList = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String phoneNumber = resultSet.getString("phone_number");
                String emailAddress = resultSet.getString("email_address");
                clientList.add(new Client(id, name, phoneNumber, emailAddress));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return clientList;
    }

    @Override
    public void update(int id, Client client) {
        try (Connection connection = JdbcUtil.getDataSource().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE client SET name = ?, phone_number = ?, email_address = ? WHERE id = ?"
            );
            preparedStatement.setString(1, client.getName());
            preparedStatement.setString(2, client.getPhoneNumber());
            preparedStatement.setString(3, client.getEmailAddress());
            preparedStatement.setLong(4, id);
            if (preparedStatement.executeUpdate() == 0) {
                throw new InvalidClientDataException();
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void add(Client client) {
        try (Connection connection = JdbcUtil.getDataSource().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO client (name, phone_number, email_address) VALUES (?, ?, ?)"
            );
            preparedStatement.setString(1, client.getName());
            preparedStatement.setString(2, client.getPhoneNumber());
            preparedStatement.setString(3, client.getEmailAddress());
            if (preparedStatement.executeUpdate() == 0) {
                throw new InvalidClientDataException();
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        try (Connection connection = JdbcUtil.getDataSource().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM client WHERE id = ?"
            );
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }


}
