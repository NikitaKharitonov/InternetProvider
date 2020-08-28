package ru.internetprovider.model.dao.implementation.hibernate;

import org.hibernate.Session;
import org.hibernate.query.Query;
import ru.internetprovider.model.dao.PhoneDao;
import ru.internetprovider.model.dao.ServiceDao;
import ru.internetprovider.model.services.ClientPhone;
import ru.internetprovider.model.services.ClientService;
import ru.internetprovider.model.services.Phone;
import ru.internetprovider.model.services.Status;

import javax.persistence.EntityTransaction;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class HibernatePhoneDao implements PhoneDao {

    @Override
    public List<ClientPhone> getAll(int clientId) {
        List<ClientPhone> phoneClientServiceList = new ArrayList<>();
        EntityTransaction entityTransaction = null;
        try (Session session = HibernateUtil.openSession()) {
            entityTransaction = session.getTransaction();
            entityTransaction.begin();

            Query query = session.createQuery("from ClientPhone where clientId = :clientId");
            query.setParameter("clientId", clientId);
            phoneClientServiceList = query.list();

            entityTransaction.commit();
        } catch (Exception e) {
            if (entityTransaction != null)
                entityTransaction.rollback();
            e.printStackTrace();
        }
        return phoneClientServiceList;
    }

    @Override
    public List<Phone> getHistory(int id) {
        List<Phone> history = null;
        EntityTransaction entityTransaction = null;
        try (Session session = HibernateUtil.openSession()) {
            entityTransaction = session.getTransaction();
            entityTransaction.begin();

            Query query = session.createQuery("from ClientPhone where id = :id");
            query.setParameter("id", id);
            ClientPhone clientPhone = (ClientPhone) query.getSingleResult();
            history = clientPhone.getHistory();
            history.sort(Comparator.comparing(Phone::getBeginDate));

            entityTransaction.commit();
        } catch (Exception e) {
            if (entityTransaction != null)
                entityTransaction.rollback();
            e.printStackTrace();
        }
        return history;
    }

    @Override
    public void update(int id, Phone phone) {
        EntityTransaction entityTransaction = null;
        try (Session session = HibernateUtil.openSession()) {
            entityTransaction = session.getTransaction();
            entityTransaction.begin();

            ClientPhone clientPhone = session.get(ClientPhone.class, id);
            if (clientPhone.getStatus().equals(Status.ACTIVE)) {
                Query query = session.createQuery("from Phone where phoneId = :id order by beginDate desc");
                query.setParameter("id", id);
                Phone lastPhone = (Phone) query.list().get(0);
                lastPhone.setEndDate(new Date());
            } else if (clientPhone.getStatus().equals(Status.SUSPENDED)) {
                clientPhone.setStatus(Status.ACTIVE);
            }

            phone.setPhoneId(id);
            session.persist(phone);

            entityTransaction.commit();
        } catch (Exception e) {
            if (entityTransaction != null)
                entityTransaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void add(int clientId, Phone phone) {
        EntityTransaction entityTransaction = null;
        try (Session session = HibernateUtil.openSession()) {
            entityTransaction = session.getTransaction();
            entityTransaction.begin();

            ClientPhone clientService = new ClientPhone(phone.getBeginDate(), Status.ACTIVE);
            clientService.setClientId(clientId);
            session.save(clientService);

            phone.setPhoneId(clientService.getId());
            session.save(phone);

            entityTransaction.commit();
        } catch (Exception e) {
            if (entityTransaction != null)
                entityTransaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public ClientPhone get(int id) {
        ClientPhone clientPhone = null;
        EntityTransaction entityTransaction = null;
        try (Session session = HibernateUtil.openSession()) {
            entityTransaction = session.getTransaction();
            entityTransaction.begin();

            Query query = session.createQuery("from ClientPhone where id = :id");
            query.setParameter("id", id);
            clientPhone = (ClientPhone) query.getSingleResult();

            entityTransaction.commit();
        } catch (Exception e) {
            if (entityTransaction != null)
                entityTransaction.rollback();
            e.printStackTrace();
        }
        return clientPhone;
    }

    @Override
    public void suspend(int id) {
        EntityTransaction entityTransaction = null;
        try (Session session = HibernateUtil.openSession()) {
            entityTransaction = session.getTransaction();
            entityTransaction.begin();

            // fixme get by id
            Query query = session.createQuery("from Phone where phoneId = :id order by beginDate desc");
            query.setParameter("id", id);
            Phone lastPhone = (Phone) query.list().get(0);
            lastPhone.setEndDate(new Date());

            ClientPhone clientPhone = session.get(ClientPhone.class, id);
            clientPhone.setStatus(Status.SUSPENDED);

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

            ClientPhone clientPhone = session.get(ClientPhone.class, id);
            clientPhone.setStatus(Status.ACTIVE);

            Query query = session.createQuery("from Phone where phoneId = :id order by beginDate desc");
            query.setParameter("id", id);
            Phone lastPhone = (Phone) query.list().get(0);

            Phone phone = new Phone();
            phone.setPhoneId(lastPhone.getPhoneId());
            phone.setSmsCount(lastPhone.getSmsCount());
            phone.setMinsCount(lastPhone.getMinsCount());
            phone.setBeginDate(new Date());

            session.save(phone);

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

            ClientPhone clientPhone = session.get(ClientPhone.class, id);

            if (clientPhone.getStatus().equals(Status.ACTIVE)) {
                Query query = session.createQuery("from Phone where phoneId = :id order by beginDate desc");
                query.setParameter("id", id);
                Phone lastPhone = (Phone) query.list().get(0);
                lastPhone.setEndDate(new Date());
            }

            clientPhone.setStatus(Status.DISCONNECTED);

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
