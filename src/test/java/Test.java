import ru.internetprovider.model.InternetDao;
import ru.internetprovider.model.exceptions.ClientNotFoundException;
import ru.internetprovider.model.exceptions.ServiceNotFoundException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.SQLException;

public class Test {

    public static void main(String[] args) throws SQLException, ClientNotFoundException, ServiceNotFoundException {

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        InternetDao internetDao = new InternetDao();
        Date date = internetDao.get(17).getActivationDate();

        System.out.println(formatter.format(date));

    }
}
