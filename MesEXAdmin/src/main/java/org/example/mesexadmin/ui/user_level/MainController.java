package org.example.mesexadmin.ui.user_level;

import javafx.event.ActionEvent;
import org.example.mesexadmin.Main;
import org.example.mesexadmin.SceneManager;

import java.io.IOException;

public class MainController {
    private SceneManager sceneManager;

    public MainController() {
        sceneManager = Main.getSceneManager();
    }


    public void registerScene(ActionEvent actionEvent) throws IOException {
        // root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main-register.fxml")));
        // bufferScene(actionEvent);
        sceneManager.addScene("Register", "main-register.fxml");
        sceneManager.switchScene("Register");
    }

    public void loginScene(ActionEvent actionEvent) throws IOException {
        // root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main-login.fxml")));
        // bufferScene(actionEvent);
        sceneManager.addScene("Login", "main-login.fxml");
        sceneManager.switchScene("Login");
    }

    public void mainScene(ActionEvent actionEvent) throws IOException {
        // root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main-messaging.fxml")));
        // bufferScene(actionEvent);
        sceneManager.addScene("Main", "main-messaging.fxml");
        sceneManager.switchScene("Main");
    }

}
