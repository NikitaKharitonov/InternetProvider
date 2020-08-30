package ru.internetprovider.model.dao.implementation.hibernate;

import ru.internetprovider.model.dao.*;

/**
 * The implementation of the Abstract Factory pattern.
 * Provides methods for creation of Hibernate DAO objects.
 */
public class HibernateDaoFactory implements DaoFactory {
    @Override
    public ClientDao createClientDao() {
        return new HibernateClientDao();
    }

    @Override
    public InternetDao createInternetDao() {
        return new HibernateInternetDao();
    }

    @Override
    public PhoneDao createPhoneDao() {
        return new HibernatePhoneDao();
    }

    @Override
    public TelevisionDao createTelevisionDao() {
        return new HibernateTelevisionDao();
    }
}
