package org.example.mesexadmin.ui.user_level;

import javafx.animation.PauseTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;
import org.example.mesexadmin.App;
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

public class MessagesManagerController implements ControllerWrapper {
    static SceneManager sceneManager;
    static SessionUser currentUser;

    static ConversationData thisConversation;
    static MessageData selectedMessage;

    PauseTransition fieldPause;

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
    private Button jumpButton;
    @FXML
    private Label title;

    static ObservableList<MessageListComponent> loadedMessages;

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

    public void deleteAllMessages(ActionEvent actionEvent) {
        Alert newAlert = new Alert(Alert.AlertType.CONFIRMATION);
        newAlert.setContentText("Delete all messages");
        newAlert.setHeaderText("Delete all messages in the chat (Yours only)?");
        newAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (currentUser.myQuery.messages().removeAllByGroupWithUser(thisConversation.getConversationId(), currentUser.getSessionUserData().getUserId())){
                    ArrayList<MessageData> messageQuery = currentUser.myQuery.messages().lookUpByConv(thisConversation.getConversationId());
                    loadedMessages = FXCollections.observableArrayList();
                    for (MessageData mQuery : messageQuery){
                        loadedMessages.add(new MessageListComponent(mQuery));
                    }

                    chat.getItems().clear();
                    chat.getItems().addAll(loadedMessages);
                    chat.scrollTo(loadedMessages.size());
                    chat.refresh();
                }
            }
        });
    }

    public void deleteOneMessage(ActionEvent actionEvent) {
        Alert newAlert = new Alert(Alert.AlertType.CONFIRMATION);
        newAlert.setContentText("Delete this");
        newAlert.setHeaderText("");
        newAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (currentUser.myQuery.messages().remove(selectedMessage.getMessageId())){
                    ArrayList<MessageData> messageQuery = currentUser.myQuery.messages().lookUpByConv(thisConversation.getConversationId());
                    loadedMessages = FXCollections.observableArrayList();
                    for (MessageData mQuery : messageQuery){
                        loadedMessages.add(new MessageListComponent(mQuery));
                    }

                    chat.getItems().clear();
                    chat.getItems().addAll(loadedMessages);
                    chat.scrollTo(loadedMessages.size());
                    chat.refresh();
                }
            }
        });
    }

    public void updateMessages(){
        ArrayList<MessageData> messageQuery;
        String token = filterField.getText().trim();
        if (!token.isEmpty())
            messageQuery = currentUser.myQuery.messages().lookUpByConvFilter(thisConversation.getConversationId(), token);
        else messageQuery = currentUser.myQuery.messages().lookUpByConv(thisConversation.getConversationId());

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
        MessagingController.findMessage(thisConversation, selectedMessage);
        sceneManager.addScene("Main", "main-messaging.fxml");
        sceneManager.switchScene("Main");
    }

    @Override
    public void myInitialize() {
        currentUser = App.getCurrentUser();
        deleteOneButton.setDisable(true);
        jumpButton.setDisable(true);
        thisConversation = MessagingController.currentConversation;
        title.setText(thisConversation.getConversationName());
        selectedMessage = null;

        ArrayList<MessageData> messageQuery = currentUser.myQuery.messages().lookUpByConv(thisConversation.getConversationId());
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
        sceneManager = App.getSceneManager();
        fieldPause = new PauseTransition(Duration.millis(500));
        fieldPause.setOnFinished((e) -> updateMessages());

        chat.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MessageListComponent>() {
                @Override
                public void changed(ObservableValue<? extends MessageListComponent> observableValue, MessageListComponent messageListComponent, MessageListComponent t1) {
                    MessageListComponent sM = chat.getSelectionModel().getSelectedItem();
                    if (sM != null){
                        selectedMessage = sM.getMessage();
                        System.out.println(selectedMessage.getContent());
                        deleteOneButton.setDisable(!sM.getMessage().getSenderId().equals(currentUser.getSessionUserData().getUserId()));
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
