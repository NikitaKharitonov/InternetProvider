package ru.internetprovider.model;

public interface Dao<T> {

    T get(int id);
    void delete(int id);


}
