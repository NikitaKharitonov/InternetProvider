package ru.internetprovider.model.dao.implementation.hibernate;

import org.hibernate.Session;
import org.hibernate.query.Query;
import ru.internetprovider.model.dao.TelevisionDao;
import ru.internetprovider.model.services.Television;
import ru.internetprovider.model.services.Status;
import ru.internetprovider.model.services.TelevisionState;

import javax.persistence.EntityTransaction;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * The implementation of the Data Access Object pattern for Television services
 * using the Hibernate technology.
 */
public class HibernateTelevisionDao implements TelevisionDao {

    @Override
    public List<Television> getAll(int clientId) {
        List<Television> televisionClientServiceList = null;
        EntityTransaction entityTransaction = null;
        try (Session session = HibernateUtil.openSession()) {
            entityTransaction = session.getTransaction();
            entityTransaction.begin();

            Query query = session.createQuery("from Television where clientId = :clientId");
            query.setParameter("clientId", clientId);
            televisionClientServiceList = query.list();

            entityTransaction.commit();
        } catch (Exception e) {
            if (entityTransaction != null)
                entityTransaction.rollback();
            e.printStackTrace();
        }
        return televisionClientServiceList;
    }

    @Override
    public List<TelevisionState> getHistory(int id) {
        List<TelevisionState> history = null;
        EntityTransaction entityTransaction = null;
        try (Session session = HibernateUtil.openSession()) {
            entityTransaction = session.getTransaction();
            entityTransaction.begin();

            Query query = session.createQuery("from TelevisionState where televisionId = :id order by beginDate");
            query.setParameter("id", id);
            history = query.getResultList();
            history.sort(Comparator.comparing(TelevisionState::getBeginDate));

            entityTransaction.commit();
        } catch (Exception e) {
            if (entityTransaction != null)
                entityTransaction.rollback();
            e.printStackTrace();
        }
        return history;
    }

    @Override
    public void update(int id, TelevisionState televisionState) {
        EntityTransaction entityTransaction = null;
        try (Session session = HibernateUtil.openSession()) {
            entityTransaction = session.getTransaction();
            entityTransaction.begin();

            Television television = session.get(Television.class, id);
            if (television.getStatus().equals(Status.ACTIVE)) {
                Query query = session.createQuery("from TelevisionState where televisionId = :id order by beginDate desc");
                query.setParameter("id", id);
                TelevisionState lastTelevisionState = (TelevisionState) query.list().get(0);
                lastTelevisionState.setEndDate(new Date());
            } else if (television.getStatus().equals(Status.SUSPENDED)) {
                television.setStatus(Status.ACTIVE);
            }

            televisionState.setTelevisionId(id);
            session.persist(televisionState);

            entityTransaction.commit();
        } catch (Exception e) {
            if (entityTransaction != null)
                entityTransaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void add(int clientId, TelevisionState televisionState) {
        EntityTransaction entityTransaction = null;
        try (Session session = HibernateUtil.openSession()) {
            entityTransaction = session.getTransaction();
            entityTransaction.begin();

            Television clientService = new Television(televisionState.getBeginDate(), Status.ACTIVE);
            clientService.setClientId(clientId);
            session.save(clientService);

            televisionState.setTelevisionId(clientService.getId());
            session.save(televisionState);

            entityTransaction.commit();
        } catch (Exception e) {
            if (entityTransaction != null)
                entityTransaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public Television get(int id) {
        Television television = null;
        EntityTransaction entityTransaction = null;
        try (Session session = HibernateUtil.openSession()) {
            entityTransaction = session.getTransaction();
            entityTransaction.begin();

            Query query = session.createQuery("from Television where id = :id");
            query.setParameter("id", id);
            television = (Television) query.getSingleResult();

            entityTransaction.commit();
        } catch (Exception e) {
            if (entityTransaction != null)
                entityTransaction.rollback();
            e.printStackTrace();
        }
        return television;
    }

    @Override
    public void suspend(int id) {
        EntityTransaction entityTransaction = null;
        try (Session session = HibernateUtil.openSession()) {
            entityTransaction = session.getTransaction();
            entityTransaction.begin();

            Query query = session.createQuery("from TelevisionState where televisionId = :id order by beginDate desc");
            query.setParameter("id", id);
            TelevisionState lastTelevisionState = (TelevisionState) query.list().get(0);
            lastTelevisionState.setEndDate(new Date());

            Television television = session.get(Television.class, id);
            television.setStatus(Status.SUSPENDED);

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

            Television television = session.get(Television.class, id);
            television.setStatus(Status.ACTIVE);

            Query query = session.createQuery("from TelevisionState where televisionId = :id order by beginDate desc");
            query.setParameter("id", id);
            TelevisionState lastTelevisionState = (TelevisionState) query.list().get(0);

            TelevisionState televisionState = new TelevisionState();
            televisionState.setTelevisionId(lastTelevisionState.getTelevisionId());
            televisionState.setChannelsCount(lastTelevisionState.getChannelsCount());
            televisionState.setBeginDate(new Date());

            session.save(televisionState);

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

            Television television = session.get(Television.class, id);

            if (television.getStatus().equals(Status.ACTIVE)) {
                Query query = session.createQuery("from TelevisionState where televisionId = :id order by beginDate desc");
                query.setParameter("id", id);
                TelevisionState lastTelevisionState = (TelevisionState) query.list().get(0);
                lastTelevisionState.setEndDate(new Date());
            }

            television.setStatus(Status.DELETED);

            entityTransaction.commit();
        } catch (Exception e) {
            if (entityTransaction != null)
                entityTransaction.rollback();
            e.printStackTrace();
        }
    }

}
