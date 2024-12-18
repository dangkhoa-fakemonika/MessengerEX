package org.example.mesexadmin;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import org.example.mesexadmin.data_access.GlobalQuery;

public class Main extends Application {
    public static GlobalQuery globalQuery;
    private static SceneManager sceneManager;
    private static SessionUser currentUser;

    @Override
    public void start(Stage primaryStage) throws Exception {

        sceneManager = new SceneManager(primaryStage);
        sceneManager.addScene("Login", "main-login.fxml");
        sceneManager.switchScene("Login");
//        sceneManager.addScene("Main", "main-messaging.fxml");
//        sceneManager.switchScene("Main");
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
        globalQuery = new GlobalQuery(new MongoManagement(""));
        currentUser = new SessionUser(globalQuery);
        launch(args);
    }

    public static SceneManager getSceneManager() {
        return sceneManager;
    }
    public static SessionUser getCurrentUser() {
        currentUser.updateCurrentUserData();
        return currentUser;
    }
}
