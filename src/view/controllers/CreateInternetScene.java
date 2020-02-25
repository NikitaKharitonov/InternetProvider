package view.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.services.Internet;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CreateInternetScene implements Initializable {

    public TextField nameField;
    public Spinner<Integer> speedSpinner;
    public ChoiceBox<String> connectionTypeChoiceBox;
    public CheckBox antivirusCheckBox;
    public DatePicker activationDatePicker;

    @FXML
    private void createButtonClicked(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((javafx.scene.Node)event.getSource()).getScene().getWindow();
        Parent parent = FXMLLoader.load(getClass().getResource("fxml/internet_scene.fxml"));
        stage.setScene(new Scene(parent));
    }


    @FXML
    private void backButtonClicked(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((javafx.scene.Node)event.getSource()).getScene().getWindow();
        Parent parent = FXMLLoader.load(getClass().getResource("fxml/internet_scene.fxml"));
        stage.setScene(new Scene(parent));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        connectionTypeChoiceBox.getItems().addAll(Internet.getConnectionTypes());
    }
}
