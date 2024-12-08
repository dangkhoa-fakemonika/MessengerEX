package org.example.mesexadmin.ui.user_level;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.stage.Stage;
import org.example.mesexadmin.PopUpController;

//import javax.mail.Session;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class EditProfileController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    void bufferScene(ActionEvent actionEvent){
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void returnToMain(ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main-messaging.fxml")));
        bufferScene(actionEvent);
    }

    public void changePassword(ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("change-password.fxml")));
        bufferScene(actionEvent);
    }

    public void changeUsername(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("pop-up-change-name.fxml"));
        Dialog<Objects> dialog = new Dialog<>();
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        DialogPane dialogPane = loader.load();
        PopUpController popUpController = loader.getController();
        popUpController.currentDialog = dialog;
        dialog.setDialogPane(dialogPane);
        dialog.showAndWait();
    }

    public void sendEmail(){
        String receiver = "mcfacebook911@gmail.com";
        String sender = "amoungus69@sussy.com";
        String host = "127.0.0.1";
        Properties properties = System.getProperties();
//        Session session = Session.de
    }

    public void resetPassword(ActionEvent actionEvent) {
        Alert newAlert = new Alert(Alert.AlertType.CONFIRMATION);
        newAlert.setContentText("Send reset request to your email?");
        newAlert.setHeaderText("Reset Password");
        newAlert.showAndWait();
    }

    public void changeEmail(ActionEvent actionEvent) {
        Alert newAlert = new Alert(Alert.AlertType.CONFIRMATION);
        newAlert.setContentText("Send email change request to user's email?");
        newAlert.setHeaderText("Change Email");
        newAlert.showAndWait();
    }


}
