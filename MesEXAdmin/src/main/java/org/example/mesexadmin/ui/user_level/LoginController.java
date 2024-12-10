package org.example.mesexadmin.ui.user_level;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

import org.example.mesexadmin.Main;
import org.example.mesexadmin.PopUpController;
import org.example.mesexadmin.SceneManager;
import org.example.mesexadmin.ui.ControllerWrapper;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController implements ControllerWrapper {
    private SceneManager sceneManager;

    @FXML private Button loginButton;
    @FXML private Button switchToRegisterButton;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button forgotAccount;

    public void registerScene(ActionEvent actionEvent) throws IOException {
        sceneManager.addScene("Register", "main-register.fxml");
        sceneManager.switchScene("Register");
    }

    public void mainScene(ActionEvent actionEvent) throws IOException {
        sceneManager.addScene("Main", "main-messaging.fxml");
        sceneManager.switchScene("Main");
    }

    private void clearField() {
        usernameField.clear();
        passwordField.clear();
    }

    @Override
    public void myInitialize() {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sceneManager = Main.getSceneManager();

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
                try {
                    clearField();
                    mainScene(actionEvent);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        forgotAccount.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    FXMLLoader loader = new FXMLLoader(Main.class.getResource("pop-up-forgot-password.fxml"));
                    Dialog<Objects> dialog = new Dialog<>();
                    dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
                    DialogPane dialogPane = loader.load();
                    PopUpController popUpController = loader.getController();
                    popUpController.currentDialog = dialog;
                    dialog.setDialogPane(dialogPane);
                    dialog.showAndWait();
                    dialog.close();
                } catch (Exception e){

                }
            }
        });
    }
}
