package view.controllers;

import model.exceptions.InvalidModelException;
import view.ClientApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Client;

import java.io.IOException;

public class CreateClientScene {
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
            ClientApplication.model.addClient(new Client(id, name, phoneField.getText(), emailField.getText()));
        } catch (IllegalArgumentException e) {
            AlertBox.display(e.getMessage());
        } catch (InvalidModelException e) {

        }
    }

    @FXML
    private void backButtonClicked(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((javafx.scene.Node)event.getSource()).getScene().getWindow();
        Parent parent = FXMLLoader.load(getClass().getResource("fxml/main_scene.fxml"));
        stage.setScene(new Scene(parent));
    }

}
