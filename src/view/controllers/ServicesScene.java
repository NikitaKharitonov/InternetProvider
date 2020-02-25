package view.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ServicesScene {

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
        Parent parent = FXMLLoader.load(getClass().getResource("fxml/user_scene.fxml"));
        stage.setScene(new Scene(parent));
    }
}
