package org.example.mesexadmin.ui.user_level;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
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

import org.example.mesexadmin.App;
import org.example.mesexadmin.PopUpController;
import org.example.mesexadmin.SceneManager;
import org.example.mesexadmin.SessionUser;
import org.example.mesexadmin.data_class.FriendRequestData;
import org.example.mesexadmin.data_class.UserData;
import org.example.mesexadmin.ui.ControllerWrapper;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class FriendsController implements ControllerWrapper {
    private SceneManager sceneManager;
    private SessionUser currentUser;

    @FXML private Button returnToMainButton;

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
    @FXML private ChoiceBox<String> onlineFilter;
    @FXML private TextField onlineFilterField;

    // All friend table
    @FXML private TableView<UserData> friendsTable;
    @FXML private TableColumn<UserData, String> friendsNameColumn;
    @FXML private TableColumn<UserData, String> friendsUsernameColumn;
    @FXML private TableColumn<UserData, String> friendsGenderColumn;
    @FXML private TableColumn<UserData, String> friendsBirthdayColumn;
    @FXML private TableColumn<UserData, String> friendsStatusColumn;
    @FXML private TableColumn<UserData, String> friendsLastLoginColumn;
    @FXML private TableColumn<UserData, String> friendsJoinedDateColumn;
    @FXML private ChoiceBox<String> friendsFilter;
    @FXML private TextField friendsFilterField;

    // Sent request table
    @FXML private TableView<FriendRequestData> sentRequestsTable;
    @FXML private TableColumn<FriendRequestData, String> sentRequestsUsernameColumn;
    @FXML private TableColumn<FriendRequestData, String> sentRequestsDateColumn;
    @FXML private Button removeFriendRequestButton;
    @FXML private ChoiceBox<String> sentRequestsFilter;
    @FXML private TextField sentRequestsFilterField;

    // Received request table
    @FXML private TableView<FriendRequestData> receivedRequestsTable;
    @FXML private TableColumn<FriendRequestData, String> receivedRequestsUsernameColumn;
    @FXML private TableColumn<FriendRequestData, String> receivedRequestsDateColumn;
    @FXML private Button acceptRequestButton;
    @FXML private Button rejectFriendRequestButton;
    @FXML private ChoiceBox<String> receivedRequestsFilter;
    @FXML private TextField receivedRequestsFilterField;

    // Blocked table
    @FXML private TableView<UserData> blockedTable;
    @FXML private TableColumn<UserData,  String> blockedNameColumn;
    @FXML private TableColumn<UserData, String> blockedUsernameColumn;
    @FXML private Button unblockUserkButton;
    @FXML private ChoiceBox<String> blockedFilter;
    @FXML private TextField blockedFilterField;

    // Selected row
    static UserData currentOnline, currentFriend, currentBlocked;
    static FriendRequestData currentSentRequest, currentReceivedRequest;

    // Selected filter
    static ChoiceBox<String> currentFilter;
    static TextField currentFilterField;
    static String previousFilterOption;
    static String currentFilterOption;

    // Data list
    final ObservableList<UserData> onlineData = FXCollections.observableArrayList();
    final ObservableList<UserData> friendData = FXCollections.observableArrayList();
    final ObservableList<UserData> blockedData = FXCollections.observableArrayList();
    final ObservableList<FriendRequestData> receivedRequests = FXCollections.observableArrayList();
    final ObservableList<FriendRequestData> sentRequests = FXCollections.observableArrayList();

    // Update data task
    private ScheduledService<Void> updateOnlineData;
    private ScheduledService<Void> updateFriendData;
    private ScheduledService<Void> updateBlockedData;
    private ScheduledService<Void> updateReceivedRequests;
    private ScheduledService<Void> updateSentRequests;

    // Utility
    private PauseTransition onlineFilterPause;
    private PauseTransition friendsFilterPause;
    private PauseTransition blockedFilterPause;
    private PauseTransition sentRequestsFilterPause;
    private PauseTransition receivedRequestsFilterPause;
    private String[] userFilterKeys = {"None", "username", "displayName"};
    private String[] requestFilterKeys = {"None", "username"};
    private HashMap<String, String> filterMap = new HashMap<>();

    public void addFriend(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("pop-up-add.fxml"));
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

    @Override
    public void myInitialize() {
        currentUser = App.getCurrentUser();

        filterMap.put("online", "None");
        filterMap.put("blocked", "None");
        filterMap.put("friends", "None");
        filterMap.put("sentRequests", "None");
        filterMap.put("receivedRequests", "None");

        onlineFilter.setValue("None");
        friendsFilter.setValue("None");
        blockedFilter.setValue("None");
        sentRequestsFilter.setValue("None");
        receivedRequestsFilter.setValue("None");

        currentFilter = onlineFilter;
        currentFilterField = onlineFilterField;

        initiateUpdateTask();

        onlineFilter.setOnAction((e) -> updateOnlineData.restart());
        onlineFilterPause.setOnFinished((e) -> updateOnlineData.restart());

        friendsFilter.setOnAction((e) -> updateFriendData.restart());
        friendsFilterPause.setOnFinished((e) -> updateFriendData.restart());

        blockedFilter.setOnAction((e) -> updateBlockedData.restart());
        blockedFilterPause.setOnFinished((e) -> updateBlockedData.restart());

        sentRequestsFilter.setOnAction((e) -> updateSentRequests.restart());
        sentRequestsFilterPause.setOnFinished((e) -> updateSentRequests.restart());

        receivedRequestsFilter.setOnAction((e) -> updateReceivedRequests.restart());
        receivedRequestsFilterPause.setOnFinished((e) -> updateReceivedRequests.restart());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sceneManager = App.getSceneManager();

        // Set up filter
        onlineFilter.getItems().addAll(userFilterKeys);
        friendsFilter.getItems().addAll(userFilterKeys);
        blockedFilter.getItems().addAll(userFilterKeys);
        sentRequestsFilter.getItems().addAll(requestFilterKeys);
        receivedRequestsFilter.getItems().addAll(requestFilterKeys);

        onlineFilterPause = new PauseTransition(Duration.millis(500));
        onlineFilterField.textProperty().addListener((observableValue, o, n) -> {
            onlineFilterPause.stop();
            onlineFilterPause.playFromStart();
        });

        friendsFilterPause = new PauseTransition(Duration.millis(500));
        friendsFilterField.textProperty().addListener((observableValue, o, n) -> {
            friendsFilterPause.stop();
            friendsFilterPause.playFromStart();
        });

        blockedFilterPause = new PauseTransition(Duration.millis(500));
        blockedFilterField.textProperty().addListener((observableValue, o, n) -> {
            blockedFilterPause.stop();
            blockedFilterPause.playFromStart();
        });

        sentRequestsFilterPause = new PauseTransition(Duration.millis(500));
        sentRequestsFilterField.textProperty().addListener((observableValue, o, n) -> {
            sentRequestsFilterPause.stop();
            sentRequestsFilterPause.playFromStart();
        });

        receivedRequestsFilterPause = new PauseTransition(Duration.millis(500));
        receivedRequestsFilterField.textProperty().addListener((observableValue, o, n) -> {
            receivedRequestsFilterPause.stop();
            receivedRequestsFilterPause.playFromStart();
        });

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
                        }
                    }
                });
            }
        });

        unblockUserkButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setContentText("Remove block on this user?");
                alert.setHeaderText("Unblock User");

                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        if (unblockUser(currentBlocked)) {
                            new Alert(AlertType.INFORMATION, "You have removed this user from your block list!").showAndWait();
                            blockedData.remove(currentBlocked);
                        }
                    }
                });
            };
        });

        returnToMainButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                try {
                    cancelAllTasks();
                    friendManagementTabPane.getSelectionModel().select(onlineTab);
                    sceneManager.addScene("Main", "main-messaging.fxml");
                    sceneManager.switchScene("Main");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private boolean acceptFriendRequest() {
        return currentUser.acceptFriendRequest(currentReceivedRequest);
    }

    private boolean removeFriendRequest(FriendRequestData removeTarget) {
        return currentUser.removeFriendRequest(removeTarget);
    }

    private boolean unblockUser(UserData blockedUser) {
        return currentUser.unblockUser(blockedUser.getUserId());
    }

    private void initiateUpdateTask() {
        updateOnlineData = initiateGetUserDataTask(onlineData, "online");
        updateOnlineData.setPeriod(Duration.seconds(60));
        updateOnlineData.start();  // This task run upon entry

        updateFriendData = initiateGetUserDataTask(friendData, "friends");
        updateFriendData.setPeriod(Duration.seconds(60));

        updateBlockedData = initiateGetUserDataTask(blockedData, "blocked");
        updateBlockedData.setPeriod(Duration.seconds(60)); 

        updateSentRequests = initiateGetFriendRequestDataTask(sentRequests, "sentRequests");
        updateSentRequests.setPeriod(Duration.seconds(60));

        updateReceivedRequests = initiateGetFriendRequestDataTask(receivedRequests, "reveicedRequests");
        updateReceivedRequests.setPeriod(Duration.seconds(60));
    }

    private void handleSwitchTab(Tab tab) {
        if (tab == onlineTab) {
            currentFilter = onlineFilter;
            currentFilterField = onlineFilterField;
            updateOnlineData.restart();

        } else if (tab == friendsTab) {
            currentFilter = friendsFilter;
            currentFilterField = friendsFilterField;
            updateFriendData.restart();

        } else if (tab == blockedUsersTab) {
            currentFilter = blockedFilter;
            currentFilterField = blockedFilterField;
            updateBlockedData.restart();

        } else if (tab == sentRequestsTab) {
            currentFilter = sentRequestsFilter;
            currentFilterField = sentRequestsFilterField;
            updateSentRequests.restart();

        } else if (tab == receivedRequestsTab) {
            currentFilter = receivedRequestsFilter;
            currentFilterField = receivedRequestsFilterField;
            updateReceivedRequests.restart();
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
                            ArrayList<UserData> newData = getUserDataList(tab);
                            data.setAll(newData);
                            updateFilterField(tab);
                        });
                        return null;
                    }
                };
            }
        };
    }

    private ScheduledService<Void> initiateGetFriendRequestDataTask(ObservableList<FriendRequestData> data, String tab) {
        return new ScheduledService<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        Platform.runLater(() -> {
                            ArrayList<FriendRequestData> newData = getFriendRequestDataList(tab);
                            data.setAll(newData);
                            updateFilterField(tab);
                        });
                        return null;
                    }
                };
            }
        };
    }

    private ArrayList<UserData> getUserDataList(String tab) {
        String filterKey = currentFilterField.getText();
        String filterOption = currentFilter.getValue();
        boolean isFilter = !filterOption.equals("None") && !filterKey.isEmpty();

        if (tab.equals("online")) {
            if (isFilter)
                return currentUser.getOnlineFriendListWithFilter(filterOption, filterKey);
            else 
                return currentUser.getOnlineFriendList();
        } else if (tab.equals("friends")) {
            if (isFilter)
                return currentUser.getFriendListWithFilter(filterOption, filterKey);
            else
                return currentUser.getFriendList();
        } else {
            filterOption = blockedFilter.getValue();
            if (isFilter)
                return currentUser.getBlockedListWithFilter(filterOption, filterKey);
            else
                return currentUser.getBlockedList();
        }
    }

    private ArrayList<FriendRequestData> getFriendRequestDataList(String tab) {
        String filterKey = currentFilterField.getText();
        String filterOption = currentFilter.getValue();
        boolean isFilter = !filterOption.equals("None") && !filterKey.isEmpty();

        if (tab.equals("sentRequests")) {
            if (isFilter)
                return currentUser.getSentRequests();
            else
                return currentUser.getSentRequests();
        } else {
            if (isFilter)
                return currentUser.getReceivedRequests();
            else
                return currentUser.getReceivedRequests();
        }
    }

    private void updateFilterField(String tab) {
        if (currentFilter.getValue().equals("None")) {
            currentFilterField.clear();
            currentFilterField.setDisable(true);
        } else {
            currentFilterField.setDisable(false);
        }    
    }

    private void cancelAllTasks() {
        updateOnlineData.cancel();
        updateFriendData.cancel();
        updateBlockedData.cancel();
        updateSentRequests.cancel();
        updateReceivedRequests.cancel();
    }
}