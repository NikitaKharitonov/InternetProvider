package ru.internetprovider.model.dao;

import ru.internetprovider.model.Client;

import java.util.List;

/**
 * The implementation of the Data Access Object pattern for clients.
 */
public interface ClientDao extends Dao<Client> {
    List<Client> getAll();
    void update(int id, Client client);
    void add(Client client);
}
