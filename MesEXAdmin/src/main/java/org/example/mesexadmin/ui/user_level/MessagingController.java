package org.example.mesexadmin.ui.user_level;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.example.mesexadmin.Main;
import org.example.mesexadmin.PopUpController;
import org.example.mesexadmin.SceneManager;
import org.example.mesexadmin.data_class.ConversationData;
import org.example.mesexadmin.data_class.MessageData;
import org.example.mesexadmin.ui.elements.ConversationListComponent;
import org.example.mesexadmin.ui.elements.MessageListComponent;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class MessagingController implements Initializable {
    private SceneManager sceneManager;

    static ConversationData currentConversation;

    @FXML
    private ListView<ConversationListComponent> messagingList;
    @FXML
    private ListView<MessageListComponent> messages;
    @FXML
    private Label myLabel;
    @FXML
    private TextArea myTextArea;
    @FXML
    private MenuButton optionButton;

    // Load from database
    ObservableList<ConversationListComponent> conversationList = FXCollections.observableArrayList(
            new ConversationListComponent(new ConversationData("group 1", "1")),
            new ConversationListComponent(new ConversationData("group 2", "2")),
            new ConversationListComponent(new ConversationData("group 3", "3"))
    );

    public void addFriend(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("pop-up-add.fxml"));
        Dialog<Objects> dialog = new Dialog<>();
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        DialogPane dialogPane = loader.load();
        PopUpController popUpController = loader.getController();
        popUpController.currentDialog = dialog;
        dialog.setDialogPane(dialogPane);
        dialog.showAndWait();
        dialog.close();
    }

    public void updateMessageBuffer(ActionEvent actionEvent){
//        if (actionEvent.getEventType())
    }

    public void addMessage(ActionEvent actionEvent){
        if (!myTextArea.getText().trim().isEmpty()){
            String msg = myTextArea.getText().trim();

            // Add Message processing here

            messages.getItems().add(new MessageListComponent(new MessageData(msg, "sender_1", "rec_1")));
            myTextArea.setText("");
        }
    }

    public void addMessage(){
        if (!myTextArea.getText().trim().isEmpty()){
            String msg = myTextArea.getText().trim();

            // Add Message processing here

            messages.getItems().add(new MessageListComponent(new MessageData(msg, "sender_1", "rec_1")));
            myTextArea.setText("");
        }
    }

    public void addGroup(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("pop-up-create-group.fxml"));
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

    public void advanced(ActionEvent actionEvent) throws IOException{
        sceneManager.addScene("ChatHistoryManagement", "main-all-chat-history-management.fxml");
        sceneManager.switchScene("ChatHistoryManagement");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sceneManager = Main.getSceneManager();

        optionButton.setDisable(true);
        messagingList.getItems().addAll(conversationList);

        messagingList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ConversationListComponent>() {
            @Override
            public void changed(ObservableValue<? extends ConversationListComponent> observableValue, ConversationListComponent conversationListComponent, ConversationListComponent t1) {
                currentConversation = messagingList.getSelectionModel().getSelectedItem().getConversation();
                myLabel.setText("Selected Chat: " + currentConversation.getConversationName());
                optionButton.setDisable(false);
                // Get current conversation
//                currentConversation = Main.globalQuery.conversations().getConversation(null);
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
//                        boolean res = Main.globalQuery.conversations().addMessageCon(null, null);
//                        System.out.println(res);
                        addMessage();
                    }
                }
            }
        });

//        Thread thread = getThread();
//        thread.start();

    }

//    private Thread getThread() {
//        Task<Void> task = new Task<Void>() {
//            @Override
//            protected Void call() throws Exception {
//                ChangeStreamIterable<Document> changeStream = Main.myMongo.messages.watch();
//                changeStream.forEach((Consumer<? super ChangeStreamDocument<Document>>) (n) -> Platform.runLater(() -> MongoManagement.processPushMongo(n, messages)));
//                return null;
//            }
//        };
//
//        Thread thread = new Thread(task);
//        thread.setDaemon(true);
//        return thread;
//    }

    public void friendsSettingScene(ActionEvent actionEvent) throws IOException{
        sceneManager.addScene("FriendsManagement", "main-friend-config.fxml");
        sceneManager.switchScene("FriendsManagement");
//
//        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main-friend-config.fxml")));
//        Stage thisStage = (Stage) ((MenuItem) actionEvent.getSource()).getParentPopup().getOwnerWindow();
//        scene = new Scene(root);
//        thisStage.setScene(scene);
//        thisStage.show();
    }

    public void userManagementScene(ActionEvent actionEvent) throws IOException{
        sceneManager.addScene("AppUserManagement", "main-user-manager.fxml");
        sceneManager.switchScene("AppUserManagement");
//
//        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main-user-manager.fxml")));
//        Stage thisStage = (Stage) ((MenuItem) actionEvent.getSource()).getParentPopup().getOwnerWindow();
//        scene = new Scene(root);
//        thisStage.setScene(scene);
//        thisStage.show();
    }

    public void groupManagementScene(ActionEvent actionEvent) throws IOException{
        sceneManager.addScene("AppGroupManagement", "main-group-manager.fxml");
        sceneManager.switchScene("AppGroupManagement");

//        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main-group-manager.fxml")));
//        Stage thisStage = (Stage) ((MenuItem) actionEvent.getSource()).getParentPopup().getOwnerWindow();
//        scene = new Scene(root);
//        thisStage.setScene(scene);
//        thisStage.show();
    }

    public void appManagementScene(ActionEvent actionEvent) throws IOException{
        sceneManager.addScene("AppManagement", "main-app-manager.fxml");
        sceneManager.switchScene("AppManagement");

//        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main-app-manager.fxml")));
//        Stage thisStage = (Stage) ((MenuItem) actionEvent.getSource()).getParentPopup().getOwnerWindow();
//        scene = new Scene(root);
//        thisStage.setScene(scene);
//        thisStage.show();
    }

    public void personalGroupManagementScene(ActionEvent actionEvent) throws IOException{
        sceneManager.addScene("MyGroupManagement", "main-convo-config.fxml");
        sceneManager.switchScene("MyGroupManagement");

//        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main-convo-config.fxml")));
//        Stage thisStage = (Stage) ((MenuItem) actionEvent.getSource()).getParentPopup().getOwnerWindow();
//        scene = new Scene(root);
//        thisStage.setScene(scene);
//        thisStage.show();
    }

    public void profileScene(ActionEvent actionEvent) throws IOException{
        sceneManager.addScene("EditProfile", "edit-user-profile.fxml");
        sceneManager.switchScene("EditProfile");

//        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("edit-user-profile.fxml")));
//        Stage thisStage = (Stage) ((MenuItem) actionEvent.getSource()).getParentPopup().getOwnerWindow();
//        scene = new Scene(root);
//        thisStage.setScene(scene);
//        thisStage.show();
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
//        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main-single-group-manager.fxml")));
//        Stage thisStage = (Stage) ((MenuItem) actionEvent.getSource()).getParentPopup().getOwnerWindow();
//        scene = new Scene(root);
//        thisStage.setScene(scene);
//        thisStage.show();
    }

    public void blockUser(ActionEvent actionEvent) {
        Alert newAlert = new Alert(Alert.AlertType.CONFIRMATION);
        newAlert.setContentText("Block this User?");
        newAlert.setHeaderText("Block User");
        newAlert.showAndWait();
    }

}
