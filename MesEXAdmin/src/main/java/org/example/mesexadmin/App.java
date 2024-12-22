package org.example.mesexadmin;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.example.mesexadmin.data_access.GlobalQuery;

public class App extends Application {
    public static GlobalQuery globalQuery;
    public static Properties appProperties;
    private static SceneManager sceneManager;
    private static SessionUser currentUser;

    @Override
    public void init() {
        try {
            InputStream input = null;
            appProperties = new Properties();
            input = new FileInputStream("app.properties");
            appProperties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        globalQuery = new GlobalQuery(new MongoManagement());
        currentUser = new SessionUser(globalQuery);

        sceneManager = new SceneManager(primaryStage);
        sceneManager.addScene("Login", "main-login.fxml");
        sceneManager.switchScene("Login");

        primaryStage.setOnCloseRequest(event -> {
            event.consume();

            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirm Exit");
            alert.setHeaderText("Do you want to exit application?");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    if (currentUser.isLoggedIn()) {
                        currentUser.logoutSession();
                    }
                    primaryStage.close();
                }
            });
        });
    }

    public static void main(String[] args) {
        launch();
    }

    public static SceneManager getSceneManager() {
        return sceneManager;
    }
    public static SessionUser getCurrentUser() {
        // currentUser.updateCurrentUserData();
        return currentUser;
    }
}
