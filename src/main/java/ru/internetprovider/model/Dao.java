package ru.internetprovider.model;

public interface Dao<T> {

    T get(long id);
    void delete(long id);


}
