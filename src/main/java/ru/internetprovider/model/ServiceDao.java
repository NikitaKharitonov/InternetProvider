package ru.internetprovider.model;

import ru.internetprovider.model.services.ClientService;
import ru.internetprovider.model.services.Service;

import java.util.List;

public interface ServiceDao<T extends Service> extends Dao<ClientService> {

    List<ClientService> getAll(long clientId);
    List<T> getHistory(long id);
    void update(long id, T t);
    void save(long clientId, ClientService t);
    void suspend(long id);
    void activate(long id);
    void disconnect(long id);
}
