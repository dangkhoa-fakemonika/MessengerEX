package org.example.mesexadmin.ui.user_level;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.example.mesexadmin.Main;
import org.example.mesexadmin.PopUpController;
import org.example.mesexadmin.SceneManager;
import org.example.mesexadmin.SessionUser;
import org.example.mesexadmin.data_class.ConversationData;
import org.example.mesexadmin.data_class.MessageData;
import org.example.mesexadmin.ui.ControllerWrapper;
import org.example.mesexadmin.ui.elements.ConversationListComponent;
import org.example.mesexadmin.ui.elements.MessageListComponent;
import javafx.scene.control.ButtonBar.ButtonData;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class MessagingController implements ControllerWrapper {
    private SceneManager sceneManager;
    private SessionUser currentUser;
    static ConversationData currentConversation;

    @FXML private ListView<ConversationListComponent> privateList;
    @FXML private ListView<ConversationListComponent> groupList;
    @FXML private ListView<MessageListComponent> messages;
    @FXML private Label myLabel;
    @FXML private TextArea myTextArea;
    @FXML private MenuButton optionButton;
    @FXML private MenuItem addFriendButton;
    @FXML private MenuItem logoutButton;
    @FXML private MenuItem addPrivateChat;
    @FXML private Button sendButton;

    // Load from database
    static ObservableList<ConversationListComponent> privateItems;
    static ObservableList<ConversationListComponent> groupItems;
    static ObservableList<MessageListComponent> messagesList;

    public void addMessage(ActionEvent actionEvent){
        if (!myTextArea.getText().trim().isEmpty()){
            String msg = myTextArea.getText().trim();

            // Add Message processing here
            boolean res = currentUser.sendMessage(msg, currentConversation.getConversationId());
            if (res) System.out.println("Message sent");

//            messages.getItems().add(new MessageListComponent(new MessageData(msg, "sender_1", "rec_1")));
            myTextArea.setText("");
        }
    }

    public void addMessage(){
        if (!myTextArea.getText().trim().isEmpty()){
            String msg = myTextArea.getText().trim();

            // Add Message processing here
            boolean res = currentUser.sendMessage(msg, currentConversation.getConversationId());
            if (res) System.out.println("Message sent");

//            messages.getItems().add(new MessageListComponent(new MessageData(msg, "sender_1", "rec_1")));
            myTextArea.setText("");
        }
    }

    public void addGroup(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("pop-up-create-group.fxml"));
        Dialog<Objects> dialog = new Dialog<>();
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        DialogPane dialogPane = loader.load();
        PopUpController popUpController = loader.getController();
        popUpController.currentDialog = dialog;
        dialog.setDialogPane(dialogPane);
        dialog.showAndWait();
        dialog.close();
    }

    public void reportUser(ActionEvent actionEvent) {
        Alert newAlert = new Alert(Alert.AlertType.CONFIRMATION);
        newAlert.setContentText("Report this user?");
        newAlert.setHeaderText("Spam Report");
        newAlert.showAndWait();
    }

    void chatSelection(){
        ArrayList<MessageData> messageQuery = currentUser.myQuery.messages().lookUpByConv(currentConversation.getConversationId());
        messagesList = FXCollections.observableArrayList();
        for (MessageData mQuery : messageQuery){
            messagesList.add(new MessageListComponent(mQuery));
        }

        messages.getItems().clear();
        messages.getItems().addAll(messagesList);
    }

    void refresh(){
        ArrayList<ConversationData> privateQuery = currentUser.loadPrivateConversations();
        privateItems = FXCollections.observableArrayList();
        for (ConversationData cQuery : privateQuery){
            privateItems.add(new ConversationListComponent(cQuery));
        }

        privateList.getItems().clear();
        privateList.getItems().addAll(privateItems);

        ArrayList<ConversationData> groupQuery = currentUser.loadGroupConversations();
        groupItems = FXCollections.observableArrayList();
        for (ConversationData cQuery : groupQuery){
            groupItems.add(new ConversationListComponent(cQuery));
        }

        groupList.getItems().clear();
        groupList.getItems().addAll(groupItems);

        groupList.refresh();
        privateList.refresh();
    }

    
    @Override
    public void myInitialize() {
        currentUser = Main.getCurrentUser();
        optionButton.setDisable(true);
        sendButton.setDisable(true);
        myTextArea.setDisable(true);

        currentUser = Main.getCurrentUser();
        refresh();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sceneManager = Main.getSceneManager();

        privateList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ConversationListComponent>() {
            @Override
            public void changed(ObservableValue<? extends ConversationListComponent> observableValue, ConversationListComponent conversationListComponent, ConversationListComponent t1) {
                ConversationListComponent c =  privateList.getSelectionModel().getSelectedItem();
                if (c != null){
                    currentConversation = c.getConversation();
                    myLabel.setText("Selected Chat: " + currentConversation.getMembersName().toString());
                    myTextArea.setDisable(false);
                    sendButton.setDisable(false);
                    chatSelection();

                    optionButton.setDisable(false);
                }
            }
        });

        groupList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ConversationListComponent>() {
            @Override
            public void changed(ObservableValue<? extends ConversationListComponent> observableValue, ConversationListComponent conversationListComponent, ConversationListComponent t1) {
                ConversationListComponent c =  groupList.getSelectionModel().getSelectedItem();
                if (c != null){
                    currentConversation = c.getConversation();
                    myLabel.setText("Selected Chat: " + currentConversation.getConversationName());
                    chatSelection();
                    myTextArea.setDisable(false);
                    sendButton.setDisable(false);
                    optionButton.setDisable(false);
                }
            }
        });


        myTextArea.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER)  {
                    if (keyEvent.isShiftDown()){
                        myTextArea.appendText("\n");
                    }
                    else {
                        addMessage();
                    }
                }
            }
        });

        addFriendButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                FXMLLoader loader = new FXMLLoader((Main.class.getResource("pop-up-add.fxml")));
                Dialog<Objects> dialog = new Dialog<>();
                
                try {
                    DialogPane dialogPane = loader.load();
                    dialog.setDialogPane(dialogPane);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
                PopUpController popUpController = loader.getController();
                ButtonType confirmButtonType = new ButtonType("Confirm", ButtonData.YES);
                ButtonType cancelButtonType = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
                dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, cancelButtonType);

                final Button confirmButton = (Button) dialog.getDialogPane().lookupButton(confirmButtonType);
                confirmButton.addEventFilter(ActionEvent.ACTION, event -> {
                    String username = popUpController.getUsernameField();
                    if (username.isEmpty()) {
                        new Alert(AlertType.ERROR, "The field must not be empty!").showAndWait();
                        event.consume();
                    } else if (currentUser.sendFriendRequest(username)) {
                        new Alert(AlertType.INFORMATION, "Friend request sent!").showAndWait();
                        event.consume();
                    } else {
                        event.consume();
                    }
        
                    popUpController.clearAllFields();
                });
        
                dialog.showAndWait();
                dialog.close();
            }
        });

        logoutButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setHeaderText("Do you want to logout?");
                alert.setTitle("Confirm Logout");

                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        if (currentUser.logoutSession()) {
                            try {
                                sceneManager.addScene("Login", "main-login.fxml");
                                sceneManager.switchScene("Login");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        });

        addPrivateChat.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                FXMLLoader loader = new FXMLLoader((Main.class.getResource("pop-up-create-private.fxml")));
                Dialog<Objects> dialog = new Dialog<>();

                try {
                    DialogPane dialogPane = loader.load();
                    dialog.setDialogPane(dialogPane);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                PopUpController popUpController = loader.getController();
                ButtonType confirmButtonType = new ButtonType("Confirm", ButtonData.YES);
                ButtonType cancelButtonType = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
                dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, cancelButtonType);

                final Button confirmButton = (Button) dialog.getDialogPane().lookupButton(confirmButtonType);
                confirmButton.addEventFilter(ActionEvent.ACTION, event -> {
                    String username = popUpController.getUsernameField();
                    if (username.isEmpty()) {
                        new Alert(AlertType.ERROR, "The field must not be empty!").showAndWait();
                        event.consume();
                    } else if (currentUser.createPrivateConversation(username)) {
                        new Alert(AlertType.INFORMATION, "Private conversation created!").showAndWait();
                        event.consume();
                    } else {
                        new Alert(AlertType.ERROR, "Can't create conversation").showAndWait();
                        event.consume();
                    }

                    popUpController.clearAllFields();
                });

                dialog.showAndWait();
                dialog.close();
            }
        });
    }

    public void advanced(ActionEvent actionEvent) throws IOException{
        sceneManager.addScene("ChatHistoryManagement", "main-all-chat-history-management.fxml");
        sceneManager.switchScene("ChatHistoryManagement");
    }

    public void friendsSettingScene(ActionEvent actionEvent) throws IOException{
        sceneManager.addScene("FriendsManagement", "main-friend-config.fxml");
        sceneManager.switchScene("FriendsManagement");
    }

    public void userManagementScene(ActionEvent actionEvent) throws IOException{
        sceneManager.addScene("AppUserManagement", "main-user-manager.fxml");
        sceneManager.switchScene("AppUserManagement");
    }

    public void groupManagementScene(ActionEvent actionEvent) throws IOException{
        sceneManager.addScene("AppGroupManagement", "main-group-manager.fxml");
        sceneManager.switchScene("AppGroupManagement");
    }

    public void appManagementScene(ActionEvent actionEvent) throws IOException{
        sceneManager.addScene("AppManagement", "main-app-manager.fxml");
        sceneManager.switchScene("AppManagement");
    }

    public void personalGroupManagementScene(ActionEvent actionEvent) throws IOException{
        sceneManager.addScene("MyGroupManagement", "main-convo-config.fxml");
        sceneManager.switchScene("MyGroupManagement");
    }

    public void profileScene(ActionEvent actionEvent) throws IOException{
        sceneManager.addScene("EditProfile", "edit-user-profile.fxml");
        sceneManager.switchScene("EditProfile");
    }

    public void configureChatHistory(ActionEvent actionEvent) throws IOException {
        sceneManager.addScene("ChatManagement", "main-chat-history-management.fxml");
        sceneManager.switchScene("ChatManagement");
    }

    public void returnLogin(ActionEvent actionEvent) throws IOException {
        sceneManager.addScene("Login", "main-login.fxml");
        sceneManager.switchScene("Login");
    }

    public void configureGroup(ActionEvent actionEvent) throws IOException {
        sceneManager.addScene("ThisGroupManager", "main-single-group-manager.fxml");
        sceneManager.switchScene("ThisGroupManager");
    }

    public void blockUser(ActionEvent actionEvent) {
        Alert newAlert = new Alert(Alert.AlertType.CONFIRMATION);
        newAlert.setContentText("Block this User?");
        newAlert.setHeaderText("Block User");
        newAlert.showAndWait();
    }

}
