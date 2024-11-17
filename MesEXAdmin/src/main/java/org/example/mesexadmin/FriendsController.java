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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.mesexadmin.data_class.UserData;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
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
