package view.controllers;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox {
    static void display(String message) {
        Stage stage = new Stage();

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Error");
        stage.setMinWidth(250);
        stage.setMinHeight(200);

        Label label = new Label(message);
        Button button = new Button("OK");
        button.setOnAction(e -> stage.close());

        VBox box = new VBox(10);
        box.getChildren().addAll(label, button);
        box.setAlignment(Pos.CENTER);

        Scene scene = new Scene(box);
        stage.setScene(scene);
        stage.showAndWait();
    }
}
