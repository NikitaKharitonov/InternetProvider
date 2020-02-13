//import controller.BaseController;
//import controller.FailedOperation;
//import model.models.Model;
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
