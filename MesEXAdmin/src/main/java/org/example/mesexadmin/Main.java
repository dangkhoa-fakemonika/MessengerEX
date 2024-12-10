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
