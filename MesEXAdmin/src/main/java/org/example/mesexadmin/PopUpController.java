package org.example.mesexadmin;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Dialog;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class PopUpController implements Initializable {

    Dialog<Objects> currentDialog;

    public void closeDialog(){
        System.out.println(currentDialog);
        currentDialog.close();
        currentDialog.setResult(null);
        Stage stage = (Stage) currentDialog.getDialogPane().getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
