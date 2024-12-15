package org.example.mesexadmin.ui.user_level;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.mesexadmin.Main;
import org.example.mesexadmin.PopUpController;
import org.example.mesexadmin.SceneManager;
import org.example.mesexadmin.SessionUser;
import org.example.mesexadmin.data_class.FriendRequestData;
import org.example.mesexadmin.data_class.UserData;
import org.example.mesexadmin.ui.ControllerWrapper;
import org.bson.Document;

import com.mongodb.client.MongoCollection;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class FriendsController implements ControllerWrapper {
    private SceneManager sceneManager;
    private SessionUser currentUser;

    // Online friend table
    @FXML private TableView<UserData> onlineFriendTable;
    @FXML private TableColumn<UserData, String> onlineNameColumn;
    @FXML private TableColumn<UserData, String> onlineUsernameColumn;
    @FXML private TableColumn<UserData, String> onlineGenderColumn;
    @FXML private TableColumn<UserData, String> onlineLoginAtColumn;

    // All friend table
    @FXML private TableView<UserData> friendsTable;
    @FXML private TableColumn<UserData, String> friendsNameColumn;
    @FXML private TableColumn<UserData, String> friendsUsernameColumn;
    @FXML private TableColumn<UserData, String> friendsGenderColumn;
    @FXML private TableColumn<UserData, String> friendsBirthdayColumn;
    @FXML private TableColumn<UserData, String> friendsStatusColumn;
    @FXML private TableColumn<UserData, String> friendsLastLoginColumn;
    @FXML private TableColumn<UserData, String> friendsJoinedDateColumn;

    // Sent request table
    @FXML private TableView<FriendRequestData> sentRequestsTable;
    @FXML private TableColumn<FriendRequestData, String> sentRequestsUsernameColumn;
    @FXML private TableColumn<FriendRequestData, String> sentRequestsDateColumn;
    @FXML private Button removeFriendRequestButton;

    // Received request table
    @FXML private TableView<FriendRequestData> receivedRequestsTable;
    @FXML private TableColumn<FriendRequestData, String> receivedRequestsUsernameColumn;
    @FXML private TableColumn<FriendRequestData, String> receivedRequestsDateColumn;
    @FXML private Button acceptRequestButton;
    @FXML private Button rejectFriendRequestButton;

    // Blocked table
    @FXML private TableView<UserData> blockedTable;
    @FXML private TableColumn<UserData,  String> blockedNameColumn;
    @FXML private TableColumn<UserData, String> blockedUsernameColumn;

    static UserData currentOnline, currentFriend, currentBlocked;
    static FriendRequestData currentSentRequest, currentReceivedRequest;

    final ObservableList<UserData> onlineData = FXCollections.observableArrayList();
    final ObservableList<UserData> friendData = FXCollections.observableArrayList();
    final ObservableList<UserData> blockedData = FXCollections.observableArrayList();

    final ObservableList<FriendRequestData> receivedRequests = FXCollections.observableArrayList();
    final ObservableList<FriendRequestData> sentRequests = FXCollections.observableArrayList();

    public void addFriend(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("pop-up-add.fxml"));
        Dialog<Objects> dialog = new Dialog<>();
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        DialogPane dialogPane = loader.load();
        PopUpController popUpController = loader.getController();
        popUpController.currentDialog = dialog;
        dialog.setDialogPane(dialogPane);
        dialog.showAndWait();
        dialog.close();
    }

    public void blockUser(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Block this user?");
        alert.setHeaderText("Block User");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            String buttonId = ((Button) event.getSource()).getId();
            UserData removeTarget;

            if (buttonId.equals("onlineBlockButton")) {
                removeTarget = currentOnline;
            } else {
                removeTarget = currentFriend;
            }

            if (currentUser.blockUser(removeTarget.getUserId())) {
                onlineData.remove(removeTarget);
                friendData.remove(removeTarget);
                new Alert(AlertType.INFORMATION, "You have blocked this user and removed them from your friendlist").showAndWait();
            }
        }
    }

    public void unfriend(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Remove this user from your friend list?");
        alert.setHeaderText("Unfriend User");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            String buttonId = ((Button) event.getSource()).getId();
            UserData removeTarget;

            if (buttonId.equals("onlineUnfriendButton")) {
                removeTarget = currentOnline;
            } else {
                removeTarget = currentFriend;
            }

            if (currentUser.unfriendUser(removeTarget.getUserId())) {
                onlineData.remove(removeTarget);
                friendData.remove(removeTarget);
                new Alert(AlertType.INFORMATION, "You have removed this user from your friendlist").showAndWait();
            }
        }
    }

    public void unblockUser(ActionEvent actionEvent) {
        Alert newAlert = new Alert(AlertType.CONFIRMATION);
        newAlert.setContentText("Remove block on this user?");
        newAlert.setHeaderText("Unblock User");
        newAlert.showAndWait();
    }

    public void returnToMain(ActionEvent actionEvent) throws IOException {
        sceneManager.addScene("Main", "main-messaging.fxml");
        sceneManager.switchScene("Main");
    }

    @Override
    public void myInitialize() {
        currentUser = Main.getThisUser();
        UserData userData = currentUser.getSessionUserData();

        //Online friend table
        onlineData.clear();
        onlineData.addAll(
            // Get currently online users from database
            currentUser.getOnlineFriendList()
        );
        onlineFriendTable.setItems(onlineData);

        // All friend table
        friendData.clear();
        friendData.addAll(
            // Get all friend of current user from database
            currentUser.getFriendList()
        );
        friendsTable.setItems(friendData);

        // Sent requests table
        sentRequests.clear();
        sentRequests.addAll(
            // Get sent requests from database
            currentUser.myQuery.requests().getAllRequestsDetails(userData.getUserId(), null)
        );
        sentRequestsTable.setItems(sentRequests);

        // Received requests table
        receivedRequests.clear();
        receivedRequests.addAll(
            // Get received requests from database
            currentUser.myQuery.requests().getAllRequestsDetails(null, userData.getUserId())
        );
        receivedRequestsTable.setItems(receivedRequests);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sceneManager = Main.getSceneManager();
        currentUser = Main.getThisUser();

        // friendsTable.getColumns().addAll(generateUserColumns());
        // blockedTable.getColumns().addAll(generateUserColumns());

        // Online friend table
        onlineNameColumn.setCellValueFactory(new PropertyValueFactory<>("displayName"));
        onlineUsernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        onlineGenderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        onlineLoginAtColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getFormattedDate("lastLogin")));

        // All friend table
        friendsNameColumn.setCellValueFactory(new PropertyValueFactory<>("displayName"));
        friendsUsernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        friendsGenderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        friendsBirthdayColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getFormattedDate("dateOfBirth")));
        friendsStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        friendsLastLoginColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getFormattedDate("lastLogin")));
        friendsJoinedDateColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getFormattedDate("dateJoined")));

        // Sent requests table
        sentRequestsUsernameColumn.setCellValueFactory(new PropertyValueFactory<>("receiverUsername"));
        sentRequestsDateColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getFormattedTimeSent()));

        // Received requests table
        receivedRequestsUsernameColumn.setCellValueFactory(new PropertyValueFactory<>("senderUsername"));
        receivedRequestsDateColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getFormattedTimeSent()));

        onlineFriendTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<UserData>() {
            @Override
            public void changed(ObservableValue<? extends UserData> observableValue, UserData userData, UserData t1) {
                currentOnline = onlineFriendTable.getSelectionModel().getSelectedItem();
            }
        });

        friendsTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<UserData>() {
            @Override
            public void changed(ObservableValue<? extends UserData> observableValue, UserData userData, UserData t1) {
                currentFriend = friendsTable.getSelectionModel().getSelectedItem();
            }
        });

        blockedTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<UserData>() {
            @Override
            public void changed(ObservableValue<? extends UserData> observableValue, UserData userData, UserData t1) {
                currentBlocked = blockedTable.getSelectionModel().getSelectedItem();
            }
        });

        sentRequestsTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<FriendRequestData>() {
            @Override
            public void changed(ObservableValue<? extends FriendRequestData> observableValue, FriendRequestData friendRequestData, FriendRequestData t1) {
                currentSentRequest = sentRequestsTable.getSelectionModel().getSelectedItem();
            }
        });

        receivedRequestsTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<FriendRequestData>() {
            @Override
            public void changed(ObservableValue<? extends FriendRequestData> observableValue, FriendRequestData friendRequestData, FriendRequestData t1) {
                currentReceivedRequest = receivedRequestsTable.getSelectionModel().getSelectedItem();
            }
        });

        acceptRequestButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Accept this friend request?");
                alert.setHeaderText("Accept Friend Request");

                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        if (acceptFriendRequest()) {
                            new Alert(AlertType.INFORMATION, "You have accepted a friend request!").showAndWait();
                            receivedRequests.remove(currentReceivedRequest);
                            receivedRequestsTable.refresh();
                        }
                    }
                });
            }
        });

        rejectFriendRequestButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Reject this friend request?");
                alert.setHeaderText("Reject Friend Request");

                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        if (removeFriendRequest()) {
                            new Alert(AlertType.INFORMATION, "You have rejected a friend request!").showAndWait();
                            receivedRequests.remove(currentReceivedRequest);
                            receivedRequestsTable.refresh();
                        }
                    }
                });
            }
        });

        removeFriendRequestButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Remove this friend request?");
                alert.setHeaderText("Remove Friend Request");

                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        if (removeFriendRequest()) {
                            new Alert(AlertType.INFORMATION, "You have removed your request!").showAndWait();
                            sentRequests.remove(currentSentRequest);
                            sentRequestsTable.refresh();
                        }
                    }
                });
            }
        });
    }

    private boolean acceptFriendRequest() {
        return currentUser.acceptFriendRequest(currentReceivedRequest);
    }

    private boolean removeFriendRequest() {
        return currentUser.removeFriendRequest(currentReceivedRequest);
    }
}