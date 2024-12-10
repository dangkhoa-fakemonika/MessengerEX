package org.example.mesexadmin.ui.user_level;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;

import org.example.mesexadmin.Main;
import org.example.mesexadmin.SceneManager;
import org.example.mesexadmin.ui.ControllerWrapper;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements ControllerWrapper {
    private SceneManager sceneManager;

    @FXML private Button loginButton;
    @FXML private Button switchToRegisterButton;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

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
    }
}
