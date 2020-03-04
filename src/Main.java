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

import model.Model;
import model.exceptions.InvalidModelException;
import model.exceptions.ServiceNotFoundException;
import model.exceptions.ClientNotFoundException;
import model.DBModel;
import model.services.Internet;
import model.services.Service;

import java.util.Date;

public class Main {
    public static void main(String[] args)
            throws ClientNotFoundException, ServiceNotFoundException, InvalidModelException {
        Model model = new DBModel();
        Service internet = new Internet(
                new Date(),
                new Date(),
                new Date(),
                //new Date(2020, 4, 1, 0, 0, 0),
                Service.Status.ACTIVE,
                100,
                true,
                Internet.ConnectionType.Cable
        );
        model.addServiceToClient(5, internet);

//        Service[] services = model.getClientServicesByType(4, "Internet");
//        for (Service service : services) {
//            System.out.println(service.toString());
//        }
    }
}