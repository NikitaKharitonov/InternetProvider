package ru.internetprovider.model.dao;

import ru.internetprovider.model.services.Service;
import ru.internetprovider.model.services.ServiceState;

import java.util.List;

/**
 * The implementation of the Data Access Object pattern for abstract services.
 * @param <S> the abstract service.
 * @param <T> the abstract service's state.
 */
public interface ServiceDao<S extends ServiceState, T extends Service<S>> extends Dao<T> {

    List<T> getAll(int clientId);
    List<S> getHistory(int id);
    void update(int id, S s);
    void add(int clientId, S s);
    void suspend(int id);
    void activate(int id);
}
