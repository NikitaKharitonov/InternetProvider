package ru.internetprovider.model.dao.implementation.hibernate;

import org.hibernate.Session;
import org.hibernate.query.Query;
import ru.internetprovider.model.dao.InternetDao;
import ru.internetprovider.model.dao.ServiceDao;
import ru.internetprovider.model.services.ClientInternet;
import ru.internetprovider.model.services.ClientService;
import ru.internetprovider.model.services.Internet;
import ru.internetprovider.model.services.Status;

import javax.persistence.EntityTransaction;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class HibernateInternetDao implements InternetDao {

    @Override
    public List<Internet> getHistory(int id) {
        List<Internet> history = null;
        EntityTransaction entityTransaction = null;
        try (Session session = HibernateUtil.openSession()) {
            entityTransaction = session.getTransaction();
            entityTransaction.begin();

            Query query = session.createQuery("from ClientInternet where id = :id");
            query.setParameter("id", id);
            ClientInternet clientInternet = (ClientInternet) query.getSingleResult();
            history = clientInternet.getHistory();
            history.sort(Comparator.comparing(Internet::getBeginDate));

            entityTransaction.commit();
        } catch (Exception e) {
            if (entityTransaction != null)
                entityTransaction.rollback();
            e.printStackTrace();
        }
        return history;
    }

    @Override
    public ClientInternet get(int id) {
        ClientInternet clientInternet = null;
        EntityTransaction entityTransaction = null;
        try (Session session = HibernateUtil.openSession()) {
            entityTransaction = session.getTransaction();
            entityTransaction.begin();

            Query query = session.createQuery("from ClientInternet where id = :id");
            query.setParameter("id", id);
            clientInternet = (ClientInternet) query.getSingleResult();

            entityTransaction.commit();
        } catch (Exception e) {
            if (entityTransaction != null)
                entityTransaction.rollback();
            e.printStackTrace();
        }
        return clientInternet;
    }

    @Override
    public List<ClientInternet> getAll(int clientId) {
        List<ClientInternet> clientInternetList = null;
        EntityTransaction entityTransaction = null;
        try (Session session = HibernateUtil.openSession()) {
            entityTransaction = session.getTransaction();
            entityTransaction.begin();

            Query query = session.createQuery("from ClientInternet where clientId = :clientId");
            query.setParameter("clientId", clientId);
            clientInternetList = query.list();

            entityTransaction.commit();
        } catch (Exception e) {
            if (entityTransaction != null)
                entityTransaction.rollback();
            e.printStackTrace();
        }
        return clientInternetList;
    }

    @Override
    public void update(int id, Internet internet) {
        EntityTransaction entityTransaction = null;
        try (Session session = HibernateUtil.openSession()) {
            entityTransaction = session.getTransaction();
            entityTransaction.begin();

            ClientInternet clientInternet = session.get(ClientInternet.class, id);
            if (clientInternet.getStatus().equals(Status.ACTIVE)) {
                Query query = session.createQuery("from Internet where internetId = :id order by beginDate desc");
                query.setParameter("id", id);
                Internet lastInternet = (Internet) query.list().get(0);
                lastInternet.setEndDate(new Date());
            } else if (clientInternet.getStatus().equals(Status.SUSPENDED)) {
                clientInternet.setStatus(Status.ACTIVE);
            }

            internet.setInternetId(id);
            session.persist(internet);

            entityTransaction.commit();
        } catch (Exception e) {
            if (entityTransaction != null)
                entityTransaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void add(int clientId, Internet internet) {
        EntityTransaction entityTransaction = null;
        try (Session session = HibernateUtil.openSession()) {
            entityTransaction = session.getTransaction();
            entityTransaction.begin();

            ClientInternet clientService = new ClientInternet(internet.getBeginDate(), Status.ACTIVE);
            clientService.setClientId(clientId);
            session.save(clientService);

            internet.setInternetId(clientService.getId());
            session.save(internet);

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

            Query query = session.createQuery("from Internet where internetId = :id order by beginDate desc");
            query.setParameter("id", id);
            Internet lastInternet = (Internet) query.list().get(0);
            lastInternet.setEndDate(new Date());

            ClientInternet clientInternet = session.get(ClientInternet.class, id);
            clientInternet.setStatus(Status.SUSPENDED);

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

            ClientInternet clientInternet = session.get(ClientInternet.class, id);
            clientInternet.setStatus(Status.ACTIVE);

            Query query = session.createQuery("from Internet where internetId = :id order by beginDate desc");
            query.setParameter("id", id);
            Internet lastInternet = (Internet) query.list().get(0);

            Internet internet = new Internet();
            internet.setInternetId(lastInternet.getInternetId());
            internet.setAntivirus(lastInternet.isAntivirus());
            internet.setConnectionType(lastInternet.getConnectionType());
            internet.setSpeed(lastInternet.getSpeed());
            internet.setBeginDate(new Date());

            session.save(internet);

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

            ClientInternet clientInternet = session.get(ClientInternet.class, id);

            if (clientInternet.getStatus().equals(Status.ACTIVE)) {
                Query query = session.createQuery("from Internet where internetId = :id order by beginDate desc");
                query.setParameter("id", id);
                Internet lastInternet = (Internet) query.list().get(0);
                lastInternet.setEndDate(new Date());
            }

            clientInternet.setStatus(Status.DISCONNECTED);

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
