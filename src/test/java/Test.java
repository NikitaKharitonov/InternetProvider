import org.hibernate.Session;
import org.hibernate.query.Query;
import ru.internetprovider.model.hibernate.HibernateUtil;
import ru.internetprovider.model.services.ClientService;

import javax.persistence.EntityTransaction;
import java.util.List;

public class Test {

    public static void main(String[] args) {
        List<ClientService> clientServiceList = null;
        EntityTransaction entityTransaction = null;
        try (Session session = HibernateUtil.openSession()) {
            entityTransaction = session.getTransaction();
            entityTransaction.begin();

            Query query = session.createQuery("from ClientInternet where clientId = :clientId");
            query.setParameter("clientId", 2);
            clientServiceList = query.list();

            entityTransaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (entityTransaction != null)
                entityTransaction.rollback();
        }
        System.out.println(clientServiceList);

    }
}
