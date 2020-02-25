package view.controllers;

import view.UserApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class CreateTelevisionScene {

    public TextField nameField;
    public TextField numberOfChannelsField;

    @FXML
    private void createButtonClicked(ActionEvent event) throws IOException {
        try {
            String nameString = nameField.getText();
            String numberOfChannelsString = numberOfChannelsField.getText();
            if (nameString.equals("") || numberOfChannelsString.equals(""))
                throw new IllegalArgumentException("Enter data");
            int numberOfChannels = Integer.parseInt(numberOfChannelsString);
        } catch (IllegalArgumentException e) {
            AlertBox.display(e.getMessage());
        }

        Stage stage = (Stage) ((javafx.scene.Node)event.getSource()).getScene().getWindow();
        Parent parent = FXMLLoader.load(getClass().getResource("fxml/television_scene.fxml"));
        stage.setScene(new Scene(parent));
    }

    @FXML
    private void backButtonClicked(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((javafx.scene.Node)event.getSource()).getScene().getWindow();
        Parent parent = FXMLLoader.load(getClass().getResource("fxml/television_scene.fxml"));
        stage.setScene(new Scene(parent));
    }
}
