package ru.internetprovider.model.dao.implementation.hibernate;

import org.hibernate.Session;
import org.hibernate.query.Query;
import ru.internetprovider.model.dao.InternetDao;
import ru.internetprovider.model.services.Internet;
import ru.internetprovider.model.services.InternetState;
import ru.internetprovider.model.services.Status;

import java.util.*;

/**
 * The implementation of the Data Access Object pattern for Internet services
 * using the Hibernate technology.
 */
public class HibernateInternetDao implements InternetDao {

    SessionProvider sessionProvider = new SessionProvider();

    @Override
    public List<InternetState> getHistory(int id) {
//        List<InternetState> history = null;
//        EntityTransaction entityTransaction = null;
//        try (Session session = HibernateUtil.openSession()) {
//            entityTransaction = session.getTransaction();
//            entityTransaction.begin();

        Session session = sessionProvider.openSessionWithTransaction();

        Query query = session.createQuery("from InternetState where internetId = :id order by beginDate");
        query.setParameter("id", id);
        List<InternetState> history = query.getResultList();
        history.sort(Comparator.comparing(InternetState::getBeginDate));

        sessionProvider.closeSessionWithTransaction();

//            entityTransaction.commit();
//        } catch (Exception e) {
//            if (entityTransaction != null)
//                entityTransaction.rollback();
//            e.printStackTrace();
//        }
        return history;
    }

    @Override
    public Internet get(int id) {
//        Internet internet = null;
//        EntityTransaction entityTransaction = null;
//        try (Session session = HibernateUtil.openSession()) {
//            entityTransaction = session.getTransaction();
//            entityTransaction.begin();

        Session session = sessionProvider.openSessionWithTransaction();

//        Query query = session.createQuery("from Internet where id = :id");
//        query.setParameter("id", id);
//        Internet internet = (Internet) query.getSingleResult();
        Internet internet = session.get(Internet.class, id);

        sessionProvider.closeSessionWithTransaction();

//            entityTransaction.commit();
//        } catch (Exception e) {
//            if (entityTransaction != null)
//                entityTransaction.rollback();
//            e.printStackTrace();
//        }
        return internet;
    }

    @Override
    public List<Internet> getAll(int clientId) {
//        List<Internet> internetList = null;
//        EntityTransaction entityTransaction = null;
//        try (Session session = HibernateUtil.openSession()) {
//            entityTransaction = session.getTransaction();
//            entityTransaction.begin();

        Session session = sessionProvider.openSessionWithTransaction();

//        Query query = session.createQuery("from Internet where clientId = :clientId");
//        query.setParameter("clientId", clientId);
//        internetList = query.list();

        List<Internet> internetList = (List<Internet>) session.createQuery("from Internet").list();

        sessionProvider.closeSessionWithTransaction();

//            entityTransaction.commit();
//        } catch (Exception e) {
//            if (entityTransaction != null)
//                entityTransaction.rollback();
//            e.printStackTrace();
//        }
        return internetList;
    }

    @Override
    public void update(int id, InternetState internetState) {
//        EntityTransaction entityTransaction = null;
//        try (Session session = HibernateUtil.openSession()) {
//            entityTransaction = session.getTransaction();
//            entityTransaction.begin();

        Session session = sessionProvider.openSessionWithTransaction();

        Internet internet = session.get(Internet.class, id);
        if (internet.getStatus().equals(Status.ACTIVE)) {
            Query query = session.createQuery("from InternetState where internetId = :id order by beginDate desc");
            query.setParameter("id", id);
            InternetState lastInternetState = (InternetState) query.list().get(0);
            lastInternetState.setEndDate(new Date());
        } else if (internet.getStatus().equals(Status.SUSPENDED)) {
            internet.setStatus(Status.ACTIVE);
        }

        internetState.setInternetId(id);
        session.persist(internetState);

        sessionProvider.closeSessionWithTransaction();

//            entityTransaction.commit();
//        } catch (Exception e) {
//            if (entityTransaction != null)
//                entityTransaction.rollback();
//            e.printStackTrace();
//        }
    }

    @Override
    public void add(int clientId, InternetState internetState) {
//        EntityTransaction entityTransaction = null;
//        try (Session session = HibernateUtil.openSession()) {
//            entityTransaction = session.getTransaction();
//            entityTransaction.begin();

        Session session = sessionProvider.openSessionWithTransaction();

        Internet internet = new Internet(internetState.getBeginDate(), Status.ACTIVE);
        internet.setClientId(clientId);
        session.save(internet);
        internetState.setInternetId(internet.getId());
        session.save(internetState);

        sessionProvider.closeSessionWithTransaction();
//            entityTransaction.commit();
//        } catch (Exception e) {
//            if (entityTransaction != null)
//                entityTransaction.rollback();
//            e.printStackTrace();
//        }
    }

    @Override
    public void suspend(int id) {
//        EntityTransaction entityTransaction = null;
//        try (Session session = HibernateUtil.openSession()) {
//            entityTransaction = session.getTransaction();
//            entityTransaction.begin();

        Session session = sessionProvider.openSessionWithTransaction();

        Query query = session.createQuery("from InternetState where internetId = :id order by beginDate desc");
        query.setParameter("id", id);
        InternetState lastInternetState = (InternetState) query.list().get(0);
        lastInternetState.setEndDate(new Date());

        Internet internet = session.get(Internet.class, id);
        internet.setStatus(Status.SUSPENDED);

        sessionProvider.closeSessionWithTransaction();

//            entityTransaction.commit();
//        } catch (Exception e) {
//            if (entityTransaction != null)
//                entityTransaction.rollback();
//            e.printStackTrace();
//        }
    }

    @Override
    public void activate(int id) {
//        EntityTransaction entityTransaction = null;
//        try (Session session = HibernateUtil.openSession()) {
//            entityTransaction = session.getTransaction();
//            entityTransaction.begin();

        Session session = sessionProvider.openSessionWithTransaction();

        Internet internet = session.get(Internet.class, id);
        internet.setStatus(Status.ACTIVE);

        Query query = session.createQuery("from InternetState where internetId = :id order by beginDate desc");
        query.setParameter("id", id);
        InternetState lastInternetState = (InternetState) query.list().get(0);

        InternetState internetState = new InternetState();
        internetState.setInternetId(lastInternetState.getInternetId());
        internetState.setAntivirus(lastInternetState.isAntivirus());
        internetState.setConnectionType(lastInternetState.getConnectionType());
        internetState.setSpeed(lastInternetState.getSpeed());
        internetState.setBeginDate(new Date());

        session.save(internetState);

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

        Internet internet = session.get(Internet.class, id);
        if (internet.getStatus().equals(Status.ACTIVE)) {
            Query query = session.createQuery("from InternetState where internetId = :id order by beginDate desc");
            query.setParameter("id", id);
            InternetState lastInternetState = (InternetState) query.list().get(0);
            lastInternetState.setEndDate(new Date());
        }
        internet.setStatus(Status.DELETED);

        sessionProvider.closeSessionWithTransaction();

//            entityTransaction.commit();
//        } catch (Exception e) {
//            if (entityTransaction != null)
//                entityTransaction.rollback();
//            e.printStackTrace();
//        }
    }
}
