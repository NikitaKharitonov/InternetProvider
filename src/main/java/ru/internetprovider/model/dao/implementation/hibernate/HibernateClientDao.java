package ru.internetprovider.model.dao.implementation.hibernate;

import org.hibernate.Session;
import org.hibernate.query.Query;
import ru.internetprovider.model.Client;
import ru.internetprovider.model.dao.ClientDao;
import ru.internetprovider.model.dao.Dao;

import javax.persistence.EntityTransaction;
import java.util.List;

public class HibernateClientDao implements ClientDao {

    @Override
    public Client get(int id) {
        EntityTransaction entityTransaction = null;
        Client client = null;
        try (Session session = HibernateUtil.openSession()) {
            entityTransaction = session.beginTransaction();

            Query query = session.createQuery("from Client where id = :id");
            query.setParameter("id", id);
            client = (Client) query.getSingleResult();

            entityTransaction.commit();
        } catch (Exception e) {
            if (entityTransaction != null)
                entityTransaction.rollback();
            e.printStackTrace();
        }
        return client;
    }

    @Override
    public List<Client> getAll() {
        List<Client> clientList = null;
        EntityTransaction entityTransaction = null;
        try (Session session = HibernateUtil.openSession()) {
            entityTransaction = session.getTransaction();
            entityTransaction.begin();

            Query query = session.createQuery("from Client");
            clientList = query.list();

            entityTransaction.commit();
        } catch (Exception e) {
            if (entityTransaction != null)
                entityTransaction.rollback();
            e.printStackTrace();
        }
        return clientList;
    }

    @Override
    public void update(int id, Client client) {
        EntityTransaction entityTransaction = null;
        try (Session session = HibernateUtil.openSession()) {
            entityTransaction = session.getTransaction();
            entityTransaction.begin();

            Query query = session.createQuery("from Client where id = :id");
            query.setParameter("id", id);
            Client currClient = (Client) query.getSingleResult();
            currClient.setName(client.getName());
            currClient.setPhoneNumber(client.getPhoneNumber());
            currClient.setEmailAddress(client.getEmailAddress());

            entityTransaction.commit();
        } catch (Exception e) {
            if (entityTransaction != null)
                entityTransaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void add(Client client) {
        EntityTransaction entityTransaction = null;
        try (Session session = HibernateUtil.openSession()) {
            entityTransaction = session.getTransaction();
            entityTransaction.begin();

            session.persist(client);

            entityTransaction.commit();
        } catch (Exception e) {
            if (entityTransaction != null)
                entityTransaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        EntityTransaction entityTransaction = null;
        try (Session session = HibernateUtil.openSession()) {
            entityTransaction = session.getTransaction();
            entityTransaction.begin();

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

            entityTransaction.commit();
        } catch (Exception e) {
            if (entityTransaction != null)
                entityTransaction.rollback();
            e.printStackTrace();
        }
    }


}
