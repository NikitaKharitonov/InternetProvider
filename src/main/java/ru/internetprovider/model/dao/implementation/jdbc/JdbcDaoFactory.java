package ru.internetprovider.model.dao.implementation.jdbc;

import ru.internetprovider.model.dao.*;

/**
 * The implementation of the Abstract Factory pattern.
 * Provides methods for creation of JDBC DAO objects.
 */
public class JdbcDaoFactory implements DaoFactory {
    @Override
    public ru.internetprovider.model.dao.ClientDao createClientDao() {
        return new JdbcClientDao();
    }

    @Override
    public ru.internetprovider.model.dao.InternetDao createInternetDao() {
        return new JdbcInternetDao();
    }

    @Override
    public ru.internetprovider.model.dao.PhoneDao createPhoneDao() {
        return new JdbcPhoneDao();
    }

    @Override
    public ru.internetprovider.model.dao.TelevisionDao createTelevisionDao() {
        return new JdbcTelevisionDao();
    }
}
