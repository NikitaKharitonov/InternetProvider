package ru.internetprovider.model;

import ru.internetprovider.model.exceptions.ClientNotFoundException;
import ru.internetprovider.model.exceptions.InvalidClientDataException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDao implements Dao<Client> {


    @Override
    public Client get(long id) {
        Client client = null;
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
                client = new Client(id, name, phoneNumber, email);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return client;
    }

    public List<Client> getAll() {
        List<Client> clientList = null;
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM client");
            clientList = new ArrayList<>();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
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

    public void update(long id, Client client) {
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
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void add(Client client) {
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
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void delete(long id) {
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
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
