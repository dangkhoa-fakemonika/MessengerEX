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

    public Dialog<Objects> currentDialog;

    public void closeDialog(ActionEvent actionEvent){
        currentDialog.close();
        currentDialog.setResult(null);
        Stage stage = (Stage) currentDialog.getDialogPane().getScene().getWindow();
        stage.close();
    }

    public void acceptCreateGroup(ActionEvent actionEvent){
        boolean res = currentUser.myQuery.conversations().createConversation(currentUser.getSessionUserData().getUserId(), "dummy dummy");
        if (res){
            System.out.println("New conversation created");
            closeDialog(actionEvent);
        }

    }

    public void acceptAddFriend(ActionEvent actionEvent){
        boolean res = currentUser.myQuery.users().addFriend(currentUser.getSessionUserData().getUserId(), new ObjectId());
        if (res){
            System.out.println("New conversation created");
            closeDialog(actionEvent);
        }
    }

    public void deleteAccount(ActionEvent actionEvent){

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currentUser = Main.getThisUser();
    }
    public String getUsernameField() {
        return usernameField.getText();
    }

    public void clearAllFields() {
        if (usernameField != null) {
            usernameField.clear();
        }
    }
}
