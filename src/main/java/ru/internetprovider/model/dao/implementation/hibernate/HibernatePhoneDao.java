package ru.internetprovider.model.dao.implementation.hibernate;

import org.hibernate.Session;
import org.hibernate.query.Query;
import ru.internetprovider.model.dao.PhoneDao;
import ru.internetprovider.model.services.Phone;
import ru.internetprovider.model.services.PhoneState;
import ru.internetprovider.model.services.Status;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * The implementation of the Data Access Object pattern for Phone services
 * using the Hibernate technology.
 */
public class HibernatePhoneDao implements PhoneDao {

    SessionProvider sessionProvider = new SessionProvider();

    @Override
    public List<Phone> getAll(int clientId) {
        List<Phone> phoneClientServiceList = new ArrayList<>();
//        EntityTransaction entityTransaction = null;
//        try (Session session = HibernateUtil.openSession()) {
//            entityTransaction = session.getTransaction();
//            entityTransaction.begin();

        Session session = sessionProvider.openSessionWithTransaction();

        Query query = session.createQuery("from Phone where clientId = :clientId");
        query.setParameter("clientId", clientId);
        phoneClientServiceList = query.list();

        sessionProvider.closeSessionWithTransaction();

//            entityTransaction.commit();
//        } catch (Exception e) {
//            if (entityTransaction != null)
//                entityTransaction.rollback();
//            e.printStackTrace();
//        }
        return phoneClientServiceList;
    }

    @Override
    public List<PhoneState> getHistory(int id) {
        List<PhoneState> history = null;
//        EntityTransaction entityTransaction = null;
//        try (Session session = HibernateUtil.openSession()) {
//            entityTransaction = session.getTransaction();
//            entityTransaction.begin();

        Session session = sessionProvider.openSessionWithTransaction();

        Query query = session.createQuery("from PhoneState where phoneId = :id order by beginDate");
        query.setParameter("id", id);
        history = query.getResultList();
        history.sort(Comparator.comparing(PhoneState::getBeginDate));

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
    public void update(int id, PhoneState phoneState) {
//        EntityTransaction entityTransaction = null;
//        try (Session session = HibernateUtil.openSession()) {
//            entityTransaction = session.getTransaction();
//            entityTransaction.begin();

        Session session = sessionProvider.openSessionWithTransaction();

        Phone phone = session.get(Phone.class, id);
        if (phone.getStatus().equals(Status.ACTIVE)) {
            Query query = session.createQuery("from PhoneState where phoneId = :id order by beginDate desc");
            query.setParameter("id", id);
            PhoneState lastPhoneState = (PhoneState) query.list().get(0);
            lastPhoneState.setEndDate(new Date());
        } else if (phone.getStatus().equals(Status.SUSPENDED)) {
            phone.setStatus(Status.ACTIVE);
        }

        phoneState.setPhoneId(id);
        session.persist(phoneState);

        sessionProvider.closeSessionWithTransaction();

//            entityTransaction.commit();
//        } catch (Exception e) {
//            if (entityTransaction != null)
//                entityTransaction.rollback();
//            e.printStackTrace();
//        }
    }

    @Override
    public void add(int clientId, PhoneState phoneState) {
//        EntityTransaction entityTransaction = null;
//        try (Session session = HibernateUtil.openSession()) {
//            entityTransaction = session.getTransaction();
//            entityTransaction.begin();

        Session session = sessionProvider.openSessionWithTransaction();

        Phone clientService = new Phone(phoneState.getBeginDate(), Status.ACTIVE);
        clientService.setClientId(clientId);
        session.save(clientService);

        phoneState.setPhoneId(clientService.getId());
        session.save(phoneState);

        sessionProvider.closeSessionWithTransaction();

//            entityTransaction.commit();
//        } catch (Exception e) {
//            if (entityTransaction != null)
//                entityTransaction.rollback();
//            e.printStackTrace();
//        }
    }

    @Override
    public Phone get(int id) {
        Phone phone = null;
//        EntityTransaction entityTransaction = null;
//        try (Session session = HibernateUtil.openSession()) {
//            entityTransaction = session.getTransaction();
//            entityTransaction.begin();

        Session session = sessionProvider.openSessionWithTransaction();

        Query query = session.createQuery("from Phone where id = :id");
        query.setParameter("id", id);
        phone = (Phone) query.getSingleResult();

        sessionProvider.closeSessionWithTransaction();

//            entityTransaction.commit();
//        } catch (Exception e) {
//            if (entityTransaction != null)
//                entityTransaction.rollback();
//            e.printStackTrace();
//        }
        return phone;
    }

    @Override
    public void suspend(int id) {
//        EntityTransaction entityTransaction = null;
//        try (Session session = HibernateUtil.openSession()) {
//            entityTransaction = session.getTransaction();
//            entityTransaction.begin();

        Session session = sessionProvider.openSessionWithTransaction();

        Query query = session.createQuery("from PhoneState where phoneId = :id order by beginDate desc");
        query.setParameter("id", id);
        PhoneState lastPhoneState = (PhoneState) query.list().get(0);
        lastPhoneState.setEndDate(new Date());

        Phone phone = session.get(Phone.class, id);
        phone.setStatus(Status.SUSPENDED);

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

        Phone phone = session.get(Phone.class, id);
        phone.setStatus(Status.ACTIVE);

        Query query = session.createQuery("from PhoneState where phoneId = :id order by beginDate desc");
        query.setParameter("id", id);
        PhoneState lastPhoneState = (PhoneState) query.list().get(0);

        PhoneState phoneState = new PhoneState();
        phoneState.setPhoneId(lastPhoneState.getPhoneId());
        phoneState.setSmsCount(lastPhoneState.getSmsCount());
        phoneState.setMinsCount(lastPhoneState.getMinsCount());
        phoneState.setBeginDate(new Date());

        session.save(phoneState);

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

        Phone phone = session.get(Phone.class, id);

        if (phone.getStatus().equals(Status.ACTIVE)) {
            Query query = session.createQuery("from PhoneState where phoneId = :id order by beginDate desc");
            query.setParameter("id", id);
            PhoneState lastPhoneState = (PhoneState) query.list().get(0);
            lastPhoneState.setEndDate(new Date());
        }

        phone.setStatus(Status.DELETED);

        sessionProvider.closeSessionWithTransaction();

//            entityTransaction.commit();
//        } catch (Exception e) {
//            if (entityTransaction != null)
//                entityTransaction.rollback();
//            e.printStackTrace();
//        }
    }
}
