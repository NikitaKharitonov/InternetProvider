package ru.internetprovider.model;

import ru.internetprovider.model.services.ClientService;
import ru.internetprovider.model.services.Service;

import java.util.List;

public interface ServiceDao<T extends Service> extends Dao<ClientService> {

    List<ClientService> getAll(int clientId);
    List<T> getHistory(int id);
    void update(int id, T t);
    void save(int clientId, T t);
    void suspend(int id);
    void activate(int id);
    void disconnect(int id);
}
