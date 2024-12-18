package org.example.mesexadmin.ui.user_level;

import javafx.animation.PauseTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;
import org.example.mesexadmin.Main;
import org.example.mesexadmin.SceneManager;
import org.example.mesexadmin.SessionUser;
import org.example.mesexadmin.data_class.ConversationData;
import org.example.mesexadmin.data_class.MessageData;
import org.example.mesexadmin.ui.ControllerWrapper;
import org.example.mesexadmin.ui.elements.MessageListComponent;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AllMessagesManagerController implements ControllerWrapper {
    static SceneManager sceneManager;
    static SessionUser currentUser;

    static MessageData selectedMessage;

    PauseTransition fieldPause;

    @FXML
    private ListView<MessageListComponent> chat;
    @FXML
    private TextField filterField;
    @FXML
    private Button jumpButton;

    static ObservableList<MessageListComponent> loadedMessages;

    public void returnToMain(ActionEvent actionEvent) throws IOException {
        sceneManager.addScene("Main", "main-messaging.fxml");
        sceneManager.switchScene("Main");
    }

    public void updateMessages(){
        ArrayList<MessageData> messageQuery;
        String token = filterField.getText().trim();
        if (!token.isEmpty())
            messageQuery = currentUser.myQuery.messages().lookUpByUserFilter(currentUser.getSessionUserData().getUserId(), token);
        else messageQuery = currentUser.myQuery.messages().lookUpByUser(currentUser.getSessionUserData().getUserId());

        loadedMessages = FXCollections.observableArrayList();
        for (MessageData mQuery : messageQuery){
            loadedMessages.add(new MessageListComponent(mQuery));
        }

        chat.getItems().clear();
        chat.getItems().addAll(loadedMessages);
        chat.scrollTo(loadedMessages.size());
        chat.refresh();
    }

    public void jumpTo() throws IOException {
        ConversationData findConversation = currentUser.myQuery.conversations().getConversation(selectedMessage.getConversationId());
        findConversation.getMembersId().forEach((id) ->
                findConversation.getMembersName().add(new SimpleStringProperty(currentUser.myQuery.users().getUserById(id).getUsername())));
        findConversation.getModeratorsId().forEach((id) ->
                findConversation.getModeratorsName().add(new SimpleStringProperty(currentUser.myQuery.users().getUserById(id).getUsername())));
        MessagingController.findMessage(findConversation, selectedMessage);
        sceneManager.addScene("Main", "main-messaging.fxml");
        sceneManager.switchScene("Main");
    }

    @Override
    public void myInitialize() {
        jumpButton.setDisable(true);
        selectedMessage = null;
        currentUser = Main.getCurrentUser();
        filterField.clear();

        ArrayList<MessageData> messageQuery = currentUser.myQuery.messages().lookUpByUser(currentUser.getSessionUserData().getUserId());
        loadedMessages = FXCollections.observableArrayList();
        for (MessageData mQuery : messageQuery){
            loadedMessages.add(new MessageListComponent(mQuery));
        }

        chat.getItems().clear();
        chat.getItems().addAll(loadedMessages);
        chat.scrollTo(loadedMessages.size());
        chat.refresh();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sceneManager = Main.getSceneManager();
        fieldPause = new PauseTransition(Duration.millis(500));
        fieldPause.setOnFinished((e) -> updateMessages());

        chat.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MessageListComponent>() {
                @Override
                public void changed(ObservableValue<? extends MessageListComponent> observableValue, MessageListComponent messageListComponent, MessageListComponent t1) {
                    MessageListComponent sM = chat.getSelectionModel().getSelectedItem();
                    if (sM != null){
                        selectedMessage = sM.getMessage();
                        System.out.println(selectedMessage.getContent());
                        jumpButton.setDisable(false);
                    }
                }
            }
        );

        jumpButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    jumpTo();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        filterField.textProperty().addListener(((observableValue, s, t1) -> {
            fieldPause.stop();
            fieldPause.playFromStart();
        }));
    }
}
