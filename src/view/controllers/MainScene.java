package view.controllers;

import model.User;
import model.exceptions.ClientNotFoundException;
import view.UserApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class MainScene {

    @FXML private TextField idField;

    @FXML
    private void getUserByIdButtonClicked(ActionEvent event) throws IOException {

        try {
            long id = Long.parseLong(idField.getText());
            UserApplication.selectedUser = UserApplication.model.getUser(id);
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            Parent parent = FXMLLoader.load(getClass().getResource("fxml/user_scene.fxml"));
            stage.setScene(new Scene(parent));
        } catch (ClientNotFoundException | NumberFormatException e) {
            AlertBox.display(e.getMessage());
        }
    }

    @FXML
    private void createUserButtonClicked(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Parent parent = FXMLLoader.load(getClass().getResource("fxml/create_user_scene.fxml"));
        stage.setScene(new Scene(parent));
    }
}
