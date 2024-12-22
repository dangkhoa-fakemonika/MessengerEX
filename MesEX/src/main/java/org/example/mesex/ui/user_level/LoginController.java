package org.example.mesex.ui.user_level;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import org.example.mesex.App;
import org.example.mesex.PopUpController;
import org.example.mesex.SceneManager;
import org.example.mesex.SessionUser;
import org.example.mesex.ui.ControllerWrapper;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController implements ControllerWrapper {
    private SceneManager sceneManager;
    private SessionUser currentUser;

    @FXML private Button loginButton;
    @FXML private Button switchToRegisterButton;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button resetPasswordButton;

    // public void registerScene(ActionEvent actionEvent) throws IOException {
    //     sceneManager.addScene("Register", "main-register.fxml");
    //     sceneManager.switchScene("Register");
    // }

    // public void mainScene(ActionEvent actionEvent) throws IOException {
    //     sceneManager.addScene("Main", "main-messaging.fxml");
    //     sceneManager.switchScene("Main");
    // }

    private void clearField() {
        usernameField.clear();
        passwordField.clear();
    }

    @Override
    public void myInitialize() {
        currentUser = App.getCurrentUser(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sceneManager = App.getSceneManager();

        // Adding event handler using anonymous inner class (not lambda)
        switchToRegisterButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    clearField();
                    sceneManager.addScene("Register", "main-register.fxml");
                    sceneManager.switchScene("Register");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (verifyLogin()) {
                    Dialog<String> dialog = new Dialog<>();
                    dialog.setTitle("Success");
                    dialog.setContentText("Login successfully!");
                    dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
                    dialog.showAndWait();
                    
                    try {
                        clearField();
                        sceneManager.addScene("Main", "main-messaging.fxml");
                        sceneManager.switchScene("Main");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        resetPasswordButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                FXMLLoader loader = new FXMLLoader((App.class.getResource("pop-up-reset-password.fxml")));
                Dialog<Objects> dialog = new Dialog<>();
                
                try {
                    DialogPane dialogPane = loader.load();
                    dialog.setDialogPane(dialogPane);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
                PopUpController popUpController = loader.getController();
                ButtonType confirmButtonType = new ButtonType("Confirm", ButtonData.YES);
                ButtonType cancelButtonType = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
                dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, cancelButtonType);
                
                final Button confirmButton = (Button) dialog.getDialogPane().lookupButton(confirmButtonType);
                confirmButton.addEventFilter(ActionEvent.ACTION, event -> {
                    String email = popUpController.getEmailField();
                    if (email.isEmpty()) {
                        new Alert(AlertType.ERROR, "The field must not be empty!").showAndWait();
                        event.consume();
                    } else if (currentUser.resetPassword(email)) {
                        new Alert(AlertType.INFORMATION, "Password reset success!").showAndWait();
                    } else {
                        event.consume();
                    }
        
                    popUpController.clearAllFields();
                });
        
                dialog.showAndWait();
                dialog.close();
            }
        });
    }

    private boolean verifyLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty()) {
            new Alert(AlertType.ERROR, "Username is required!").showAndWait();
            return false;
        }

        if (password.isEmpty()) {
            new Alert(AlertType.ERROR, "Password is required!").showAndWait();
            return false;
        }

        return currentUser.loginSession(username, password);
    }
}
