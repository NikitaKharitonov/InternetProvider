package view.controllers;

import controller.FailedOperation;
import view.UserApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;
import model.User;
import view.UserApplication;

import java.io.IOException;

public class CreateUserScene {
    public TextField idField;
    @FXML public TextField nameField;
    @FXML private TextField phoneField;
    @FXML public TextField emailField;

    @FXML
    private void createButtonClicked(ActionEvent event) {
        try {
            String name = nameField.getText();
            if (name.equals(""))
                throw new IllegalArgumentException("Enter name");
            long id = Long.parseLong(idField.getText());
            UserApplication.model.addUser(new User(id, name, phoneField.getText(), emailField.getText()));
        } catch (IllegalArgumentException e) {
            AlertBox.display(e.getMessage());
        }
    }

    @FXML
    private void backButtonClicked(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((javafx.scene.Node)event.getSource()).getScene().getWindow();
        Parent parent = FXMLLoader.load(getClass().getResource("fxml/main_scene.fxml"));
        stage.setScene(new Scene(parent));
    }

}
