package ru.internetprovider.model.dao.implementation.hibernate;

import org.hibernate.Session;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Provides an EntityManagerFactory for Hibernate DAO objects.
 */
public class HibernateUtil {

    private static final EntityManagerFactory entityManagerFactory =
            Persistence.createEntityManagerFactory("InternetProvider");

    public static Session openSession() {
        return (Session) entityManagerFactory.createEntityManager().getDelegate();
    }

}
