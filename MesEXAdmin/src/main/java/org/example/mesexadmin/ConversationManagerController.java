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
import org.example.mesexadmin.data_class.ConversationData;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ConversationManagerController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TableView<ConversationData> activeGroupTable;
    @FXML
    private TableView<ConversationData> inactiveGroupTable;

    void bufferScene(ActionEvent actionEvent){
//        System.out.println(actionEvent.getSource());
        stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

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
