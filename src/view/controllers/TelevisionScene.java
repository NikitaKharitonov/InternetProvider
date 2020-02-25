package view.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TelevisionScene {

    @FXML
    private void createButtonClicked(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((javafx.scene.Node)event.getSource()).getScene().getWindow();
        Parent parent = FXMLLoader.load(getClass().getResource("fxml/create_television_scene.fxml"));
        stage.setScene(new Scene(parent));
    }

    @FXML
    private void backButtonClicked(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((javafx.scene.Node)event.getSource()).getScene().getWindow();
        Parent parent = FXMLLoader.load(getClass().getResource("fxml/user_scene.fxml"));
        stage.setScene(new Scene(parent));
    }
}
