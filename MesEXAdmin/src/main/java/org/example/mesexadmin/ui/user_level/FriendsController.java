package org.example.mesexadmin.ui.user_level;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;

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

    @FXML private TabPane friendManagementTabPane;

    @FXML private Tab onlineTab;
    @FXML private Tab friendsTab;
    @FXML private Tab sentRequestsTab;
    @FXML private Tab receivedRequestsTab;
    @FXML private Tab blockedUsersTab;

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
    @FXML private Button unblockUserkButton;

    // Selected row
    static UserData currentOnline, currentFriend, currentBlocked;
    static FriendRequestData currentSentRequest, currentReceivedRequest;

    // Data list
    final ObservableList<UserData> onlineData = FXCollections.observableArrayList();
    final ObservableList<UserData> friendData = FXCollections.observableArrayList();
    final ObservableList<UserData> blockedData = FXCollections.observableArrayList();
    final ObservableList<FriendRequestData> receivedRequests = FXCollections.observableArrayList();
    final ObservableList<FriendRequestData> sentRequests = FXCollections.observableArrayList();

    private ScheduledService<Void> getOnlineData;
    private ScheduledService<Void> getFriendData;
    private ScheduledService<Void> getBlockedData;
    private ScheduledService<Void> getReceivedRequests;
    private ScheduledService<Void> getSentRequests;

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
                // blockedData.add(removeTarget);
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
        pauseAllServices();
        sceneManager.addScene("Main", "main-messaging.fxml");
        sceneManager.switchScene("Main");
    }

    @Override
    public void myInitialize() {
        currentUser = Main.getThisUser();
        UserData userData = currentUser.getSessionUserData();

        //Online friend table
        // onlineData.setAll(
        //     // Get currently online users from database
        //     currentUser.getOnlineFriendList()
        // );
        // onlineFriendTable.setItems(onlineData);

        // All friend table
        // friendData.setAll(
        //     // Get all friend of current user from database
        //     currentUser.getFriendList()
        // );
        // friendsTable.setItems(friendData);

        // Sent requests table
        // sentRequests.setAll(
        //     // Get sent requests from database
        //     currentUser.getSentRequests()
        // );
        // sentRequestsTable.setItems(sentRequests);

        // Received requests table
        // receivedRequests.setAll(
        //     // Get received requests from database
        //     currentUser.getReceivedRequests()
        // );
        // receivedRequestsTable.setItems(receivedRequests);

        // Blocked user table
        // blockedData.setAll(
        //     currentUser.getBlockedList()
        // );
        // blockedTable.setItems(blockedData);
        initiateGetData();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sceneManager = Main.getSceneManager();
        currentUser = Main.getThisUser();

        // Online friend table
        onlineNameColumn.setCellValueFactory(new PropertyValueFactory<>("displayName"));
        onlineUsernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        onlineGenderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        onlineLoginAtColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getFormattedDate("lastLogin")));

        onlineFriendTable.setItems(onlineData);

        // All friend table
        friendsNameColumn.setCellValueFactory(new PropertyValueFactory<>("displayName"));
        friendsUsernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        friendsGenderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        friendsBirthdayColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getFormattedDate("dateOfBirth")));
        friendsStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        friendsLastLoginColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getFormattedDate("lastLogin")));
        friendsJoinedDateColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getFormattedDate("dateJoined")));

        friendsTable.setItems(friendData);

        // Sent requests table
        sentRequestsUsernameColumn.setCellValueFactory(new PropertyValueFactory<>("receiverUsername"));
        sentRequestsDateColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getFormattedTimeSent()));

        sentRequestsTable.setItems(sentRequests);

        // Received requests table
        receivedRequestsUsernameColumn.setCellValueFactory(new PropertyValueFactory<>("senderUsername"));
        receivedRequestsDateColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getFormattedTimeSent()));

        receivedRequestsTable.setItems(receivedRequests);

        // Blocked user table
        blockedNameColumn.setCellValueFactory(new PropertyValueFactory<>("displayName"));
        blockedUsernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        blockedTable.setItems(blockedData);

        // Tab switch handle
        friendManagementTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            handleSwitchTab(newTab);
        });

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
                            // receivedRequestsTable.refresh();
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
                        if (removeFriendRequest(currentReceivedRequest)) {
                            new Alert(AlertType.INFORMATION, "You have rejected a friend request!").showAndWait();
                            receivedRequests.remove(currentReceivedRequest);
                            // receivedRequestsTable.refresh();
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
                        if (removeFriendRequest(currentSentRequest)) {
                            new Alert(AlertType.INFORMATION, "You have removed your request!").showAndWait();
                            sentRequests.remove(currentSentRequest);
                            // sentRequestsTable.refresh();
                        }
                    }
                });
            }
        });
    }

    private boolean acceptFriendRequest() {
        return currentUser.acceptFriendRequest(currentReceivedRequest);
    }

    private boolean removeFriendRequest(FriendRequestData removeTarget) {
        return currentUser.removeFriendRequest(removeTarget);
    }

    private void initiateGetData() {
        getOnlineData = initiateGetUserDataTask(onlineData, "online");
        getFriendData = initiateGetUserDataTask(friendData, "friends");
        getBlockedData = initiateGetUserDataTask(blockedData, "blocked");
        getSentRequests = initiateGetFriendRequesstDataTask(sentRequests, "sentRequests");
        getReceivedRequests = initiateGetFriendRequesstDataTask(receivedRequests, "reveicedRequests");
    }

    private void handleSwitchTab(Tab tab) {
        // pauseAllServices();

        if (tab == onlineTab) {
            System.out.println("onl");
            getOnlineData.setPeriod(Duration.seconds(5));
            getOnlineData.restart();
        } else if (tab == friendsTab) {
            System.out.println("friend");
            getFriendData.setPeriod(Duration.seconds(5));
            getFriendData.restart();
        } else if (tab == blockedUsersTab) {
            System.out.println("blocked");
            getBlockedData.setPeriod(Duration.seconds(5)); 
            getBlockedData.restart();
        } else if (tab == sentRequestsTab) {
            System.out.println("sent");
            getSentRequests.setPeriod(Duration.seconds(5));
            getSentRequests.restart();
        } else if (tab == receivedRequestsTab) {
            System.out.println("receiv");
            getReceivedRequests.setPeriod(Duration.seconds(5));
            getReceivedRequests.restart();
        }
    }

    private ScheduledService<Void> initiateGetUserDataTask(ObservableList<UserData> data, String tab) {
        return new ScheduledService<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        Platform.runLater(() -> {
                            System.out.println("start fet");
                            getUserDataList(tab).stream().filter(item -> !data.contains(item)).forEach(data::add);
                            System.out.println("end fet");
                        });
                        return null;
                    }
                };
            }
        };
    }

    private ScheduledService<Void> initiateGetFriendRequesstDataTask(ObservableList<FriendRequestData> data, String tab) {
        return new ScheduledService<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        Platform.runLater(() -> {
                            System.out.println("start fet");
                            getFriendRequestDataList(tab).stream().filter(item -> !data.contains(item)).forEach(data::add);
                            System.out.println("end fet");
                        });
                        return null;
                    }
                };
            }
        };
    }

    private ArrayList<UserData> getUserDataList(String tab) {
        if (tab.equals("online")) {
            return currentUser.getOnlineFriendList();
        } else if (tab.equals("friends")) {
            return currentUser.getFriendList();
        } else {
            return currentUser.getBlockedList();
        }
    }

    private ArrayList<FriendRequestData> getFriendRequestDataList(String tab) {
        if (tab.equals("sentRequests")) {
            return currentUser.getSentRequests();
        } else {
            return currentUser.getReceivedRequests();
        }
    }
    
    private void pauseAllServices() {
        getOnlineData.cancel();
        getFriendData.cancel();
        getBlockedData.cancel();
        getSentRequests.cancel();
        getReceivedRequests.cancel();
    }
}