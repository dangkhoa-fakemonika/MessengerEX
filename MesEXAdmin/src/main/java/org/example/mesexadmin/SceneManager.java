package org.example.mesexadmin;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {
    
    private Map<String, Scene> sceneMap = new HashMap<>();
    private Stage stage;

    public SceneManager(Stage primarStage) {
        this.stage = primarStage;
    }

    public void addScene(String sceneName, String fxml) throws IOException {
        if (!sceneMap.containsKey(sceneName)) {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxml)));
            sceneMap.put(sceneName, new Scene(root));
        }
    }

    public void switchScene(String sceneName) {
        Scene scene = sceneMap.get(sceneName);
        if (scene != null) {
            stage.setScene(scene);
            stage.show();
        }
    }
}