package ru.internetprovider.model.dao;

public interface Dao<T> {

    T get(int id);
    void delete(int id);
//    void update(int id, T t);

}
