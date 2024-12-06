package org.example.mesexadmin.ui.user_level;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;

import org.example.mesexadmin.Main;
import org.example.mesexadmin.SceneManager;

public class LoginController {
    private SceneManager sceneManager;

    @FXML private Button loginButton;
    @FXML private Button switchToRegisterButton;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    public LoginController() {
        sceneManager = Main.getSceneManager();
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
    }

    private void clearField() {
        usernameField.clear();
        passwordField.clear();
    }

    // public void loginScene(ActionEvent actionEvent) throws IOException {
    //     // root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main-login.fxml")));
    //     // bufferScene(actionEvent);
    //     sceneManager.addScene("Login", "main-login.fxml");
    //     sceneManager.switchScene("Login");
    // }

    // public void mainScene(ActionEvent actionEvent) throws IOException {
    //     // root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main-messaging.fxml")));
    //     // bufferScene(actionEvent);
    //     sceneManager.addScene("Main", "main-messaging.fxml");
    //     sceneManager.switchScene("Main");
    // }

}