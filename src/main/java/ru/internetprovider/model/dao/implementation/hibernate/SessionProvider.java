package ru.internetprovider.model.dao.implementation.hibernate;

import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Provides a Session for Hibernate DAO objects.
 */
public class SessionProvider {

    private static final String PERSISTENCE_UNIT_NAME = "InternetProvider";
    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY;
    private Session session;
    private Transaction transaction;

    static {
        ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    }

    public Session openSessionWithTransaction() {
        session = (Session) ENTITY_MANAGER_FACTORY.createEntityManager().getDelegate();
        transaction = session.beginTransaction();
        return session;
    }

    public Session openSession() {
        return (Session) ENTITY_MANAGER_FACTORY.createEntityManager().getDelegate();
    }

    public void closeSessionWithTransaction() {
        transaction.commit();
        session.close();
    }

    public void closeSession() {
        session.close();
    }

    public Session getSession() {
        return session;
    }

}
