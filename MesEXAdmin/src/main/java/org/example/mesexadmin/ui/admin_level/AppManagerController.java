package org.example.mesexadmin.ui.admin_level;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.mesexadmin.Main;
import org.example.mesexadmin.SceneManager;
import org.example.mesexadmin.data_class.ActivityData;
import org.example.mesexadmin.data_class.UserData;
import org.example.mesexadmin.ui.ControllerWrapper;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AppManagerController implements ControllerWrapper {
    static SceneManager sceneManager;

    @FXML
    private TableView<ActivityData> userTable;
    @FXML
    private TableView<UserData> loginTable;
    @FXML
    private TableView<UserData> newAccountTable;
    @FXML
    private TableView<UserData> socialTable;
    @FXML
    private TableView<UserData> activeTable;

    final ObservableList<ActivityData> data = FXCollections.observableArrayList(
            new ActivityData("user2", "12:00:00 13-11-2024", "login"),
            new ActivityData("user2", "12:00:00 12-11-2024", "reset_password"),
            new ActivityData("user1", "12:00:00 11-11-2024", "login"),
            new ActivityData("user2", "12:00:00 10-11-2024", "register"),
            new ActivityData("user1", "12:00:00 9-11-2024", "register")
    );


    ObservableList<TableColumn<ActivityData, String>> generateActivityColumns(){
        TableColumn<ActivityData, String> idCol = new TableColumn<>("ID");
        idCol.setMinWidth(250);
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<ActivityData, String> timeCol = new TableColumn<>("Time");
        timeCol.setMinWidth(250);
        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
        TableColumn<ActivityData, String> actionCol = new TableColumn<>("Action Performed");
        actionCol.setMinWidth(300);
        actionCol.setCellValueFactory(new PropertyValueFactory<>("action"));

        return FXCollections.observableArrayList(idCol, timeCol, actionCol);
    }

    ObservableList<TableColumn<UserData, String>> generateLoginColumns(){
        TableColumn<UserData, String> idCol = new TableColumn<>("Username");
        idCol.setMinWidth(250);
//        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<UserData, String> timeCol = new TableColumn<>("Full name");
        timeCol.setMinWidth(250);
//        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
        TableColumn<UserData, String> actionCol = new TableColumn<>("Time");
        actionCol.setMinWidth(300);
//        actionCol.setCellValueFactory(new PropertyValueFactory<>("action"));

        return FXCollections.observableArrayList(idCol, timeCol, actionCol);
    }

    ObservableList<TableColumn<UserData, String>> generateNewUsersColumns(){
        TableColumn<UserData, String> idCol = new TableColumn<>("Username");
        idCol.setMinWidth(250);
//        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<UserData, String> timeCol = new TableColumn<>("Full name");
        timeCol.setMinWidth(250);
//        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
        TableColumn<UserData, String> actionCol = new TableColumn<>("Time");
        actionCol.setMinWidth(300);
//        actionCol.setCellValueFactory(new PropertyValueFactory<>("action"));

        return FXCollections.observableArrayList(idCol, timeCol, actionCol);
    }

    ObservableList<TableColumn<UserData, String>> generateFriendColumns(){
        TableColumn<UserData, String> idCol = new TableColumn<>("Username");
        idCol.setMinWidth(160);
//        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<UserData, String> timeCol = new TableColumn<>("Full name");
        timeCol.setMinWidth(160);
//        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
        TableColumn<UserData, String> actionCol = new TableColumn<>("Time");
        actionCol.setMinWidth(160);
//        actionCol.setCellValueFactory(new PropertyValueFactory<>("action"));
        TableColumn<UserData, String> directFriendCol = new TableColumn<>("Direct Friends");
        directFriendCol.setMinWidth(160);
        TableColumn<UserData, String> indirectFriendCol = new TableColumn<>("Indirect Friends");
        indirectFriendCol.setMinWidth(160);

        return FXCollections.observableArrayList(idCol, timeCol, actionCol, directFriendCol, indirectFriendCol);
    }

    ObservableList<TableColumn<UserData, String>> generatePersonalActiveColumns(){
        TableColumn<UserData, String> idCol = new TableColumn<>("Username");
        idCol.setMinWidth(160);
        TableColumn<UserData, String> actionCol = new TableColumn<>("Time");
        actionCol.setMinWidth(160);
        TableColumn<UserData, String> timeCol = new TableColumn<>("App Open Frequency");
        timeCol.setMinWidth(160);
        TableColumn<UserData, String> directFriendCol = new TableColumn<>("Groups Chat");
        directFriendCol.setMinWidth(160);
        TableColumn<UserData, String> indirectFriendCol = new TableColumn<>("Private Chat");
        indirectFriendCol.setMinWidth(160);

        return FXCollections.observableArrayList(idCol, timeCol, actionCol, directFriendCol, indirectFriendCol);
    }

    public void returnToMain(ActionEvent actionEvent) throws IOException {
        sceneManager.addScene("Main", "main-messaging.fxml");
        sceneManager.switchScene("Main");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sceneManager = Main.getSceneManager();
    }

    @Override
    public void myInitialize() {
        userTable.setItems(FXCollections.observableArrayList());
        loginTable.setItems(FXCollections.observableArrayList());
        newAccountTable.setItems(FXCollections.observableArrayList());
        socialTable.setItems(FXCollections.observableArrayList());
        activeTable.setItems(FXCollections.observableArrayList());
        userTable.getColumns().clear();
        loginTable.getColumns().clear();
        newAccountTable.getColumns().clear();
        socialTable.getColumns().clear();
        activeTable.getColumns().clear();


        userTable.setItems(data);
        userTable.getColumns().addAll(generateActivityColumns());
        loginTable.getColumns().addAll(generateLoginColumns());
        newAccountTable.getColumns().addAll(generateNewUsersColumns());
        socialTable.getColumns().addAll(generateFriendColumns());
        activeTable.getColumns().addAll(generatePersonalActiveColumns());
    }
}
