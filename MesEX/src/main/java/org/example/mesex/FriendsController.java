package org.example.mesex;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.mesex.FriendRequestData;
import org.example.mesex.SpamTicketData;
import org.example.mesex.UserData;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class FriendsController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TableView<UserData> friendsTable;
    @FXML
    private TableView<FriendRequestData> pendingTable;
    @FXML
    private TableView<FriendRequestData> requestTable;
    @FXML
    private TableView<UserData> blockedTable;

    private TableColumn<UserData, String> nameCol;
    private TableColumn<UserData, String> idCol;
    private TableColumn<UserData, String> statusCol;


    void bufferScene(ActionEvent actionEvent){
//        System.out.println(actionEvent.getSource());
        stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

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

    public void blockUser(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("pop-up-block.fxml"));
        Dialog<Objects> dialog = new Dialog<>();
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        DialogPane dialogPane = loader.load();
        PopUpController popUpController = loader.getController();
        popUpController.currentDialog = dialog;
        dialog.setDialogPane(dialogPane);
        dialog.showAndWait();
        dialog.close();
    }

    public void returnToMain(ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main-messaging.fxml")));
        bufferScene(actionEvent);
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

    final ObservableList<FriendRequestData> requestData = FXCollections.observableArrayList(new ArrayList<>());
    final ObservableList<FriendRequestData> pendingData = FXCollections.observableArrayList(new ArrayList<>());


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
    public void initialize(URL url, ResourceBundle resourceBundle) {


        friendsTable.setItems(data);
        friendsTable.getColumns().addAll(generateUserColumns());
        blockedTable.setItems(blockedData);
        blockedTable.getColumns().addAll(generateUserColumns());

    }
}
