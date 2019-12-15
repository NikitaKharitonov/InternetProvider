import controller.BaseController;
import controller.FailedOperation;
import model.data_storage_factories.XMLFileDataStorageFactory;
import model.models.Model;
import view.ConsoleView;
import view.View;
import model.models.BaseModel;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            Model model = new BaseModel();
            model.setDataStorageFactory(new XMLFileDataStorageFactory());
            model.read();
            View view = new ConsoleView(new BaseController(model));
            view.run();
        } catch (IOException | FailedOperation e) {
            System.err.println(e.getMessage());
        }

    }
}
