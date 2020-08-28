package ru.internetprovider.model.dao.implementation.hibernate;

import org.hibernate.Session;
import org.hibernate.query.Query;
import ru.internetprovider.model.dao.InternetDao;
import ru.internetprovider.model.services.Internet;
import ru.internetprovider.model.services.TemporalInternet;
import ru.internetprovider.model.services.Status;

import javax.persistence.EntityTransaction;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class HibernateInternetDao implements InternetDao {

    @Override
    public List<TemporalInternet> getHistory(int id) {
        List<TemporalInternet> history = null;
        EntityTransaction entityTransaction = null;
        try (Session session = HibernateUtil.openSession()) {
            entityTransaction = session.getTransaction();
            entityTransaction.begin();

            Query query = session.createQuery("from Internet where id = :id");
            query.setParameter("id", id);
            Internet internet = (Internet) query.getSingleResult();
            history = internet.getHistory();
            history.sort(Comparator.comparing(TemporalInternet::getBeginDate));

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
    public void update(int id, TemporalInternet temporalInternet) {
        EntityTransaction entityTransaction = null;
        try (Session session = HibernateUtil.openSession()) {
            entityTransaction = session.getTransaction();
            entityTransaction.begin();

            Internet internet = session.get(Internet.class, id);
            if (internet.getStatus().equals(Status.ACTIVE)) {
                Query query = session.createQuery("from TemporalInternet where internetId = :id order by beginDate desc");
                query.setParameter("id", id);
                TemporalInternet lastTemporalInternet = (TemporalInternet) query.list().get(0);
                lastTemporalInternet.setEndDate(new Date());
            } else if (internet.getStatus().equals(Status.SUSPENDED)) {
                internet.setStatus(Status.ACTIVE);
            }

            temporalInternet.setInternetId(id);
            session.persist(temporalInternet);

            entityTransaction.commit();
        } catch (Exception e) {
            if (entityTransaction != null)
                entityTransaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void add(int clientId, TemporalInternet temporalInternet) {
        EntityTransaction entityTransaction = null;
        try (Session session = HibernateUtil.openSession()) {
            entityTransaction = session.getTransaction();
            entityTransaction.begin();

            Internet clientService = new Internet(temporalInternet.getBeginDate(), Status.ACTIVE);
            clientService.setClientId(clientId);
            session.save(clientService);

            temporalInternet.setInternetId(clientService.getId());
            session.save(temporalInternet);

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

            Query query = session.createQuery("from TemporalInternet where internetId = :id order by beginDate desc");
            query.setParameter("id", id);
            TemporalInternet lastTemporalInternet = (TemporalInternet) query.list().get(0);
            lastTemporalInternet.setEndDate(new Date());

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

            Query query = session.createQuery("from TemporalInternet where internetId = :id order by beginDate desc");
            query.setParameter("id", id);
            TemporalInternet lastTemporalInternet = (TemporalInternet) query.list().get(0);

            TemporalInternet temporalInternet = new TemporalInternet();
            temporalInternet.setInternetId(lastTemporalInternet.getInternetId());
            temporalInternet.setAntivirus(lastTemporalInternet.isAntivirus());
            temporalInternet.setConnectionType(lastTemporalInternet.getConnectionType());
            temporalInternet.setSpeed(lastTemporalInternet.getSpeed());
            temporalInternet.setBeginDate(new Date());

            session.save(temporalInternet);

            entityTransaction.commit();
        } catch (Exception e) {
            if (entityTransaction != null)
                entityTransaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void disconnect(int id) {
        EntityTransaction entityTransaction = null;
        try (Session session = HibernateUtil.openSession()) {
            entityTransaction = session.getTransaction();
            entityTransaction.begin();

            Internet internet = session.get(Internet.class, id);

            if (internet.getStatus().equals(Status.ACTIVE)) {
                Query query = session.createQuery("from TemporalInternet where internetId = :id order by beginDate desc");
                query.setParameter("id", id);
                TemporalInternet lastTemporalInternet = (TemporalInternet) query.list().get(0);
                lastTemporalInternet.setEndDate(new Date());
            }

            internet.setStatus(Status.DISCONNECTED);

            entityTransaction.commit();
        } catch (Exception e) {
            if (entityTransaction != null)
                entityTransaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        //todo?
    }
}
