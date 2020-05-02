import ru.internetprovider.model.Client;
import ru.internetprovider.model.DBModel;
import ru.internetprovider.model.exceptions.ClientNotFoundException;
import ru.internetprovider.model.exceptions.ServiceNotFoundException;
import ru.internetprovider.model.services.Internet;
import ru.internetprovider.model.services.Phone;
import ru.internetprovider.model.services.Television;

import java.util.Date;
import java.sql.SQLException;

public class Test {

    public static void main(String[] args) throws SQLException, ClientNotFoundException, ServiceNotFoundException {
        DBModel dbModel = new DBModel();

        dbModel.updateInternet(4, new Internet(new Date(), null, 300, true, Internet.ConnectionType.ADSL));
        System.out.println(dbModel.getTelevisionHistory(6));

    }
}
