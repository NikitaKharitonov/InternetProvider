//import controller.BaseController;
//import controller.FailedOperation;
//import model.Model;
//import view.ConsoleView;
//import view.View;
//import model.models.BaseModel;
//
//import java.io.IOException;
//
//public class Main {
//    public static void main(String[] args) {
//        try {
//            Model model = new BaseModel();
//            model.read();
//            View view = new ConsoleView(new BaseController(model));
//            view.run();
//        } catch (IOException  | FailedOperation failedOperation) {
//            failedOperation.printStackTrace();
//        }
//    }
//}

import model.Client;
import model.Model;
import model.exceptions.InvalidModelException;
import model.exceptions.ServiceNotFoundException;
import model.exceptions.ClientNotFoundException;
import model.DBModel;
import model.services.Internet;
import model.services.Phone;
import model.services.Service;
import model.services.Television;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Main {
    public static void main(String[] args)
            throws ClientNotFoundException, InvalidModelException, ServiceNotFoundException {
        Model model = new DBModel();

        List<Client> clients = model.getClients();

        for (Client client : clients) {
            System.out.println(client.toString());
        }

        List<Service> internets = model.getClientServicesByType(4, "Internet");

        for (Service service : internets) {
            System.out.println(service.toString());
        }

//        Date activationDate = new Date();
//        Date date_begin = new Date();
//        Date date_end;
//        try {
//            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
//            date_end = format.parse("01.05.2020 23:59:59");
//        } catch (ParseException e) {
//            date_end = null;
//        }
//        Service.Status status = Service.Status.ACTIVE;
//
//        int speed = 100;
//        boolean antivirus = true;
//        Internet.ConnectionType connectionType = Internet.ConnectionType.Cable;
//
//        int minsCount = 300;
//        int smsCount = 100;
//
//        int channelsCount = 500;
//
//        Service internet = new Internet(activationDate, date_begin, date_end, status, speed, antivirus, connectionType);
//        Service phone = new Phone(activationDate, date_begin, date_end, status, minsCount, smsCount);
//        Service television = new Television(activationDate, date_begin, date_end, status, channelsCount);
//
//        model.addServiceToClient(5, internet);
//        model.addServiceToClient(5, phone);
//        model.addServiceToClient(5, television);
    }
}