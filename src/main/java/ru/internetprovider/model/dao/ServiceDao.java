package ru.internetprovider.model.dao;

import ru.internetprovider.model.services.Service;
import ru.internetprovider.model.services.TemporalService;

import java.util.List;

public interface ServiceDao<S extends TemporalService, T extends Service> extends Dao<T> {

    List<T> getAll(int clientId);
    List<S> getHistory(int id);
    void update(int id, S s);
    void add(int clientId, S s);
    void suspend(int id);
    void activate(int id);
    void disconnect(int id);
}
