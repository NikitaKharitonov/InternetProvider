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

import model.exceptions.UserNotFoundException;
import model.DBModel;
import model.User;

public class Main {
    public static void main(String[] args) throws UserNotFoundException {
        DBModel model = new DBModel();
        User user = model.getUser(1);
        System.out.println(user.toString());
    }
}