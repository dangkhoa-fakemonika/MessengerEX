package org.example.mesexadmin.ui.user_level;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import org.example.mesexadmin.Main;
import org.example.mesexadmin.SceneManager;
import org.example.mesexadmin.ui.ControllerWrapper;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ChangePasswordController implements ControllerWrapper {
    static SceneManager sceneManager;

    public void returnToMain(ActionEvent actionEvent) throws IOException {
        sceneManager.addScene("Main", "main-messaging.fxml");
        sceneManager.switchScene("Main");
    }

    public void returnToChange(ActionEvent actionEvent) throws IOException {
        sceneManager.addScene("EditProfile", "edit-user-profile.fxml");
        sceneManager.switchScene("EditProfile");
    }

    @Override
    public void myInitialize() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sceneManager = Main.getSceneManager();
    }
}
