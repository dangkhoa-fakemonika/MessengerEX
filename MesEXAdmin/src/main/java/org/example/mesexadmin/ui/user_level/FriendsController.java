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
import java.util.ResourceBundle;

public class FriendsController implements ControllerWrapper {
    private SceneManager sceneManager;
    private SessionUser currentUser;

    @FXML private TableView<UserData> friendsTable;

    // Sent request table
    @FXML private TableView<FriendRequestData> sentRequestsTable;
    @FXML private TableColumn<FriendRequestData, String> sentRequestsUsernameColumn;
    @FXML private TableColumn<FriendRequestData, Date> sentRequestsDateColumn;

    // Received request table
    @FXML private TableView<FriendRequestData> receivedRequestsTable;
    @FXML private TableColumn<FriendRequestData, String> receivedRequestsUsernameColumn;
    @FXML private TableColumn<FriendRequestData, Date> receivedRequestsDateColumn;
    @FXML private Button acceptRequestButton;
    @FXML private Button rejectFriendRequestButton;

    @FXML private TableView<UserData> blockedTable;


    static UserData friend, blocked;
    static FriendRequestData currentSentRequest, currentReceivedRequest;

    static ObservableList<UserData> data = FXCollections.observableArrayList();
    static ObservableList<UserData> blockedData = FXCollections.observableArrayList();

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

    public void blockUser(ActionEvent actionEvent) throws IOException {
        Alert newAlert = new Alert(Alert.AlertType.CONFIRMATION);
        newAlert.setContentText("Block this user?");
        newAlert.setHeaderText("Block User");
        newAlert.showAndWait();
    }

    public void unFriend(ActionEvent actionEvent) {
        Alert newAlert = new Alert(Alert.AlertType.CONFIRMATION);
        newAlert.setContentText("Remove this user from your friend list?");
        newAlert.setHeaderText("Unfriend");
        newAlert.showAndWait();
    }

    public void removeRequest(ActionEvent actionEvent) {
        Alert newAlert = new Alert(AlertType.CONFIRMATION);
        newAlert.setContentText("Un-send friend request to this user?");
        newAlert.setHeaderText("Remove Request");
        newAlert.showAndWait();
    }

    // public void acceptFriend(ActionEvent actionEvent) {
    //     Alert newAlert = new Alert(AlertType.CONFIRMATION);
    //     newAlert.setContentText("Accept friend request?");
    //     newAlert.setHeaderText("Accept Friend");
    //     newAlert.showAndWait();
    // }

    // public void denyFriend(ActionEvent actionEvent) {
    //     Alert newAlert = new Alert(AlertType.CONFIRMATION);
    //     newAlert.setContentText("Deny friend request?");
    //     newAlert.setHeaderText("Deny Friend");
    //     newAlert.showAndWait();
    // }

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

    public ObservableList<TableColumn<UserData, String>> generateUserColumns(){
        TableColumn<UserData, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("displayName"));
        nameCol.setMinWidth(100);

        TableColumn<UserData, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        usernameCol.setMinWidth(100);

        TableColumn<UserData, String> genderCol = new TableColumn<>("Gender");
        genderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));
        genderCol.setMinWidth(100);

        return FXCollections.observableArrayList(nameCol, usernameCol, genderCol);
    }


    public ObservableList<TableColumn<FriendRequestData, String>> generateRequestColumns(){
        TableColumn<FriendRequestData, String> nameCol = new TableColumn<>("ID");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("senderId"));
        nameCol.setMinWidth(100);

        TableColumn<FriendRequestData, String> timeCol = new TableColumn<>("Time Sent");
        timeCol.setCellValueFactory(new PropertyValueFactory<>("timeSent"));
        timeCol.setMinWidth(100);

        return FXCollections.observableArrayList(nameCol, timeCol);
    }

    public ObservableList<TableColumn<FriendRequestData, String>> generatePendingColumns(){
        TableColumn<FriendRequestData, String> nameCol = new TableColumn<>("ID");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("receiverId"));
        nameCol.setMinWidth(100);

        TableColumn<FriendRequestData, String> timeCol = new TableColumn<>("Time Sent");
        timeCol.setCellValueFactory(new PropertyValueFactory<>("timeSent"));
        timeCol.setMinWidth(100);

        return FXCollections.observableArrayList(nameCol, timeCol);
    }

    @Override
    public void myInitialize() {
        currentUser = Main.getThisUser();
        UserData userData = currentUser.getSessionUserData();

        // Sent requests table
        
        sentRequests.clear();
        sentRequests.addAll(
            // Get sent requests from database
            currentUser.myQuery.requests().getAllRequestsDetails(userData.getUserId(), null)
        );
        sentRequestsTable.setItems(FXCollections.observableArrayList(sentRequests));

        // Received requests table
        receivedRequests.clear();
        receivedRequests.addAll(
            // Get received requests from database
            currentUser.myQuery.requests().getAllRequestsDetails(null, userData.getUserId())
        );
        receivedRequestsTable.setItems(FXCollections.observableArrayList(receivedRequests));

        friendsTable.setItems(FXCollections.observableArrayList());
//        friendsTable.getColumns().clear();
        blockedTable.setItems(FXCollections.observableArrayList());
//        blockedTable.getColumns().clear();
        

        if (data != null){
            data.clear();
            System.out.println(currentUser.getSessionUserData().getFriend().size());
            ArrayList<UserData> ud = currentUser.myQuery.users().getUserLists(currentUser.getSessionUserData().getFriend());
            System.out.println(ud.size());
            data.addAll(ud);
        }


        friendsTable.setItems(data);
        blockedTable.setItems(blockedData);
        friendsTable.refresh();
        blockedTable.refresh();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sceneManager = Main.getSceneManager();
        currentUser = Main.getThisUser();

        friendsTable.getColumns().addAll(generateUserColumns());
        blockedTable.getColumns().addAll(generateUserColumns());

        // Sent requests table
        sentRequestsUsernameColumn.setCellValueFactory(new PropertyValueFactory<>("receiverUsername"));
        sentRequestsDateColumn.setCellValueFactory(new PropertyValueFactory<>("timeSent"));

        // Received requests table
        receivedRequestsUsernameColumn.setCellValueFactory(new PropertyValueFactory<>("senderUsername"));
        receivedRequestsDateColumn.setCellValueFactory(new PropertyValueFactory<>("timeSent"));

        friendsTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<UserData>() {
            @Override
            public void changed(ObservableValue<? extends UserData> observableValue, UserData userData, UserData t1) {
                friend = friendsTable.getSelectionModel().getSelectedItem();
            }
        });

        blockedTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<UserData>() {
            @Override
            public void changed(ObservableValue<? extends UserData> observableValue, UserData userData, UserData t1) {
                blocked = blockedTable.getSelectionModel().getSelectedItem();
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
            public void handle(ActionEvent arg0) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Accept friend request?");
                alert.setHeaderText("Accept Friend");

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
            public void handle(ActionEvent arg0) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Reject friend request?");
                alert.setHeaderText("Reject Friend");

                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        if (rejectFriendRequest()) {
                            new Alert(AlertType.INFORMATION, "You have rejected a friend request!").showAndWait();
                            receivedRequests.remove(currentReceivedRequest);
                            receivedRequestsTable.refresh();
                        }
                    }
                });
            }
        });
    }

    private boolean acceptFriendRequest() {
        return currentUser.acceptFriendRequest(currentReceivedRequest);
    }

    private boolean rejectFriendRequest() {
        return currentUser.rejectFriendRequest(currentReceivedRequest);
    }
}
