package view.controllers;

import model.Client;
import view.ClientApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientScene implements Initializable {
    public Label idLabel;
    public Label nameLabel;
    public Label phoneLabel;
    public Label emailLabel;

    @FXML
    private void updatePersonalDataButtonClicked(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Parent parent = FXMLLoader.load(getClass().getResource("fxml/update_personal_data_scene.fxml"));
        stage.setScene(new Scene(parent));
    }

    @FXML
    private void internetButtonClicked(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Parent parent = FXMLLoader.load(getClass().getResource("fxml/internet_scene.fxml"));
        stage.setScene(new Scene(parent));
    }

    @FXML
    private void phoneButtonClicked(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Parent parent = FXMLLoader.load(getClass().getResource("fxml/phone_scene.fxml"));
        stage.setScene(new Scene(parent));
    }

    @FXML
    private void televisionButtonClicked(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Parent parent = FXMLLoader.load(getClass().getResource("fxml/television_scene.fxml"));
        stage.setScene(new Scene(parent));
    }

    @FXML
    private void backButtonClicked(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Parent parent = FXMLLoader.load(getClass().getResource("fxml/main_scene.fxml"));
        stage.setScene(new Scene(parent));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idLabel.setText("ID: " + ClientApplication.selectedClient.getId());
        nameLabel.setText("Name: " + ClientApplication.selectedClient.getName());
        phoneLabel.setText("Phone: " + ClientApplication.selectedClient.getPhone());
        emailLabel.setText("Email: " + ClientApplication.selectedClient.getEmail());
    }
}