package ru.internetprovider.controller;

import ru.internetprovider.model.dao.*;
import ru.internetprovider.model.dao.implementation.hibernate.HibernateDaoFactory;
import ru.internetprovider.model.dao.implementation.jdbc.JdbcDaoFactory;

public class DataAccess {

    private static final DaoFactory JDBC_DAO_FACTORY = new HibernateDaoFactory();

    private static final ClientDao CLIENT_DAO = JDBC_DAO_FACTORY.createClientDao();

    private static final InternetDao INTERNET_DAO = JDBC_DAO_FACTORY.createInternetDao();

    private static final PhoneDao PHONE_DAO = JDBC_DAO_FACTORY.createPhoneDao();

    private static final TelevisionDao TELEVISION_DAO = JDBC_DAO_FACTORY.createTelevisionDao();

    public static ClientDao getClientDao() {
        return CLIENT_DAO;
    }

    public static InternetDao getInternetDao() {
        return INTERNET_DAO;
    }

    public static PhoneDao getPhoneDao() {
        return PHONE_DAO;
    }

    public static TelevisionDao getTelevisionDao() {
        return TELEVISION_DAO;
    }
}
