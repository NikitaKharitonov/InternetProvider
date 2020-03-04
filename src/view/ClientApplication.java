package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.DBModel;
import model.Model;
import model.Client;
import model.exceptions.InvalidModelException;

import java.io.IOException;

public class ClientApplication extends Application {
    public static Client selectedClient;
    public static Model model = new DBModel();

    public static void main(String[] args) throws InvalidModelException {
        Model model = new DBModel();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("controllers/fxml/main_scene.fxml"));
        primaryStage.setTitle("Internet Provider");
        primaryStage.setScene(new Scene(parent));
        primaryStage.show();
    }
}
