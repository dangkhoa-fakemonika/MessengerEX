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
import org.example.mesexadmin.data_class.GroupData;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class GroupManagerController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TableView<GroupData> activeGroupTable;
    @FXML
    private TableView<GroupData> inactiveGroupTable;

    void bufferScene(ActionEvent actionEvent){
//        System.out.println(actionEvent.getSource());
        stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    final ObservableList<GroupData> data = FXCollections.observableArrayList(
        new GroupData("group1", "T1", "faker"),
        new GroupData("group2", "Gen.G", "chovy"),
        new GroupData("group3", "GAM Esports", "levi")
    );

    final ObservableList<GroupData> inactiveData = FXCollections.observableArrayList(
            new GroupData("group1", "SKT T1", "faker?"),
            new GroupData("group2", "Samsung Galaxy", "cuvee")
    );

    ObservableList<TableColumn<GroupData, String>> generateColumns(){
        TableColumn<GroupData, String> groupIdCol;
        TableColumn<GroupData, String> groupNameCol;
        TableColumn<GroupData, String> hostCol;
        TableColumn<GroupData, String> participantCol;

        groupIdCol = new TableColumn<>("Group Name");
        groupNameCol = new TableColumn<>("Group ID");
        hostCol = new TableColumn<>("Host");
        participantCol = new TableColumn<>("Participants");

        groupIdCol.setCellValueFactory(new PropertyValueFactory<>("groupName"));
        groupNameCol.setCellValueFactory(new PropertyValueFactory<>("groupID"));
        hostCol.setCellValueFactory(new PropertyValueFactory<>("hostID"));
        participantCol.setCellValueFactory(new PropertyValueFactory<>("participantIDs"));

        groupIdCol.setMinWidth(150);
        groupNameCol.setMinWidth(150);
        hostCol.setMinWidth(150);
        participantCol.setMinWidth(350);

        return FXCollections.observableArrayList(groupNameCol, groupIdCol, hostCol, participantCol);
    }

    public void returnToMain(ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main-messaging.fxml")));
        bufferScene(actionEvent);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        activeGroupTable.setItems(data);
        activeGroupTable.getColumns().addAll(generateColumns());

        inactiveGroupTable.setItems(inactiveData);
        inactiveGroupTable.getColumns().addAll(generateColumns());
    }
}
