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
import org.example.mesexadmin.data_class.GroupData;
import org.example.mesexadmin.data_class.UserData;

import java.net.URL;
import java.util.ResourceBundle;

public class GroupManagerController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TableView<GroupData> activeGroupTable;
    @FXML
    private TableView<GroupData> inactiveGroupTable;

    private TableColumn<GroupData, String> groupIdCol;
    private TableColumn<GroupData, String> groupNameCol;
    private TableColumn<GroupData, String> hostCol;
    private TableColumn<GroupData, String> participantCol;

    final ObservableList<GroupData> data = FXCollections.observableArrayList(
        new GroupData("group1", "T1", "faker"),
        new GroupData("group2", "Gen.G", "chovy"),
        new GroupData("group3", "GAM Esports", "levi")
    );


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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

        activeGroupTable.setItems(data);
        activeGroupTable.getColumns().addAll(groupNameCol, groupIdCol, hostCol, participantCol);

    }
}
