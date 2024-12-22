package org.example.mesex.ui.user_level;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import org.example.mesex.App;
import org.example.mesex.SceneManager;
import org.example.mesex.ui.ControllerWrapper;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RegisterController implements ControllerWrapper {
    private SceneManager sceneManager;

    @FXML private Button registerButton;
    @FXML private Button switchToLoginButton;
    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;

    public RegisterController() {
        sceneManager = App.getSceneManager();
    }

    public void loginScene(ActionEvent actionEvent) throws IOException {
        sceneManager.addScene("Login", "main-login.fxml");
        sceneManager.switchScene("Login");
    }

    public void mainScene(ActionEvent actionEvent) throws IOException {
        sceneManager.addScene("Main", "main-messaging.fxml");
        sceneManager.switchScene("Main");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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

        registerButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (verifyRegister()) {
                    Dialog<String> dialog = new Dialog<>();
                    dialog.setTitle("Success");
                    dialog.setContentText("Register successfully!");
                    dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
                    dialog.showAndWait();
                    
                    try {
                        clearField();
                        sceneManager.addScene("Login", "main-login.fxml");
                        sceneManager.switchScene("Login");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private boolean verifyRegister() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (username.isEmpty()) {
            new Alert(AlertType.ERROR, "Username is required!").showAndWait();
            return false;
        }

        if (username.length() < 5) {
            new Alert(AlertType.ERROR, "Username can not be too short (at least 5 characters)").showAndWait();
            return false;
        }

        if (email.isEmpty()) {
            new Alert(AlertType.ERROR, "Email is required!").showAndWait();
            return false;
        }

        if (password.isEmpty()) {
            new Alert(AlertType.ERROR, "Password is required!").showAndWait();
            return false;
        }

        if (confirmPassword.isEmpty()) {
            new Alert(AlertType.ERROR, "Confirm password is required!").showAndWait();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            new Alert(AlertType.ERROR, "Password and confirm password are not matched!").showAndWait();
            return false;
        }

        if (!email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
            new Alert(AlertType.ERROR, "Email invalid!").showAndWait();
            return false;
        }

        return App.getCurrentUser().registerUser(username, email, password);
    }

    private void clearField() {
        usernameField.clear();
        emailField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
    }

    @Override
    public void myInitialize() {

    }
}
