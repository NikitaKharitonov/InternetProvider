package view.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class CreatePhoneScene {

    public TextField nameField;
    public TextField numberOfMinutesField;
    public TextField numberOfSmsField;

    @FXML
    private void createButtonClicked(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((javafx.scene.Node)event.getSource()).getScene().getWindow();
        Parent parent = FXMLLoader.load(getClass().getResource("fxml/phone_scene.fxml"));
        stage.setScene(new Scene(parent));
    }

    @FXML
    private void backButtonClicked(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((javafx.scene.Node)event.getSource()).getScene().getWindow();
        Parent parent = FXMLLoader.load(getClass().getResource("fxml/phone_scene.fxml"));
        stage.setScene(new Scene(parent));
    }
}
