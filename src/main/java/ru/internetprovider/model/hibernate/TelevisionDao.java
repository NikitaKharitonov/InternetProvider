package ru.internetprovider.model.hibernate;

import org.hibernate.Session;
import org.hibernate.query.Query;
import ru.internetprovider.model.ServiceDao;
import ru.internetprovider.model.services.ClientService;
import ru.internetprovider.model.services.ClientTelevision;
import ru.internetprovider.model.services.Status;
import ru.internetprovider.model.services.Television;

import javax.persistence.EntityTransaction;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class TelevisionDao implements ServiceDao<Television> {

    @Override
    public List<ClientService> getAll(int clientId) {
        List<ClientService> televisionClientServiceList = null;
        EntityTransaction entityTransaction = null;
        try (Session session = HibernateUtil.openSession()) {
            entityTransaction = session.getTransaction();
            entityTransaction.begin();

            Query query = session.createQuery("from ClientTelevision where clientId = :clientId");
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
    public List<Television> getHistory(int id) {
        List<Television> history = null;
        EntityTransaction entityTransaction = null;
        try (Session session = HibernateUtil.openSession()) {
            entityTransaction = session.getTransaction();
            entityTransaction.begin();

            Query query = session.createQuery("from ClientTelevision where id = :id");
            query.setParameter("id", id);
            ClientTelevision clientTelevision = (ClientTelevision) query.getSingleResult();
            history = clientTelevision.getHistory();
            history.sort(Comparator.comparing(Television::getBeginDate));

            entityTransaction.commit();
        } catch (Exception e) {
            if (entityTransaction != null)
                entityTransaction.rollback();
            e.printStackTrace();
        }
        return history;
    }

    @Override
    public void update(int id, Television television) {
        EntityTransaction entityTransaction = null;
        try (Session session = HibernateUtil.openSession()) {
            entityTransaction = session.getTransaction();
            entityTransaction.begin();

            ClientTelevision clientTelevision = session.get(ClientTelevision.class, id);
            if (clientTelevision.getStatus().equals(Status.ACTIVE)) {
                Query query = session.createQuery("from Television where televisionId = :id order by beginDate desc");
                query.setParameter("id", id);
                Television lastTelevision = (Television) query.list().get(0);
                lastTelevision.setEndDate(new Date());
            } else if (clientTelevision.getStatus().equals(Status.SUSPENDED)) {
                clientTelevision.setStatus(Status.ACTIVE);
            }

            television.setTelevisionId(id);
            session.persist(television);

            entityTransaction.commit();
        } catch (Exception e) {
            if (entityTransaction != null)
                entityTransaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void save(int clientId, Television television) {
        EntityTransaction entityTransaction = null;
        try (Session session = HibernateUtil.openSession()) {
            entityTransaction = session.getTransaction();
            entityTransaction.begin();

            ClientTelevision clientService = new ClientTelevision(television.getBeginDate(), Status.ACTIVE);
            clientService.setClientId(clientId);
            session.save(clientService);

            television.setTelevisionId(clientService.getId());
            session.save(television);

            entityTransaction.commit();
        } catch (Exception e) {
            if (entityTransaction != null)
                entityTransaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public ClientService get(int id) {
        ClientService clientService = null;
        EntityTransaction entityTransaction = null;
        try (Session session = HibernateUtil.openSession()) {
            entityTransaction = session.getTransaction();
            entityTransaction.begin();

            Query query = session.createQuery("from ClientTelevision where id = :id");
            query.setParameter("id", id);
            clientService = (ClientService) query.getSingleResult();

            entityTransaction.commit();
        } catch (Exception e) {
            if (entityTransaction != null)
                entityTransaction.rollback();
            e.printStackTrace();
        }
        return clientService;
    }

    @Override
    public void suspend(int id) {
        EntityTransaction entityTransaction = null;
        try (Session session = HibernateUtil.openSession()) {
            entityTransaction = session.getTransaction();
            entityTransaction.begin();

            Query query = session.createQuery("from Television where televisionId = :id order by beginDate desc");
            query.setParameter("id", id);
            Television lastTelevision = (Television) query.list().get(0);
            lastTelevision.setEndDate(new Date());

            ClientTelevision clientTelevision = session.get(ClientTelevision.class, id);
            clientTelevision.setStatus(Status.SUSPENDED);

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

            ClientTelevision clientTelevision = session.get(ClientTelevision.class, id);
            clientTelevision.setStatus(Status.ACTIVE);

            Query query = session.createQuery("from Television where televisionId = :id order by beginDate desc");
            query.setParameter("id", id);
            Television lastTelevision = (Television) query.list().get(0);

            // fixme kostyl'
            Television television = new Television();
            television.setTelevisionId(lastTelevision.getTelevisionId());
            television.setChannelsCount(lastTelevision.getChannelsCount());
            television.setBeginDate(new Date());

            session.save(television);

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

            ClientTelevision clientTelevision = session.get(ClientTelevision.class, id);

            if (clientTelevision.getStatus().equals(Status.ACTIVE)) {
                Query query = session.createQuery("from Television where televisionId = :id order by beginDate desc");
                query.setParameter("id", id);
                Television lastTelevision = (Television) query.list().get(0);
                lastTelevision.setEndDate(new Date());
            }

            clientTelevision.setStatus(Status.DISCONNECTED);

            entityTransaction.commit();
        } catch (Exception e) {
            if (entityTransaction != null)
                entityTransaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        // todo?
    }

}
