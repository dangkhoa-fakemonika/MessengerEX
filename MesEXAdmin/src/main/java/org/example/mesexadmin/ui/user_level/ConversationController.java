package org.example.mesexadmin.ui.user_level;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.mesexadmin.Main;
import org.example.mesexadmin.PopUpController;
import org.example.mesexadmin.SceneManager;
import org.example.mesexadmin.data_class.ConversationData;
import org.example.mesexadmin.ui.ControllerWrapper;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ConversationController implements ControllerWrapper {
    static SceneManager sceneManager;
    @FXML
    private TableView<ConversationData> allGroupTable;
    @FXML
    private TableView<ConversationData> myGroupTable;

    static ObservableList<ConversationData> data = FXCollections.observableArrayList(
            new ConversationData("T1", "faker"),
            new ConversationData("Gen.G", "chovy"),
            new ConversationData( "GAM Esports", "levi")
    );

    static ObservableList<ConversationData> inactiveData = FXCollections.observableArrayList(
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

    public void configureGroup(ActionEvent actionEvent) throws IOException {
        sceneManager.addScene("ThisGroupManager", "main-single-group-manager.fxml");
        sceneManager.switchScene("ThisGroupManager");
    }

    public void leaveGroup(ActionEvent actionEvent) {
        Alert newAlert = new Alert(Alert.AlertType.CONFIRMATION);
        newAlert.setContentText("Leave this group?");
        newAlert.setHeaderText("Leave Group");
        newAlert.showAndWait();
    }

    public void addMember(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("pop-up-add-member.fxml"));
        Dialog<Objects> dialog = new Dialog<>();
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        DialogPane dialogPane = loader.load();
        PopUpController popUpController = loader.getController();
        popUpController.currentDialog = dialog;
        dialog.setDialogPane(dialogPane);
        dialog.showAndWait();
        dialog.close();
    }

    public void addGroup(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("pop-up-create-group.fxml"));
        Dialog<Objects> dialog = new Dialog<>();
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        DialogPane dialogPane = loader.load();
        PopUpController popUpController = loader.getController();
        popUpController.currentDialog = dialog;
        dialog.setDialogPane(dialogPane);
        dialog.showAndWait();
        dialog.close();
    }

    public void renameGroup(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("pop-up-change-group-name.fxml"));
        Dialog<Objects> dialog = new Dialog<>();
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        DialogPane dialogPane = loader.load();
        PopUpController popUpController = loader.getController();
        popUpController.currentDialog = dialog;
        dialog.setDialogPane(dialogPane);
        dialog.showAndWait();
        dialog.close();
    }

    @Override
    public void myInitialize() {
        myGroupTable.setItems(FXCollections.observableArrayList());
        allGroupTable.setItems(FXCollections.observableArrayList());
        myGroupTable.getColumns().clear();
        allGroupTable.getColumns().clear();

        myGroupTable.setItems(data);
        myGroupTable.getColumns().addAll(generateColumns());

        allGroupTable.setItems(inactiveData);
        allGroupTable.getColumns().addAll(generateColumns());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sceneManager = Main.getSceneManager();
    }
}
