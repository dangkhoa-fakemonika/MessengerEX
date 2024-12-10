package org.example.mesexadmin.ui.user_level;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.example.mesexadmin.PopUpController;
import org.example.mesexadmin.SceneManager;
import org.example.mesexadmin.data_class.MessageData;
import org.example.mesexadmin.data_class.UserData;
import org.example.mesexadmin.ui.elements.MessageListComponent;
import org.example.mesexadmin.ui.elements.UserListComponent;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class SingleGroupManagerController implements Initializable {
    SceneManager sceneManager;

    @FXML
    private ListView<UserListComponent> memberList;
    @FXML
    private ListView<UserListComponent> modList;
    @FXML
    private ListView<MessageListComponent> chat;

    // Replace this with new data
    ObservableList<UserListComponent> observableList1 = FXCollections.observableArrayList(
            new UserListComponent(new UserData("user1")) ,
            new UserListComponent(new UserData("user2")) ,
            new UserListComponent(new UserData("user3"))
    );
    ObservableList<UserListComponent> observableList2 = FXCollections.observableArrayList(
            new UserListComponent(new UserData("mod1")) ,
            new UserListComponent(new UserData("mod2")) ,
            new UserListComponent(new UserData("mod3"))
    );
    ObservableList<MessageListComponent> observableList3 =
            FXCollections.observableArrayList(
                    new MessageListComponent(new MessageData("sample message 1", "a", "b")),
                    new MessageListComponent(new MessageData("sample message 3", "a", "b")),
                    new MessageListComponent(new MessageData("sample message 4", "a", "b")),
                    new MessageListComponent(new MessageData("sample message 6", "a", "b"))
            );

    public void returnToMain(ActionEvent actionEvent) throws IOException {
        sceneManager.addScene("Main", "main-messaging.fxml");
        sceneManager.switchScene("Main");
    }

    public void addMember(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("pop-up-add-member.fxml"));
        Dialog<Objects> dialog = new Dialog<>();
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        DialogPane dialogPane = loader.load();
        PopUpController popUpController = loader.getController();
        popUpController.currentDialog = dialog;
        dialog.setDialogPane(dialogPane);
        dialog.showAndWait();
        dialog.close();
    }

    public void addMod(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("pop-up-add-mod.fxml"));
        Dialog<Objects> dialog = new Dialog<>();
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        DialogPane dialogPane = loader.load();
        PopUpController popUpController = loader.getController();
        popUpController.currentDialog = dialog;
        dialog.setDialogPane(dialogPane);
        dialog.showAndWait();
        dialog.close();
    }

    public void removeUser(ActionEvent actionEvent) {
        Alert newAlert = new Alert(Alert.AlertType.CONFIRMATION);
        newAlert.setContentText("Remove this user?");
        newAlert.setHeaderText("Remove User");
        newAlert.showAndWait();
    }

    public void leaveGroup(ActionEvent actionEvent) {
        Alert newAlert = new Alert(Alert.AlertType.CONFIRMATION);
        newAlert.setContentText("Leave this group?");
        newAlert.setHeaderText("Leave Group");
        newAlert.showAndWait();
    }

    public void deleteGroup(ActionEvent actionEvent) {
        Alert newAlert = new Alert(Alert.AlertType.CONFIRMATION);
        newAlert.setContentText("Delete this group?");
        newAlert.setHeaderText("Delete Group");
        newAlert.showAndWait();
    }

    public void goToChat(ActionEvent actionEvent) throws IOException {
        sceneManager.addScene("ChatHistoryManagement", "main-chat-history-management.fxml");
        sceneManager.switchScene("ChatHistoryManagement");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        memberList.getItems().addAll(observableList1);
        modList.getItems().addAll(observableList2);
        chat.getItems().addAll(observableList3);
    }


}
