package org.example.mesexadmin;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {
    public static MongoManagement myMongo;
    private static SceneManager sceneManager;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main-login.fxml")));
        // Scene scene = new Scene(root);
        // Stage stage = new Stage();

        // primaryStage.setScene(scene);
        // primaryStage.setResizable(false);
        // primaryStage.show();

        sceneManager = new SceneManager(primaryStage);
        sceneManager.addScene("Login", "main-login.fxml");
        sceneManager.switchScene("Login");
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static SceneManager getSceneManager() {
        return sceneManager;
    }
}
