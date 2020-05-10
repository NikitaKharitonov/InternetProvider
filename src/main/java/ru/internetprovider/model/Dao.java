package ru.internetprovider.model;

public interface Dao<T> {

    String URL = "jdbc:postgresql://localhost:5432/provider_db";
    String LOGIN = "postgres";
    String PASSWORD = "root";

    T get(long id);
    void delete(long id);


}
