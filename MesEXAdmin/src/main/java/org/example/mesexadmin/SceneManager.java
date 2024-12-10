package org.example.mesexadmin;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.mesexadmin.ui.ControllerWrapper;

public class SceneManager {
    
    private final Map<String, Scene> sceneMap = new HashMap<>();
    private final Map<String, FXMLLoader> loaderMap = new HashMap<>();
    private final Stage stage;

    public SceneManager(Stage primarStage) {
        this.stage = primarStage;
    }

    public void addScene(String sceneName, String fxml) throws IOException {
        if (!sceneMap.containsKey(sceneName)) {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(fxml)));
            Parent root = loader.load();
            System.out.println(root.getUserData());
            sceneMap.put(sceneName, new Scene(root));
            loaderMap.put(sceneName, loader);
        }
    }

    public void switchScene(String sceneName) {
        Scene scene = sceneMap.get(sceneName);
        if (scene != null) {
            try {
                FXMLLoader loader = loaderMap.get(sceneName);
                ((ControllerWrapper) loader.getController()).myInitialize();
            }
            catch (Exception e){
                System.out.println("super idol dexiaozong");
            }
            stage.setScene(scene);
            stage.show();
        }
    }
}