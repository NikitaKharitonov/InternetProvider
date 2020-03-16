import model.Client;
import model.DBModel;
import model.Model;
import model.exceptions.ClientNotFoundException;
import model.exceptions.InvalidModelException;
import model.exceptions.ServiceNotFoundException;
import model.services.ClientService;
import model.services.Condition;
import model.services.Internet;

import java.util.List;

public class Main {
    public static void main(String[] args)
            throws InvalidModelException, ClientNotFoundException, ServiceNotFoundException {
        Model model = new DBModel();
        for (Client client : model.getClients()) {
            System.out.println(client.toString());
            for (ClientService<Internet> internet : model.getClientInternets(client.getId())) {
                System.out.println(internet.toString());
                for (Condition condition : model.getInternetHistory(internet.getId())) {
                    System.out.println(condition.toString());
                }
            };
            System.out.println();
        }
    }
}