package org.example.mesexadmin.ui.user_level;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;

import org.example.mesexadmin.Main;
import org.example.mesexadmin.SceneManager;

import java.io.IOException;

public class LoginController {
    private SceneManager sceneManager;

    @FXML private Button loginButton;
    @FXML private Button switchToRegisterButton;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    public LoginController() {
        sceneManager = Main.getSceneManager();
    }


    public void registerScene(ActionEvent actionEvent) throws IOException {
        // root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main-register.fxml")));
        // bufferScene(actionEvent);
        sceneManager.addScene("Register", "main-register.fxml");
        sceneManager.switchScene("Register");
    }

    public void mainScene(ActionEvent actionEvent) throws IOException {
        // root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main-messaging.fxml")));
        // bufferScene(actionEvent);
        sceneManager.addScene("Main", "main-messaging.fxml");
        sceneManager.switchScene("Main");
    }

    @FXML
    public void initialize() {
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

    private void clearField() {
        usernameField.clear();
        passwordField.clear();
    }
}
