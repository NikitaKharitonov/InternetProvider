package ru.internetprovider.model.dao.implementation.hibernate;

import org.hibernate.Session;
import org.hibernate.query.Query;
import ru.internetprovider.model.dao.PhoneDao;
import ru.internetprovider.model.services.Phone;
import ru.internetprovider.model.services.TemporalPhone;
import ru.internetprovider.model.services.Status;

import javax.persistence.EntityTransaction;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class HibernatePhoneDao implements PhoneDao {

    @Override
    public List<Phone> getAll(int clientId) {
        List<Phone> phoneClientServiceList = new ArrayList<>();
        EntityTransaction entityTransaction = null;
        try (Session session = HibernateUtil.openSession()) {
            entityTransaction = session.getTransaction();
            entityTransaction.begin();

            Query query = session.createQuery("from Phone where clientId = :clientId");
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
    public List<TemporalPhone> getHistory(int id) {
        List<TemporalPhone> history = null;
        EntityTransaction entityTransaction = null;
        try (Session session = HibernateUtil.openSession()) {
            entityTransaction = session.getTransaction();
            entityTransaction.begin();

            Query query = session.createQuery("from Phone where id = :id");
            query.setParameter("id", id);
            Phone phone = (Phone) query.getSingleResult();
            history = phone.getHistory();
            history.sort(Comparator.comparing(TemporalPhone::getBeginDate));

            entityTransaction.commit();
        } catch (Exception e) {
            if (entityTransaction != null)
                entityTransaction.rollback();
            e.printStackTrace();
        }
        return history;
    }

    @Override
    public void update(int id, TemporalPhone temporalPhone) {
        EntityTransaction entityTransaction = null;
        try (Session session = HibernateUtil.openSession()) {
            entityTransaction = session.getTransaction();
            entityTransaction.begin();

            Phone phone = session.get(Phone.class, id);
            if (phone.getStatus().equals(Status.ACTIVE)) {
                Query query = session.createQuery("from TemporalPhone where phoneId = :id order by beginDate desc");
                query.setParameter("id", id);
                TemporalPhone lastTemporalPhone = (TemporalPhone) query.list().get(0);
                lastTemporalPhone.setEndDate(new Date());
            } else if (phone.getStatus().equals(Status.SUSPENDED)) {
                phone.setStatus(Status.ACTIVE);
            }

            temporalPhone.setPhoneId(id);
            session.persist(temporalPhone);

            entityTransaction.commit();
        } catch (Exception e) {
            if (entityTransaction != null)
                entityTransaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void add(int clientId, TemporalPhone temporalPhone) {
        EntityTransaction entityTransaction = null;
        try (Session session = HibernateUtil.openSession()) {
            entityTransaction = session.getTransaction();
            entityTransaction.begin();

            Phone clientService = new Phone(temporalPhone.getBeginDate(), Status.ACTIVE);
            clientService.setClientId(clientId);
            session.save(clientService);

            temporalPhone.setPhoneId(clientService.getId());
            session.save(temporalPhone);

            entityTransaction.commit();
        } catch (Exception e) {
            if (entityTransaction != null)
                entityTransaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public Phone get(int id) {
        Phone phone = null;
        EntityTransaction entityTransaction = null;
        try (Session session = HibernateUtil.openSession()) {
            entityTransaction = session.getTransaction();
            entityTransaction.begin();

            Query query = session.createQuery("from Phone where id = :id");
            query.setParameter("id", id);
            phone = (Phone) query.getSingleResult();

            entityTransaction.commit();
        } catch (Exception e) {
            if (entityTransaction != null)
                entityTransaction.rollback();
            e.printStackTrace();
        }
        return phone;
    }

    @Override
    public void suspend(int id) {
        EntityTransaction entityTransaction = null;
        try (Session session = HibernateUtil.openSession()) {
            entityTransaction = session.getTransaction();
            entityTransaction.begin();

            // fixme get by id
            Query query = session.createQuery("from TemporalPhone where phoneId = :id order by beginDate desc");
            query.setParameter("id", id);
            TemporalPhone lastTemporalPhone = (TemporalPhone) query.list().get(0);
            lastTemporalPhone.setEndDate(new Date());

            Phone phone = session.get(Phone.class, id);
            phone.setStatus(Status.SUSPENDED);

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

            Phone phone = session.get(Phone.class, id);
            phone.setStatus(Status.ACTIVE);

            Query query = session.createQuery("from TemporalPhone where phoneId = :id order by beginDate desc");
            query.setParameter("id", id);
            TemporalPhone lastTemporalPhone = (TemporalPhone) query.list().get(0);

            TemporalPhone temporalPhone = new TemporalPhone();
            temporalPhone.setPhoneId(lastTemporalPhone.getPhoneId());
            temporalPhone.setSmsCount(lastTemporalPhone.getSmsCount());
            temporalPhone.setMinsCount(lastTemporalPhone.getMinsCount());
            temporalPhone.setBeginDate(new Date());

            session.save(temporalPhone);

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

            Phone phone = session.get(Phone.class, id);

            if (phone.getStatus().equals(Status.ACTIVE)) {
                Query query = session.createQuery("from TemporalPhone where phoneId = :id order by beginDate desc");
                query.setParameter("id", id);
                TemporalPhone lastTemporalPhone = (TemporalPhone) query.list().get(0);
                lastTemporalPhone.setEndDate(new Date());
            }

            phone.setStatus(Status.DISCONNECTED);

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
