package org.example.mesexadmin.ui.user_level;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

import org.example.mesexadmin.Main;
import org.example.mesexadmin.PopUpController;
import org.example.mesexadmin.SceneManager;
import org.example.mesexadmin.SessionUser;
import org.example.mesexadmin.data_class.ConversationData;
import org.example.mesexadmin.data_class.MessageData;
import org.example.mesexadmin.data_class.UserData;
import org.example.mesexadmin.ui.ControllerWrapper;
import org.example.mesexadmin.ui.elements.ConversationListComponent;
import org.example.mesexadmin.ui.elements.MessageListComponent;
import org.example.mesexadmin.ui.elements.UserListComponent;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class MessagingController implements ControllerWrapper {
    private SceneManager sceneManager;
    private SessionUser currentUser;
    static ConversationData currentConversation;
    static UserData selectedUser;
    PauseTransition searchPause;

    @FXML private ListView<ConversationListComponent> privateList;
    @FXML private ListView<ConversationListComponent> groupList;
    @FXML private ListView<MessageListComponent> messages;
    @FXML private Label myLabel;
    @FXML private TextArea myTextArea;
    @FXML private MenuButton optionButton;
    @FXML private MenuItem addFriendButton;
    @FXML private MenuItem logoutButton;
    @FXML private MenuItem addPrivateChat;
    @FXML private MenuItem manageFriendButton;
    @FXML private MenuItem addGroupButton;
    @FXML private MenuItem manageGroupButton;
    @FXML private Button sendButton;
    @FXML private Tab privateTab;
    @FXML private Tab groupTab;
    @FXML private Tab everyoneTab;
    @FXML private TabPane messagesTabPane;
    @FXML private MenuItem seeMessagesButton;
    @FXML private MenuItem configureGroupButton;
    @FXML private MenuItem blockUserButton;
    @FXML private MenuItem reportUserButton;
    @FXML private TextField searchUserField;
    @FXML private ListView<UserListComponent> searchUserList;
    @FXML private Button addPrivateTargetButton;
    @FXML private Button addGroupTargetButton;

    @FXML private TextField searchPrivateConversation;
    @FXML private TextField searchGroupConversation;

    private ScheduledService<Void> updateChatList;
    private ScheduledService<Void> updateChat;
    private UserData currentChatTarget = null;
    private Tab currentTab;
    private boolean tabSwitched;
    private boolean chatSwitched;

    // Load from database
    static ObservableList<ConversationListComponent> privateItems = FXCollections.observableArrayList();
    static ObservableList<ConversationListComponent> groupItems = FXCollections.observableArrayList();
    static ObservableList<MessageListComponent> messagesList = FXCollections.observableArrayList();
    static ObservableList<UserListComponent> searchUserItems = FXCollections.observableArrayList();

    public static boolean jumpToMessage = false;
    static ConversationData jumpConversation = null;
    static MessageData jumpMessage = null;

    public void addMessage(ActionEvent actionEvent) {
        if (!myTextArea.getText().trim().isEmpty()) {
            String msg = myTextArea.getText().trim();

            if (currentUser.getBLockedStatus(currentChatTarget.getUserId())) {
                new Alert(AlertType.INFORMATION, "You have been blocked by this user").showAndWait();
                disableChat();
                return;
            }

            // Add Message processing here
            boolean res = currentUser.sendMessage(msg, currentConversation.getConversationId());
            if (res) System.out.println("Message sent");

            myTextArea.setText("");
        }
    }

    public static void findMessage(ConversationData conversationData, MessageData messageData){
        jumpMessage = messageData;
        jumpConversation = conversationData;
        jumpToMessage = true;
    }

    public void goToMessage() {
        ConversationListComponent item = new ConversationListComponent(jumpConversation);
        if (jumpConversation.getType().equals("private")) {
            messagesTabPane.getSelectionModel().clearAndSelect(0);
            privateList.scrollTo(item);
            privateList.getSelectionModel().select(item);
            privateList.getFocusModel().focus(privateItems.indexOf(item));
        }
        if (jumpConversation.getType().equals("group")) {
            messagesTabPane.getSelectionModel().clearAndSelect(1);
            groupList.scrollTo(item);
            groupList.getSelectionModel().select(item);
            groupList.getFocusModel().focus(groupItems.indexOf(item));
        }

        currentConversation = item.getConversation();
        chatSwitched = true;
        updateChat.restart();

        jumpToMessage = false;
        myTextArea.setDisable(false);
        sendButton.setDisable(false);
        optionButton.setDisable(false);

        MessageListComponent itemM = new MessageListComponent(jumpMessage);
        messages.scrollTo(itemM);
        int indexMessage = -1;
        for (int i = 0; i < messagesList.size(); i++){
            if (messagesList.get(i).getMessage().getMessageId().equals(jumpMessage.getMessageId())){
                indexMessage = i;
                break;
            }
        }

        messages.requestFocus();
        messages.getFocusModel().focus(indexMessage);
        messages.getSelectionModel().select(indexMessage);
    }

    public void addMessage() {
        if (!myTextArea.getText().trim().isEmpty()){
            String msg = myTextArea.getText().trim();

            // Add Message processing here
            boolean res = currentUser.sendMessage(msg, currentConversation.getConversationId());
            if (res) System.out.println("Message sent");

            // messages.getItems().add(new MessageListComponent(new MessageData(msg, "sender_1", "rec_1")));
            myTextArea.setText("");
            updateChat.restart();
        }
    }

    public void reportUser(ActionEvent actionEvent) {
        Alert newAlert = new Alert(Alert.AlertType.CONFIRMATION);
        newAlert.setContentText("Report this user?");
        newAlert.setHeaderText("Spam Report");
        newAlert.showAndWait();
    }

    private void updateCurrentChat() {

        if (currentChatTarget == null || currentUser.getBLockedStatus(currentChatTarget.getUserId())) {
            disableChat();
            return;
        } else {
            enableChat();
        }

        if (currentConversation == null) {
            messages.getItems().clear();
            return;
        }

        ArrayList<MessageData> messageQuery = currentUser.myQuery.messages().lookUpByConv(currentConversation.getConversationId());
        messagesList = FXCollections.observableArrayList();
        for (MessageData mQuery : messageQuery){
            messagesList.add(new MessageListComponent(mQuery));
        }

        // This works good
        if (chatSwitched || messagesList.size() < messages.getItems().size()) {
            messages.getItems().clear();
            messages.getItems().addAll(messagesList);
            messages.scrollTo(messages.getItems().size());
            chatSwitched = false;
            return;
        } else {
            messagesList.stream().filter(message -> !messages.getItems().contains(message)).forEach(messages.getItems()::add);
        }
    }

    private void updateCurrentChatList() {
        if (tabSwitched) {
            chatSwitched = true;
            tabSwitched = false;
        }

        if (currentTab == privateTab) {
            ArrayList<ConversationData> privateQuery = currentUser.loadPrivateConversations();
            privateItems = FXCollections.observableArrayList();

            for (ConversationData cQuery : privateQuery) {
                privateItems.add(new ConversationListComponent(cQuery));
            }

            privateList.getItems().clear();
            privateList.getItems().addAll(privateItems);

        } else if (currentTab == groupTab) {
            ArrayList<ConversationData> groupQuery = currentUser.loadGroupConversations();
            groupItems = FXCollections.observableArrayList();
            for (ConversationData cQuery : groupQuery) {
                groupItems.add(new ConversationListComponent(cQuery));
            }

            groupList.getItems().clear();
            groupList.getItems().addAll(groupItems);

        }

        updateChat.restart();
    }

    private void disableChat() {
        sendButton.setDisable(true);
        myTextArea.setDisable(true);
        myTextArea.setText("You can not chat with this user.");
    }

    private void enableChat() {
        sendButton.setDisable(false);
        myTextArea.setDisable(false);
        if (myTextArea.getText().equals("You can not chat with this user."))
            myTextArea.clear();
    }


    private void loadSearchResults(){
        selectedUser = null;
        addPrivateTargetButton.setDisable(true);
        addGroupTargetButton.setDisable(true);

        ArrayList<UserData> searchResults;

        String token = searchUserField.getText().trim();
        if (!token.isEmpty()){
            searchResults = currentUser.myQuery.users().getAllUsersNameFilter(token);
        }
        else {
            searchResults = new ArrayList<>();
        }

        searchUserItems.clear();
        searchResults.forEach((a) -> searchUserItems.add(new UserListComponent(a)));
        searchUserList.getItems().clear();
        searchUserList.getItems().addAll(searchUserItems);
        searchUserList.refresh();
    }

    
    // void refresh(){
    //     ArrayList<ConversationData> privateQuery = currentUser.loadPrivateConversations();
    //     privateItems = FXCollections.observableArrayList();
    //     for (ConversationData cQuery : privateQuery){
    //         privateItems.add(new ConversationListComponent(cQuery));
    //     }

    //     privateList.getItems().clear();
    //     privateList.getItems().addAll(privateItems);

    //     ArrayList<ConversationData> groupQuery = currentUser.loadGroupConversations();
    //     groupItems = FXCollections.observableArrayList();
    //     for (ConversationData cQuery : groupQuery){
    //         groupItems.add(new ConversationListComponent(cQuery));
    //     }

    //     groupList.getItems().clear();
    //     groupList.getItems().addAll(groupItems);

    //     groupList.refresh();
    //     privateList.refresh();
    //     messagesList = FXCollections.observableArrayList();
    //     messages.getItems().clear();
    //     messages.scrollTo(messagesList.size());
    //     messages.refresh();
    // }

    @Override
    public void myInitialize() {
        currentUser = Main.getCurrentUser();
        optionButton.setDisable(true);
        disableChat();
        myTextArea.clear();
        currentTab = privateTab;

        // Create service
        updateChat = new ScheduledService<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        Platform.runLater(() -> {
                            updateCurrentChat();
                        });
                        return null;
                    }
                };
            }
        };
        // updateChat.setPeriod(Duration.seconds(1));
        updateChat.setPeriod(Duration.millis(500));
        updateChat.start();

        updateChatList = new ScheduledService<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        Platform.runLater(() -> {
                            updateCurrentChatList();
                        });
                        return null;
                    }
                };
            }
        };
        updateChatList.setPeriod(Duration.seconds(60));
        updateChatList.start();

        currentUser = Main.getCurrentUser();
        // refresh();

        if (jumpToMessage) goToMessage();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sceneManager = Main.getSceneManager();
        searchPause = new PauseTransition(Duration.millis(500));
        searchPause.setOnFinished((e) -> loadSearchResults());

        privateList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ConversationListComponent>() {
            @Override
            public void changed(ObservableValue<? extends ConversationListComponent> observableValue, ConversationListComponent conversationListComponent, ConversationListComponent t1) {
                ConversationListComponent c =  privateList.getSelectionModel().getSelectedItem();
                if (c != null){
                    currentConversation = c.getConversation();

                    currentChatTarget = currentUser.getChatTarget(currentConversation);

                    myLabel.setText("Selected Chat: " + currentChatTarget.getUsername());
                    
                    chatSwitched = true;
                    updateCurrentChat();

                    if (currentUser.getBLockedStatus(currentChatTarget.getUserId()))
                        disableChat();
                    else
                        enableChat();

                    optionButton.setDisable(false);
                    configureGroupButton.setDisable(true);
                    configureGroupButton.setVisible(false);
                    blockUserButton.setDisable(false);
                    blockUserButton.setVisible(true);
                    reportUserButton.setDisable(false);
                    reportUserButton.setVisible(true);
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

                    chatSwitched = true;
                    updateCurrentChat();

                    myTextArea.setDisable(false);
                    sendButton.setDisable(false);
                    optionButton.setDisable(false);
                    configureGroupButton.setDisable(false);
                    configureGroupButton.setVisible(true);
                    blockUserButton.setDisable(true);
                    blockUserButton.setVisible(false);
                    reportUserButton.setDisable(true);
                    reportUserButton.setVisible(false);
                }
            }
        });

        searchUserList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<UserListComponent>() {
            @Override
            public void changed(ObservableValue<? extends UserListComponent> observableValue, UserListComponent userListComponent, UserListComponent t1) {
                UserListComponent u = searchUserList.getSelectionModel().getSelectedItem();
                if (u != null && !u.getUser().getUserId().equals(currentUser.getSessionUserData().getUserId())){
                    selectedUser = u.getUser();
                    addPrivateTargetButton.setDisable(false);
                    addGroupTargetButton.setDisable(false);
                } else {
                    addPrivateTargetButton.setDisable(true);
                    addGroupTargetButton.setDisable(true);
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
                        if (currentUser.getBLockedStatus(currentChatTarget.getUserId())) {
                            new Alert(AlertType.INFORMATION, "You have been blocked by this user").showAndWait();
                            disableChat();
                            return;
                        }
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

        addGroupButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                FXMLLoader loader = new FXMLLoader((Main.class.getResource("pop-up-create-group.fxml")));
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
                    String groupName = popUpController.getGroupNameField();
                    if (username.isEmpty() || groupName.trim().isEmpty()) {
                        new Alert(AlertType.ERROR, "The fields must not be empty!").showAndWait();
                        event.consume();
                    } else if (currentUser.createGroup(groupName, username)) {
                        new Alert(AlertType.INFORMATION, "Group created!").showAndWait();
                        updateChatList.restart();
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

        manageFriendButton.setOnAction((e) -> {
            try {
                friendsSettingScene(null);
            } catch (IOException ex) {
                e.consume();
            }
        });

        manageGroupButton.setOnAction((e) -> {
            try {
                personalGroupManagementScene(null);
            } catch (IOException ex) {
                e.consume();
            }
        });

        seeMessagesButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    cancelAllTasks();
                    sceneManager.addScene("ChatManagement", "main-chat-history-management.fxml");
                    sceneManager.switchScene("ChatManagement");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
        });

        blockUserButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Block this user?");
                alert.setHeaderText("Block User");

                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        if (currentUser.blockUser(currentChatTarget.getUserId())) {
                            new Alert(AlertType.INFORMATION, "You have blocked this user").showAndWait();
                        }
                    }
                });
            };
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
                                cancelAllTasks();
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
                        updateChatList.restart();
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

        addPrivateTargetButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setHeaderText("Create a private chat with " + selectedUser.getUsername() + " ?");
                alert.setTitle("Create Private Chat");

                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        if (currentUser.createPrivateConversation(selectedUser.getUsername())) {
                            // refresh();
                            updateChat.restart();
                        } else {
                            new Alert(AlertType.ERROR, "Can't connect to user").showAndWait();
                        }
                    }
                });
            }
        });

        addGroupTargetButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                FXMLLoader loader = new FXMLLoader((Main.class.getResource("pop-up-create-group.fxml")));
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
                String username = selectedUser.getUsername();
                popUpController.setUsername(username);

                final Button confirmButton = (Button) dialog.getDialogPane().lookupButton(confirmButtonType);
                confirmButton.addEventFilter(ActionEvent.ACTION, event -> {
                    String groupName = popUpController.getGroupNameField();
                    if (groupName.trim().isEmpty()) {
                        new Alert(AlertType.ERROR, "The fields must not be empty!").showAndWait();
                        event.consume();
                    } else if (currentUser.createGroup(groupName, username)) {
                        new Alert(AlertType.INFORMATION, "Group created!").showAndWait();
                        // refresh();
                        updateChat.restart();
                    } else {
                        event.consume();
                    }

                    popUpController.clearAllFields();
                });

                dialog.showAndWait();
                dialog.close();
            }
        });

        searchUserField.textProperty().addListener((e) -> {
            searchPause.stop();
            searchPause.playFromStart();
        });

        messagesTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            handleSwitchTab(newTab);
        });

        searchGroupConversation.textProperty().addListener((e) -> {
            String text = searchGroupConversation.getText().trim();
            int index = -1;
            for (int i = 0; i < groupItems.size(); i ++)
                if (groupItems.get(i).getDisplayData().matches(text)){
                    index = i;
                    break;
                }
            if (index != -1)
                groupList.scrollTo(index);
        });

        searchPrivateConversation.textProperty().addListener((e) -> {
            String text = searchPrivateConversation.getText().trim();
            int index = -1;
            for (int i = 0; i < privateItems.size(); i ++)
                if (privateItems.get(i).getDisplayData().matches(text)){
                    index = i;
                    break;
                }
            if (index != -1)
                groupList.scrollTo(index);
        });
    }

    private void handleSwitchTab(Tab tab) {
        if (tab == privateTab) {
            currentTab = privateTab;

            groupList.getSelectionModel().clearSelection();
            searchUserList.getSelectionModel().clearSelection();
            searchUserField.clear();
            addPrivateTargetButton.setDisable(true);
            addGroupTargetButton.setDisable(true);
            loadSearchResults();

        } else if (tab == groupTab) {
            currentTab = groupTab;

            privateList.getSelectionModel().clearSelection();
            searchUserList.getSelectionModel().clearSelection();
            searchUserField.clear();
            addPrivateTargetButton.setDisable(true);
            addGroupTargetButton.setDisable(true);
            loadSearchResults();
        } else {
            currentTab = everyoneTab;

            groupList.getSelectionModel().clearSelection();
            privateList.getSelectionModel().clearSelection();
        }

        tabSwitched = true;
        currentConversation = null;
        updateChatList.restart();
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

    public void configureGroup(ActionEvent actionEvent) throws IOException {
        sceneManager.addScene("ThisGroupManager", "main-single-group-manager.fxml");
        sceneManager.switchScene("ThisGroupManager");
    }

    private void cancelAllTasks() {
        updateChat.cancel();
        updateChatList.cancel();
    }
}
