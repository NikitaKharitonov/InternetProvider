package ru.internetprovider.model.dao;

import ru.internetprovider.model.services.ClientService;
import ru.internetprovider.model.services.Service;

import java.util.List;

public interface ServiceDao<S extends Service, T extends ClientService> extends Dao<T> {

//    CS get(int id);
//    void delete(int id);
    List<T> getAll(int clientId);
    List<S> getHistory(int id);
    void update(int id, S s);
    void add(int clientId, S s);
    void suspend(int id);
    void activate(int id);
    void disconnect(int id);
}
