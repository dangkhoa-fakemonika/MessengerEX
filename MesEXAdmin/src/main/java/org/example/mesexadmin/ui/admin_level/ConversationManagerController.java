package org.example.mesexadmin.ui.admin_level;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.mesexadmin.Main;
import org.example.mesexadmin.SceneManager;
import org.example.mesexadmin.data_class.ConversationData;
import org.example.mesexadmin.ui.ControllerWrapper;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ConversationManagerController implements ControllerWrapper {
    static SceneManager sceneManager;
    @FXML
    private TableView<ConversationData> activeGroupTable;
    @FXML
    private TableView<ConversationData> inactiveGroupTable;
    @FXML
    private Button deleteGroup, createGroup, addUser, details;

    static ConversationData selectedGroup;

    final ObservableList<ConversationData> data = FXCollections.observableArrayList(
        new ConversationData("T1", "faker"),
        new ConversationData("Gen.G", "chovy"),
        new ConversationData( "GAM Esports", "levi")
    );

    final ObservableList<ConversationData> inactiveData = FXCollections.observableArrayList(
            new ConversationData("SKT T1", "faker?"),
            new ConversationData("Samsung Galaxy", "cuvee")
    );

    ObservableList<TableColumn<ConversationData, String>> generateColumns(){
        TableColumn<ConversationData, String> groupNameCol;
        TableColumn<ConversationData, String> hostCol;
        TableColumn<ConversationData, String> cDateCol;
        TableColumn<ConversationData, String> participantCol;

        groupNameCol = new TableColumn<>("Conversation Name");
        cDateCol = new TableColumn<>("Date Created");
        hostCol = new TableColumn<>("Hosts");
        participantCol = new TableColumn<>("Participants");

        groupNameCol.setCellValueFactory(new PropertyValueFactory<>("conversationName"));
        cDateCol.setCellValueFactory(new PropertyValueFactory<>("dateCreated"));
        hostCol.setCellValueFactory(new PropertyValueFactory<>("moderatorsId"));
        participantCol.setCellValueFactory(new PropertyValueFactory<>("membersId"));

        cDateCol.setMinWidth(150);
        groupNameCol.setMinWidth(150);
        hostCol.setMinWidth(150);
        participantCol.setMinWidth(350);

        return FXCollections.observableArrayList(groupNameCol, cDateCol, hostCol, participantCol);
    }

    public void returnToMain(ActionEvent actionEvent) throws IOException {
        sceneManager.addScene("Main", "main-messaging.fxml");
        sceneManager.switchScene("Main");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sceneManager = Main.getSceneManager();
        activeGroupTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ConversationData>() {
            @Override
            public void changed(ObservableValue<? extends ConversationData> observableValue, ConversationData conversationData, ConversationData t1) {
                selectedGroup = activeGroupTable.getSelectionModel().getSelectedItem();
            }
        });

        inactiveGroupTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ConversationData>() {
            @Override
            public void changed(ObservableValue<? extends ConversationData> observableValue, ConversationData conversationData, ConversationData t1) {
                selectedGroup = inactiveGroupTable.getSelectionModel().getSelectedItem();
            }
        });
    }

    @Override
    public void myInitialize() {
        activeGroupTable.setItems(FXCollections.observableArrayList());
        inactiveGroupTable.setItems(FXCollections.observableArrayList());
        activeGroupTable.getColumns().clear();
        inactiveGroupTable.getColumns().clear();
        activeGroupTable.setItems(data);
        activeGroupTable.getColumns().addAll(generateColumns());
        inactiveGroupTable.setItems(inactiveData);
        inactiveGroupTable.getColumns().addAll(generateColumns());
    }
}
