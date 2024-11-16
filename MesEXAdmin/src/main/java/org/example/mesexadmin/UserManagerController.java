package org.example.mesexadmin;

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
import org.example.mesexadmin.data_class.UserData;

import java.net.URL;
import java.util.ResourceBundle;

public class UserManagerController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;


    @FXML
    private TableView<UserData> userTable;
    @FXML
    private TableView pendingTable;
    @FXML
    private TableView blockedTable;

    private TableColumn<UserData, String> nameCol;
    private TableColumn<UserData, String> idCol;
    private TableColumn<UserData, String> statusCol;
    private TableColumn<UserData, String> emailCol;
    private TableColumn<UserData, String> lastActiveCol;
    private TableColumn<UserData, String> frCountCol;
    private TableColumn<UserData, String> cDateCol;


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

        userTable.setItems(data);
        userTable.getColumns().addAll(nameCol, idCol, emailCol, statusCol, cDateCol, lastActiveCol, frCountCol);
    }
}
