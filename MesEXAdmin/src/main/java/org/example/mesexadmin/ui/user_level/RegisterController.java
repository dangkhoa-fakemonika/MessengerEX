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

public class RegisterController {
    private SceneManager sceneManager;

    @FXML private Button registerButton;
    @FXML private Button switchToLoginButton;
    @FXML private TextField usernameField;
    @FXML private TextField emaiField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;

    public RegisterController() {
        sceneManager = Main.getSceneManager();
    }

    public void loginScene(ActionEvent actionEvent) throws IOException {
        // root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main-login.fxml")));
        // bufferScene(actionEvent);
        sceneManager.addScene("Login", "main-login.fxml");
        sceneManager.switchScene("Login");
    }

    public void verifyRegister(ActionEvent actionEvent) throws IOException {

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
        switchToLoginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    clearField();
                    sceneManager.addScene("Login", "main-login.fxml");
                    sceneManager.switchScene("Login");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void clearField() {
        usernameField.clear();
        emaiField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
    }
}
