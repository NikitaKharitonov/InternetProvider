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

import model.exceptions.ServiceNotFoundException;
import model.exceptions.UserNotFoundException;
import model.DBModel;
import model.User;
import model.services.Internet;
import model.services.Service;

public class Main {
    public static void main(String[] args) throws UserNotFoundException, ServiceNotFoundException {
        DBModel model = new DBModel();
        Service[] services = model.getUserServicesByType(4, "Internet");
        for (Service service : services) {
            if (service instanceof Internet) {
                Internet internet = (Internet) service;
                System.out.println(internet.toString());
            }
        }
    }
}