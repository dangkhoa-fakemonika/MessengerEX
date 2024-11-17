package org.example.mesexadmin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class PopUpController implements Initializable {

    Dialog<Objects> currentDialog;
    private Stage stage;
    private Scene scene;
    private Parent root;

    public void closeDialog(ActionEvent actionEvent){
        currentDialog.close();
        currentDialog.setResult(null);
        Stage stage = (Stage) currentDialog.getDialogPane().getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
