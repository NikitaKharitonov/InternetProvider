import controller.BaseController;
import controller.FailedOperation;
import model.models.FileModel;
import view.ConsoleView;
import view.View;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, FailedOperation {
        View view = new ConsoleView(new BaseController(new FileModel()));
        view.run();
    }
}
