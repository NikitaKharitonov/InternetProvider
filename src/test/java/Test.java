import org.hibernate.Session;
import org.hibernate.query.Query;
import ru.internetprovider.model.dao.implementation.hibernate.HibernateUtil;
import ru.internetprovider.model.services.Service;

import javax.persistence.EntityTransaction;
import java.util.List;

public class Test {

    public static void main(String[] args) {
        List<Service> serviceList = null;
        EntityTransaction entityTransaction = null;
        try (Session session = HibernateUtil.openSession()) {
            entityTransaction = session.getTransaction();
            entityTransaction.begin();

            Query query = session.createQuery("from Internet where clientId = :clientId");
            query.setParameter("clientId", 2);
            serviceList = query.list();

            entityTransaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (entityTransaction != null)
                entityTransaction.rollback();
        }
        System.out.println(serviceList);

    }
}
