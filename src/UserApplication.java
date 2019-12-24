import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UserApplication extends Application
{
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("User Application");
        Button button = new Button("Personal Data");
        Button button1 = new Button("Connected Services");
        Button button3 = new Button("Log Out");

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(8);
        vbox.getChildren().add(button);
        vbox.getChildren().add(button1);
        vbox.getChildren().add(button3);
        primaryStage.setScene(new Scene(vbox, 300, 250));
        primaryStage.show();
    }
}