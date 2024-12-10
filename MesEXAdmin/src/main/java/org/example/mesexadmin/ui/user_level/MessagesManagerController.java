package org.example.mesexadmin.ui.user_level;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.example.mesexadmin.Main;
import org.example.mesexadmin.SceneManager;
import org.example.mesexadmin.data_class.ConversationData;
import org.example.mesexadmin.data_class.MessageData;
import org.example.mesexadmin.ui.ControllerWrapper;
import org.example.mesexadmin.ui.elements.MessageListComponent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MessagesManagerController implements ControllerWrapper {
    SceneManager sceneManager;

    ConversationData thisConversation;
    MessageData selectedMessage;

    @FXML
    private ListView<MessageListComponent> chat;
    @FXML
    private Button reportUserButton;
    @FXML
    private Button deleteOneButton;
    @FXML
    private Button deleteAllButton;
    @FXML
    private TextField filterField;
    @FXML
    private Label title;

    static ObservableList<MessageListComponent> observableList3;

    public void returnToMain(ActionEvent actionEvent) throws IOException {
        sceneManager.addScene("Main", "main-messaging.fxml");
        sceneManager.switchScene("Main");
    }

    public void reportUser(ActionEvent actionEvent) {
        Alert newAlert = new Alert(Alert.AlertType.CONFIRMATION);
        newAlert.setContentText("Report this user?");
        newAlert.setHeaderText("Spam Report");
        newAlert.showAndWait();
    }

    public void deleteAllUser(ActionEvent actionEvent) {
        Alert newAlert = new Alert(Alert.AlertType.CONFIRMATION);
        newAlert.setContentText("Delete all messages");
        newAlert.setHeaderText("Delete all messages in the chat?");
        newAlert.showAndWait();
    }

//    public void

    @Override
    public void myInitialize() {
        deleteOneButton.setDisable(true);
        thisConversation = MessagingController.currentConversation;
        title.setText(thisConversation.getConversationName());
        selectedMessage = null;

        observableList3 = FXCollections.observableArrayList(
                        new MessageListComponent(new MessageData("sample message 1", "a", "b")),
                        new MessageListComponent(new MessageData("sample message 3", "a", "b")),
                        new MessageListComponent(new MessageData("sample message 4", "a", "b")),
                        new MessageListComponent(new MessageData("sample message 6", "a", "b"))
                );

        chat.getItems().clear();
        chat.getItems().addAll(observableList3);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sceneManager = Main.getSceneManager();

        chat.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MessageListComponent>() {
                @Override
                public void changed(ObservableValue<? extends MessageListComponent> observableValue, MessageListComponent messageListComponent, MessageListComponent t1) {
                    selectedMessage = chat.getSelectionModel().getSelectedItem().getMessage();
                    deleteOneButton.setDisable(false);
                }
            }
        );
    }
}
