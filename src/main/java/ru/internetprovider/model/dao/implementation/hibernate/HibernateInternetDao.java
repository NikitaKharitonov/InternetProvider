package ru.internetprovider.model.dao.implementation.hibernate;

import org.hibernate.Session;
import org.hibernate.query.Query;
import ru.internetprovider.model.dao.InternetDao;
import ru.internetprovider.model.services.Internet;
import ru.internetprovider.model.services.InternetSpecification;
import ru.internetprovider.model.services.Status;

import javax.persistence.EntityTransaction;
import java.util.*;

/**
 * The implementation of the Data Access Object pattern for Internet services
 * using the Hibernate technology.
 */
public class HibernateInternetDao implements InternetDao {

    @Override
    public List<InternetSpecification> getHistory(int id) {
        List<InternetSpecification> history = null;
        EntityTransaction entityTransaction = null;
        try (Session session = HibernateUtil.openSession()) {
            entityTransaction = session.getTransaction();
            entityTransaction.begin();

            Query query = session.createQuery("from InternetSpecification where internetId = :id order by beginDate");
            query.setParameter("id", id);
            history = query.getResultList();
            history.sort(Comparator.comparing(InternetSpecification::getBeginDate));

            entityTransaction.commit();
        } catch (Exception e) {
            if (entityTransaction != null)
                entityTransaction.rollback();
            e.printStackTrace();
        }
        return history;
    }

    @Override
    public Internet get(int id) {
        Internet internet = null;
        EntityTransaction entityTransaction = null;
        try (Session session = HibernateUtil.openSession()) {
            entityTransaction = session.getTransaction();
            entityTransaction.begin();

            Query query = session.createQuery("from Internet where id = :id");
            query.setParameter("id", id);
            internet = (Internet) query.getSingleResult();

            entityTransaction.commit();
        } catch (Exception e) {
            if (entityTransaction != null)
                entityTransaction.rollback();
            e.printStackTrace();
        }
        return internet;
    }

    @Override
    public List<Internet> getAll(int clientId) {
        List<Internet> internetList = null;
        EntityTransaction entityTransaction = null;
        try (Session session = HibernateUtil.openSession()) {
            entityTransaction = session.getTransaction();
            entityTransaction.begin();

            Query query = session.createQuery("from Internet where clientId = :clientId");
            query.setParameter("clientId", clientId);
            internetList = query.list();

            entityTransaction.commit();
        } catch (Exception e) {
            if (entityTransaction != null)
                entityTransaction.rollback();
            e.printStackTrace();
        }
        return internetList;
    }

    @Override
    public void update(int id, InternetSpecification internetSpecification) {
        EntityTransaction entityTransaction = null;
        try (Session session = HibernateUtil.openSession()) {
            entityTransaction = session.getTransaction();
            entityTransaction.begin();

            Internet internet = session.get(Internet.class, id);
            if (internet.getStatus().equals(Status.ACTIVE)) {
                Query query = session.createQuery("from InternetSpecification where internetId = :id order by beginDate desc");
                query.setParameter("id", id);
                InternetSpecification lastInternetSpecification = (InternetSpecification) query.list().get(0);
                lastInternetSpecification.setEndDate(new Date());
            } else if (internet.getStatus().equals(Status.SUSPENDED)) {
                internet.setStatus(Status.ACTIVE);
            }

            internetSpecification.setInternetId(id);
            session.persist(internetSpecification);

            entityTransaction.commit();
        } catch (Exception e) {
            if (entityTransaction != null)
                entityTransaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void add(int clientId, InternetSpecification internetSpecification) {
        EntityTransaction entityTransaction = null;
        try (Session session = HibernateUtil.openSession()) {
            entityTransaction = session.getTransaction();
            entityTransaction.begin();

            Internet clientService = new Internet(internetSpecification.getBeginDate(), Status.ACTIVE);
            clientService.setClientId(clientId);
            session.save(clientService);

            internetSpecification.setInternetId(clientService.getId());
            session.save(internetSpecification);

            entityTransaction.commit();
        } catch (Exception e) {
            if (entityTransaction != null)
                entityTransaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void suspend(int id) {
        EntityTransaction entityTransaction = null;
        try (Session session = HibernateUtil.openSession()) {
            entityTransaction = session.getTransaction();
            entityTransaction.begin();

            Query query = session.createQuery("from InternetSpecification where internetId = :id order by beginDate desc");
            query.setParameter("id", id);
            InternetSpecification lastInternetSpecification = (InternetSpecification) query.list().get(0);
            lastInternetSpecification.setEndDate(new Date());

            Internet internet = session.get(Internet.class, id);
            internet.setStatus(Status.SUSPENDED);

            entityTransaction.commit();
        } catch (Exception e) {
            if (entityTransaction != null)
                entityTransaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void activate(int id) {
        EntityTransaction entityTransaction = null;
        try (Session session = HibernateUtil.openSession()) {
            entityTransaction = session.getTransaction();
            entityTransaction.begin();

            Internet internet = session.get(Internet.class, id);
            internet.setStatus(Status.ACTIVE);

            Query query = session.createQuery("from InternetSpecification where internetId = :id order by beginDate desc");
            query.setParameter("id", id);
            InternetSpecification lastInternetSpecification = (InternetSpecification) query.list().get(0);

            InternetSpecification internetSpecification = new InternetSpecification();
            internetSpecification.setInternetId(lastInternetSpecification.getInternetId());
            internetSpecification.setAntivirus(lastInternetSpecification.isAntivirus());
            internetSpecification.setConnectionType(lastInternetSpecification.getConnectionType());
            internetSpecification.setSpeed(lastInternetSpecification.getSpeed());
            internetSpecification.setBeginDate(new Date());

            session.save(internetSpecification);

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

            Internet internet = session.get(Internet.class, id);

            if (internet.getStatus().equals(Status.ACTIVE)) {
                Query query = session.createQuery("from InternetSpecification where internetId = :id order by beginDate desc");
                query.setParameter("id", id);
                InternetSpecification lastInternetSpecification = (InternetSpecification) query.list().get(0);
                lastInternetSpecification.setEndDate(new Date());
            }

            internet.setStatus(Status.DELETED);

            entityTransaction.commit();
        } catch (Exception e) {
            if (entityTransaction != null)
                entityTransaction.rollback();
            e.printStackTrace();
        }
    }
}
