package org.example.mesexadmin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainController {
    // private Stage stage;
    // private Scene scene;
    // private Parent root;
    private SceneManager sceneManager;

    public MainController() {
        sceneManager = Main.getSceneManager();
    }

//     void bufferScene(ActionEvent actionEvent){
// //        System.out.println(actionEvent.getSource());
//         stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
//         scene = new Scene(root);
//         stage.setScene(scene);
//         stage.show();
//     }

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
