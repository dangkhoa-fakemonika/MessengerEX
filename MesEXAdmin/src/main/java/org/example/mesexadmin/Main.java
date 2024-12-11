package org.example.mesexadmin;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.mesexadmin.data_access.GlobalQuery;

public class Main extends Application {
    public static GlobalQuery globalQuery;
    private static SceneManager sceneManager;
    private static SessionUser thisUser;

    @Override
    public void start(Stage primaryStage) throws Exception {

        sceneManager = new SceneManager(primaryStage);
        sceneManager.addScene("Login", "main-messaging.fxml");
        sceneManager.switchScene("Login");
    }

    public static void main(String[] args) {
//        javafx.util.
        globalQuery = new GlobalQuery(new MongoManagement(""));
        thisUser = new SessionUser(globalQuery);
        launch(args);
    }

    public static SceneManager getSceneManager() {
        return sceneManager;
    }
    public static SessionUser getThisUser() { 
        return thisUser;
    }
}
