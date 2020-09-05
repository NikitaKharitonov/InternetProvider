package ru.internetprovider.model.dao.implementation.hibernate;

import org.hibernate.Session;
import org.hibernate.query.Query;
import ru.internetprovider.model.Client;
import ru.internetprovider.model.dao.ClientDao;

import java.util.List;

/**
 * The implementation of the Data Access Object pattern for clients
 * using the Hibernate technology.
 */
public class HibernateClientDao implements ClientDao {

    SessionProvider sessionProvider = new SessionProvider();

    @Override
    public Client get(int id) {
//        EntityTransaction entityTransaction = null;
//        Client client;
//        try (Session session = HibernateUtil.openSession()) {
//            entityTransaction = session.beginTransaction();

        Session session = sessionProvider.openSessionWithTransaction();

        Query query = session.createQuery("from Client where id = :id");
        query.setParameter("id", id);
        Client client = (Client) query.getSingleResult();

        sessionProvider.closeSessionWithTransaction();

//            entityTransaction.commit();
//        } catch (Exception e) {
//            if (entityTransaction != null)
//                entityTransaction.rollback();
//            e.printStackTrace();
//        }
        return client;
    }

    @Override
    public List<Client> getAll() {
//        List<Client> clientList;
//        EntityTransaction entityTransaction = null;
//        try (Session session = HibernateUtil.openSession()) {
//            entityTransaction = session.getTransaction();
//            entityTransaction.begin();

        Session session = sessionProvider.openSessionWithTransaction();

        Query query = session.createQuery("from Client");
        List<Client> clientList = query.list();

        sessionProvider.closeSessionWithTransaction();

//            entityTransaction.commit();
//        } catch (Exception e) {
//            if (entityTransaction != null)
//                entityTransaction.rollback();
//            e.printStackTrace();
//        }
        return clientList;
    }

    @Override
    public void update(int id, Client client) {
//        EntityTransaction entityTransaction = null;
//        try (Session session = HibernateUtil.openSession()) {
//            entityTransaction = session.getTransaction();
//            entityTransaction.begin();

        Session session = sessionProvider.openSessionWithTransaction();

        Query query = session.createQuery("from Client where id = :id");
        query.setParameter("id", id);
        Client currClient = (Client) query.getSingleResult();
        currClient.setName(client.getName());
        currClient.setPhoneNumber(client.getPhoneNumber());
        currClient.setEmailAddress(client.getEmailAddress());

        sessionProvider.closeSessionWithTransaction();

//            entityTransaction.commit();
//        } catch (Exception e) {
//            if (entityTransaction != null)
//                entityTransaction.rollback();
//            e.printStackTrace();
//        }
    }

    @Override
    public void add(Client client) {
//        EntityTransaction entityTransaction = null;
//        try (Session session = HibernateUtil.openSession()) {
//            entityTransaction = session.getTransaction();
//            entityTransaction.begin();

        Session session = sessionProvider.openSessionWithTransaction();

        session.persist(client);

        sessionProvider.closeSessionWithTransaction();

//            entityTransaction.commit();
//        } catch (Exception e) {
//            if (entityTransaction != null)
//                entityTransaction.rollback();
//            e.printStackTrace();
//        }
    }

    @Override
    public void delete(int id) {
//        EntityTransaction entityTransaction = null;
//        try (Session session = HibernateUtil.openSession()) {
//            entityTransaction = session.getTransaction();
//            entityTransaction.begin();

        Session session = sessionProvider.openSessionWithTransaction();

        session.createQuery("delete from Internet where clientId = :id")
                    .setParameter("id", id)
                    .executeUpdate();
        session.createQuery("delete from Phone where clientId = :id")
                .setParameter("id", id)
                .executeUpdate();
        session.createQuery("delete from Television where clientId = :id")
                .setParameter("id", id)
                .executeUpdate();
        session.createQuery("delete from Client where id = :id")
                .setParameter("id", id)
                .executeUpdate();

        sessionProvider.closeSessionWithTransaction();

//            entityTransaction.commit();
//        } catch (Exception e) {
//            if (entityTransaction != null)
//                entityTransaction.rollback();
//            e.printStackTrace();
//        }
    }


}
