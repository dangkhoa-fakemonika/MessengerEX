package org.example.mesexadmin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.mesexadmin.data_class.UserData;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class UserManagerController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;


    @FXML
    private TableView<UserData> userTable;
    @FXML
    private TableView<UserData> spamTable;
    @FXML
    private TableView<UserData> bannedTable;

    void bufferScene(ActionEvent actionEvent){
//        System.out.println(actionEvent.getSource());
        stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

//    ObservableList<UserData> spamData = FXCollections.observableArrayList(new ArrayList<>());
//    ObservableList<UserData> bannedData = FXCollections.observableArrayList(new ArrayList<>());


    final ObservableList<UserData> data = FXCollections.observableArrayList(
            new UserData("FakeMonika", "fakemonika", "example@email.com", "Active"),
            new UserData("KanCh", "kanch", "example@email.com", "Active"),
            new UserData("Ryan Gosling", "him", "example@email.com", "Offline")
    );

    final ObservableList<UserData> spamData = FXCollections.observableArrayList(
            new UserData("FakeMonika2", "fakemonika4", "example@email.com", "Reported"),
            new UserData("KanCh2", "kanch4", "example@email.com", "Reported")
    );

    final ObservableList<UserData> bannedData = FXCollections.observableArrayList(
            new UserData("FakeMonika4", "fakemonika4", "example@email.com", "Banned"),
            new UserData("KanCh4", "kanch4", "example@email.com", "Banned")
    );

    public ObservableList<TableColumn<UserData, String>> generateColumns(){
        TableColumn<UserData, String> nameCol;
        TableColumn<UserData, String> idCol;
        TableColumn<UserData, String> statusCol;
        TableColumn<UserData, String> emailCol;
        TableColumn<UserData, String> lastActiveCol;
        TableColumn<UserData, String> frCountCol;
        TableColumn<UserData, String> cDateCol;

        nameCol = new TableColumn<>("Name");
        idCol = new TableColumn<>("Id");
        lastActiveCol = new TableColumn<>("Last Active");
        emailCol = new TableColumn<>("Email");
        frCountCol = new TableColumn<>("Friends");
        statusCol = new TableColumn<>("Status");
        cDateCol = new TableColumn<>("Date Created");

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        lastActiveCol.setCellValueFactory(new PropertyValueFactory<>("lastActive"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        frCountCol.setCellValueFactory(new PropertyValueFactory<>("friendCount"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        cDateCol.setCellValueFactory(new PropertyValueFactory<>("dateCreated"));

        nameCol.setMinWidth(100);
        idCol.setMinWidth(100);
        lastActiveCol.setMinWidth(80);
        emailCol.setMinWidth(260);
        cDateCol.setMinWidth(80);
        frCountCol.setMinWidth(80);
        statusCol.setMinWidth(80);

        return FXCollections.observableArrayList(nameCol, idCol, emailCol, statusCol, cDateCol, lastActiveCol, frCountCol);
    }

    public void returnToMain(ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main-messaging.fxml")));
        bufferScene(actionEvent);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        userTable.setItems(data);
        userTable.getColumns().addAll(generateColumns());
        spamTable.setItems(spamData);
        spamTable.getColumns().addAll(generateColumns());
        bannedTable.setItems(bannedData);
        bannedTable.getColumns().addAll(generateColumns());

    }
}
