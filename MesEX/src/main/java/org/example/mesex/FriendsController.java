package org.example.mesex;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class FriendsController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TableView<UserData> friendsTable;
    @FXML
    private TableView pendingTable;
    @FXML
    private TableView blockedTable;

    private TableColumn<UserData, String> nameCol;
    private TableColumn<UserData, String> idCol;
    private TableColumn<UserData, String> statusCol;

    public void addData(UserData user){


    }

    final ObservableList<UserData> data = FXCollections.observableArrayList(
            new UserData("FakeMonika", "fakemonika", "example@email.com"),
            new UserData("KanCh", "kanch", "example@email.com"),
            new UserData("Ryan Gosling", "him", "example@email.com")
    );

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameCol = new TableColumn<>("Name");
        idCol = new TableColumn<>("Id");
        statusCol = new TableColumn<>("Status");

        nameCol.setCellValueFactory(
                new PropertyValueFactory<>("name")
        );
        idCol.setCellValueFactory(
                new PropertyValueFactory<>("id")
        );
        statusCol.setCellValueFactory(
                new PropertyValueFactory<>("status")
        );

        nameCol.setMinWidth(150);
        idCol.setMinWidth(150);
        statusCol.setMinWidth(600);

        friendsTable.setItems(data);
        friendsTable.getColumns().addAll(nameCol, idCol, statusCol);
    }
}
