package ru.internetprovider.model;

import ru.internetprovider.model.services.ClientService;
import ru.internetprovider.model.services.Service;

import java.util.List;

public interface ServiceDao<T extends Service> extends Dao<ClientService<Service>> {

    List<ClientService<T>> getAll(long clientId);
    List<T> getHistory(long id);
    void update(long id, T t);
    void save(long clientId, ClientService<T> t);
    void suspend(long id);
    void activate(long id);
    void disconnect(long id);
}
