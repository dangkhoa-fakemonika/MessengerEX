package org.example.mesexadmin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class PopUpController implements Initializable {
    SessionUser currentUser;

    @FXML private TextField singleTextField;
    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private TextField groupNameField;


    public Dialog<Objects> currentDialog;

    public void closeDialog(ActionEvent actionEvent){
        currentDialog.close();
        currentDialog.setResult(null);
        Stage stage = (Stage) currentDialog.getDialogPane().getScene().getWindow();
        stage.close();
    }

    public void deleteAccount(ActionEvent actionEvent){

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currentUser = Main.getCurrentUser();
    }
    
    public String getUsernameField() {
        return usernameField.getText();
    }

    public String getEmailField() {
        return emailField.getText();
    }

    public String getGroupNameField() {
        return groupNameField.getText();
    }

    public void setUsername(String text){
        usernameField.setText(text);
        usernameField.setDisable(true);
    }

    public void clearAllFields() {
        if (usernameField != null) {
            usernameField.clear();
            usernameField.setDisable(false);
        }
        if (emailField != null) {
            emailField.clear();
        }

        if (groupNameField != null){
            groupNameField.clear();
        }
    }
}
