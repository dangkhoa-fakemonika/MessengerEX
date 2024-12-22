package org.example.mesexadmin.ui.user_level;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;

import org.example.mesexadmin.App;
import org.example.mesexadmin.SceneManager;
import org.example.mesexadmin.SessionUser;
import org.example.mesexadmin.ui.ControllerWrapper;

import java.net.URL;
import java.util.ResourceBundle;

public class ChangePasswordController implements ControllerWrapper {
    static SceneManager sceneManager;
    SessionUser currentUser;

    @FXML private PasswordField oldPasswordField;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Button confirmButton;
    @FXML private Button returnToEditProfileButton;

    @Override
    public void myInitialize() {
        currentUser = App.getCurrentUser();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sceneManager = App.getSceneManager();
        
        confirmButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {

                String oldPassword = oldPasswordField.getText();
                if (oldPassword.isEmpty()) {
                    new Alert(AlertType.ERROR, "Old password is required!").showAndWait();
                    return;
                }

                String newPassword = newPasswordField.getText();
                if (newPassword.isEmpty()) {
                    new Alert(AlertType.ERROR, "New password is required!").showAndWait();
                    return;
                }
                String confirmPassword = confirmPasswordField.getText();
                if (confirmPassword.isEmpty()) {
                    new Alert(AlertType.ERROR, "Confirm password is required!").showAndWait();
                    return;
                }

                if (currentUser.changePassword(oldPassword, newPassword, confirmPassword)) {
                    new Alert(AlertType.INFORMATION, "Change password success!").showAndWait();
                    try {
                        clearField();
                        sceneManager.addScene("EditProfile", "edit-user-profile.fxml");
                        sceneManager.switchScene("EditProfile");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        returnToEditProfileButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                try {
                    clearField();
                    sceneManager.addScene("EditProfile", "edit-user-profile.fxml");
                    sceneManager.switchScene("EditProfile");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void clearField() {
        oldPasswordField.clear();
        newPasswordField.clear();
        confirmPasswordField.clear();
    }
}
