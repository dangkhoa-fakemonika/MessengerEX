package org.example.mesexadmin.ui.user_level;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.mesexadmin.Main;
import org.example.mesexadmin.PopUpController;
import org.example.mesexadmin.SceneManager;
import org.example.mesexadmin.data_class.FriendRequestData;
import org.example.mesexadmin.data_class.UserData;
import org.example.mesexadmin.ui.ControllerWrapper;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class FriendsController implements ControllerWrapper {
    private SceneManager sceneManager;

    @FXML
    private TableView<UserData> friendsTable;
    @FXML
    private TableView<FriendRequestData> pendingTable;
    @FXML
    private TableView<FriendRequestData> requestTable;
    @FXML
    private TableView<UserData> blockedTable;

    static UserData friend, blocked;
    static FriendRequestData pending, request;

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
        Alert newAlert = new Alert(Alert.AlertType.CONFIRMATION);
        newAlert.setContentText("Un-send friend request to this user?");
        newAlert.setHeaderText("Remove Request");
        newAlert.showAndWait();
    }

    public void acceptFriend(ActionEvent actionEvent) {
        Alert newAlert = new Alert(Alert.AlertType.CONFIRMATION);
        newAlert.setContentText("Accept friend request?");
        newAlert.setHeaderText("Accept Friend");
        newAlert.showAndWait();
    }

    public void denyFriend(ActionEvent actionEvent) {
        Alert newAlert = new Alert(Alert.AlertType.CONFIRMATION);
        newAlert.setContentText("Deny friend request?");
        newAlert.setHeaderText("Deny Friend");
        newAlert.showAndWait();
    }

    public void unblockUser(ActionEvent actionEvent) {
        Alert newAlert = new Alert(Alert.AlertType.CONFIRMATION);
        newAlert.setContentText("Remove block on this user?");
        newAlert.setHeaderText("Unblock User");
        newAlert.showAndWait();
    }


    public void returnToMain(ActionEvent actionEvent) throws IOException {
        sceneManager.addScene("Main", "main-messaging.fxml");
        sceneManager.switchScene("Main");
    }

    final ObservableList<UserData> data = FXCollections.observableArrayList(
            new UserData("FakeMonika", "fakemonika", "example@email.com", "Active"),
            new UserData("KanCh", "kanch", "example@email.com", "Active"),
            new UserData("Ryan Gosling", "him", "example@email.com", "Offline")
    );

    final ObservableList<UserData> blockedData = FXCollections.observableArrayList(
            new UserData("FakeMonika", "fakemonika", "example@email.com", "Active"),
            new UserData("KanCh", "kanch", "example@email.com", "Active"),
            new UserData("Ryan Gosling", "him", "example@email.com", "Offline")
    );

    final ObservableList<FriendRequestData> requests = FXCollections.observableArrayList();
    final ObservableList<FriendRequestData> pendings = FXCollections.observableArrayList();

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
        friendsTable.setItems(FXCollections.observableArrayList());
        friendsTable.getColumns().clear();
        blockedTable.setItems(FXCollections.observableArrayList());
        blockedTable.getColumns().clear();
        friendsTable.setItems(data);
        friendsTable.getColumns().addAll(generateUserColumns());
        blockedTable.setItems(blockedData);
        blockedTable.getColumns().addAll(generateUserColumns());
        friendsTable.refresh();
        blockedTable.refresh();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sceneManager = Main.getSceneManager();
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

        pendingTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<FriendRequestData>() {
            @Override
            public void changed(ObservableValue<? extends FriendRequestData> observableValue, FriendRequestData friendRequestData, FriendRequestData t1) {
                pending = pendingTable.getSelectionModel().getSelectedItem();
            }
        });

        requestTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<FriendRequestData>() {
            @Override
            public void changed(ObservableValue<? extends FriendRequestData> observableValue, FriendRequestData friendRequestData, FriendRequestData t1) {
                request = requestTable.getSelectionModel().getSelectedItem();
            }
        });
    }
}
