package ru.internetprovider.model.dao;

public interface Dao<T> {

    T get(int id);
    void delete(int id);


}
