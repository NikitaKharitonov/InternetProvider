import server.controller.BaseController;
import server.controller.FailedOperation;
import server.model.data_storage_factories.XMLFileDataStorageFactory;
import server.model.models.Model;
import server.view.ConsoleView;
import server.view.View;
import server.model.models.BaseModel;

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
