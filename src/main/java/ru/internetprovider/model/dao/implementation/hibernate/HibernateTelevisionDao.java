package ru.internetprovider.model.dao.implementation.hibernate;

import org.hibernate.Session;
import org.hibernate.query.Query;
import ru.internetprovider.model.dao.TelevisionDao;
import ru.internetprovider.model.services.Television;
import ru.internetprovider.model.services.Status;
import ru.internetprovider.model.services.TelevisionSpecification;

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
    public List<TelevisionSpecification> getHistory(int id) {
        List<TelevisionSpecification> history = null;
        EntityTransaction entityTransaction = null;
        try (Session session = HibernateUtil.openSession()) {
            entityTransaction = session.getTransaction();
            entityTransaction.begin();

            Query query = session.createQuery("from TelevisionSpecification where televisionId = :id order by beginDate");
            query.setParameter("id", id);
            history = query.getResultList();
            history.sort(Comparator.comparing(TelevisionSpecification::getBeginDate));

            entityTransaction.commit();
        } catch (Exception e) {
            if (entityTransaction != null)
                entityTransaction.rollback();
            e.printStackTrace();
        }
        return history;
    }

    @Override
    public void update(int id, TelevisionSpecification temporalTelevision) {
        EntityTransaction entityTransaction = null;
        try (Session session = HibernateUtil.openSession()) {
            entityTransaction = session.getTransaction();
            entityTransaction.begin();

            Television television = session.get(Television.class, id);
            if (television.getStatus().equals(Status.ACTIVE)) {
                Query query = session.createQuery("from TelevisionSpecification where televisionId = :id order by beginDate desc");
                query.setParameter("id", id);
                TelevisionSpecification lastTemporalTelevision = (TelevisionSpecification) query.list().get(0);
                lastTemporalTelevision.setEndDate(new Date());
            } else if (television.getStatus().equals(Status.SUSPENDED)) {
                television.setStatus(Status.ACTIVE);
            }

            temporalTelevision.setTelevisionId(id);
            session.persist(temporalTelevision);

            entityTransaction.commit();
        } catch (Exception e) {
            if (entityTransaction != null)
                entityTransaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void add(int clientId, TelevisionSpecification temporalTelevision) {
        EntityTransaction entityTransaction = null;
        try (Session session = HibernateUtil.openSession()) {
            entityTransaction = session.getTransaction();
            entityTransaction.begin();

            Television clientService = new Television(temporalTelevision.getBeginDate(), Status.ACTIVE);
            clientService.setClientId(clientId);
            session.save(clientService);

            temporalTelevision.setTelevisionId(clientService.getId());
            session.save(temporalTelevision);

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

            Query query = session.createQuery("from TelevisionSpecification where televisionId = :id order by beginDate desc");
            query.setParameter("id", id);
            TelevisionSpecification lastTemporalTelevision = (TelevisionSpecification) query.list().get(0);
            lastTemporalTelevision.setEndDate(new Date());

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

            Query query = session.createQuery("from TelevisionSpecification where televisionId = :id order by beginDate desc");
            query.setParameter("id", id);
            TelevisionSpecification lastTemporalTelevision = (TelevisionSpecification) query.list().get(0);

            // fixme kostyl'
            TelevisionSpecification temporalTelevision = new TelevisionSpecification();
            temporalTelevision.setTelevisionId(lastTemporalTelevision.getTelevisionId());
            temporalTelevision.setChannelsCount(lastTemporalTelevision.getChannelsCount());
            temporalTelevision.setBeginDate(new Date());

            session.save(temporalTelevision);

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
                Query query = session.createQuery("from TelevisionSpecification where televisionId = :id order by beginDate desc");
                query.setParameter("id", id);
                TelevisionSpecification lastTemporalTelevision = (TelevisionSpecification) query.list().get(0);
                lastTemporalTelevision.setEndDate(new Date());
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
