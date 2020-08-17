package ru.internetprovider.controller;

import ru.internetprovider.model.dao.implementation.jdbc.*;

public class DaoUtil {
    private static final ClientDao clientDao = new ClientDao();
    private static final InternetDao internetDao = new InternetDao();
    private static final PhoneDao phoneDao = new PhoneDao();
    private static final TelevisionDao televisionDao = new TelevisionDao();

    public static ClientDao getClientDao() {
        return clientDao;
    }

    public static InternetDao getInternetDao() {
        return internetDao;
    }

    public static PhoneDao getPhoneDao() {
        return phoneDao;
    }

    public static TelevisionDao getTelevisionDao() {
        return televisionDao;
    }
}
