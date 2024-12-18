package org.example.mesexadmin.ui.user_level;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import org.bson.types.ObjectId;
import org.example.mesexadmin.Main;
import org.example.mesexadmin.PopUpController;
import org.example.mesexadmin.SceneManager;
import org.example.mesexadmin.SessionUser;
import org.example.mesexadmin.data_class.ConversationData;
import org.example.mesexadmin.data_class.UserData;
import org.example.mesexadmin.ui.ControllerWrapper;
import org.example.mesexadmin.ui.elements.UserListComponent;

import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class SingleGroupManagerController implements ControllerWrapper {
    static SceneManager sceneManager;
    static SessionUser currentUser;
    static ConversationData thisConversation;

    @FXML private ListView<UserListComponent> memberList;
    @FXML private ListView<UserListComponent> modList;
    @FXML private Button changeGroupName;
    @FXML private TextField getGroupName;
    @FXML private DatePicker getDateCreated;
    @FXML private Button leaveGroupButton;
    @FXML private Button addMemberButton;
    @FXML private Button addModButton;
    @FXML private Button removeMemberButton;

    static UserData selectedUser;

    // Replace this with new data
    ObservableList<UserListComponent> memberItems = FXCollections.observableArrayList();
    ObservableList<UserListComponent> moderatorItems = FXCollections.observableArrayList();

    public void returnToMain(ActionEvent actionEvent) throws IOException {
        sceneManager.addScene("Main", "main-messaging.fxml");
        sceneManager.switchScene("Main");
    }

    public void viewMessages(ActionEvent actionEvent) throws IOException {
        sceneManager.addScene("ChatManagement", "main-chat-history-management.fxml");
        sceneManager.switchScene("ChatManagement");
    }

    void refresh(){
        memberList.getItems().clear();
        modList.getItems().clear();

        ObjectId renewId = thisConversation.getConversationId();
        ConversationData renewConversation = currentUser.myQuery.conversations().getConversation(renewId);
        if (renewConversation != null)
            thisConversation = renewConversation;
        ArrayList<ObjectId> membersId = thisConversation.getMembersId();
        ArrayList<ObjectId> moderatorsId = thisConversation.getModeratorsId();
        ArrayList<UserData> members = currentUser.myQuery.users().getUserList(membersId);
        ArrayList<UserData> moderators = currentUser.myQuery.users().getUserList(moderatorsId);

        memberItems.clear();
        moderatorItems.clear();

        members.forEach((a) -> memberItems.add(new UserListComponent(a)));
        moderators.forEach((a) -> moderatorItems.add(new UserListComponent(a)));

        memberList.getItems().addAll(memberItems);
        modList.getItems().addAll(moderatorItems);
        memberList.refresh();
        modList.refresh();
    }

    @Override
    public void myInitialize() {
        currentUser = Main.getCurrentUser();
        thisConversation = MessagingController.currentConversation;

        refresh();
        if (!thisConversation.getModeratorsId().contains(currentUser.getSessionUserData().getUserId())){
            addModButton.setDisable(true);
            removeMemberButton.setDisable(true);
        }

        getDateCreated.setValue(Instant.ofEpochMilli(thisConversation.getDateCreated().getTime()).atZone(ZoneId.systemDefault()).toLocalDate());
        getGroupName.setText(thisConversation.getConversationName());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sceneManager = Main.getSceneManager();

        memberList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<UserListComponent>() {
            @Override
            public void changed(ObservableValue<? extends UserListComponent> observableValue, UserListComponent userListComponent, UserListComponent t1) {
                UserListComponent u = memberList.getSelectionModel().getSelectedItem();
                if (u != null){
                    modList.getSelectionModel().clearSelection();
                    selectedUser = u.getUser();

                    if (thisConversation.getModeratorsId().contains(currentUser.getSessionUserData().getUserId())){
                        addModButton.setDisable(false);
                        removeMemberButton.setDisable(false);
                    }
                }
            }
        });

        modList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<UserListComponent>() {
            @Override
            public void changed(ObservableValue<? extends UserListComponent> observableValue, UserListComponent userListComponent, UserListComponent t1) {
                UserListComponent u = modList.getSelectionModel().getSelectedItem();
                if (u != null){
                    memberList.getSelectionModel().clearSelection();
                    selectedUser = u.getUser();
                    if (thisConversation.getModeratorsId().contains(currentUser.getSessionUserData().getUserId())) {
                        addModButton.setDisable(true);
                        removeMemberButton.setDisable(false);
                    }
                }
            }
        });



        addMemberButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                FXMLLoader loader = new FXMLLoader((Main.class.getResource("pop-up-add-member.fxml")));
                Dialog<Objects> dialog = new Dialog<>();

                try {
                    DialogPane dialogPane = loader.load();
                    dialog.setDialogPane(dialogPane);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                PopUpController popUpController = loader.getController();
                ButtonType confirmButtonType = new ButtonType("Confirm", ButtonBar.ButtonData.YES);
                ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, cancelButtonType);

                final Button confirmButton = (Button) dialog.getDialogPane().lookupButton(confirmButtonType);
                confirmButton.addEventFilter(ActionEvent.ACTION, event -> {
                    String username = popUpController.getUsernameField();
                    if (username.isEmpty()) {
                        new Alert(Alert.AlertType.ERROR, "The field must not be empty!").showAndWait();
                        event.consume();
                    } else{
                        UserData fetchUser = currentUser.myQuery.users().getUserByUsername(username);
                        if (fetchUser != null && currentUser.myQuery.conversations().addMember(thisConversation.getConversationId(), fetchUser.getUserId())) {
                            new Alert(Alert.AlertType.INFORMATION, username + " added!").showAndWait();
                            refresh();
                        } else {
                            new Alert(Alert.AlertType.ERROR, "Can't add member").showAndWait();
                            event.consume();
                        }
                    }

                    popUpController.clearAllFields();
                });

                dialog.showAndWait();
                dialog.close();
            }
        });

        addModButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText("Promote " + selectedUser.getUsername() + " to moderator? (can't undo)");
                alert.setTitle("Promote to moderator");

                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        if (currentUser.myQuery.conversations().addModerator(thisConversation.getConversationId(), selectedUser.getUserId())) {
                            new Alert(Alert.AlertType.INFORMATION, selectedUser.getUsername() + " added to mod!").showAndWait();
                            refresh();
                        }
                        else {
                            new Alert(Alert.AlertType.ERROR, "Can't promote user.").showAndWait();
                        }
                    }
                });
            }
        });

        removeMemberButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                if (!selectedUser.getUserId().equals(currentUser.getSessionUserData().getUserId())){
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setHeaderText("Kick " + selectedUser.getUsername() + " from this group?");
                    alert.setTitle("Kick member");

                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            if (currentUser.myQuery.conversations().removeMember(thisConversation.getConversationId(), selectedUser.getUserId())) {
                                new Alert(Alert.AlertType.INFORMATION, selectedUser.getUsername() + " added!").showAndWait();
                                refresh();
                            }
                            else {
                                new Alert(Alert.AlertType.ERROR, "Can't kick user").showAndWait();
                            }
                        }
                    });
                } else {
                    new Alert(Alert.AlertType.ERROR, "You can't kick yourself!").showAndWait();
                }

            }
        });

        leaveGroupButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText("Leave this group?");
                alert.setTitle("Leave Group");

                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        if (currentUser.myQuery.conversations().removeMember(thisConversation.getConversationId(), currentUser.getSessionUserData().getUserId())) {
                            try {
                                new Alert(Alert.AlertType.INFORMATION, "You left.").showAndWait();
                                sceneManager.addScene("Main", "main-login.fxml");
                                sceneManager.switchScene("Main");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        else {
                            new Alert(Alert.AlertType.ERROR, "Can't leave right now.").showAndWait();
                        }
                    }
                });
            }
        });

        changeGroupName.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                FXMLLoader loader = new FXMLLoader((Main.class.getResource("pop-up-change-group-name.fxml")));
                Dialog<Objects> dialog = new Dialog<>();

                try {
                    DialogPane dialogPane = loader.load();
                    dialog.setDialogPane(dialogPane);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                PopUpController popUpController = loader.getController();
                ButtonType confirmButtonType = new ButtonType("Confirm", ButtonBar.ButtonData.YES);
                ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, cancelButtonType);
                popUpController.setGroupName(thisConversation.getConversationName());

                final Button confirmButton = (Button) dialog.getDialogPane().lookupButton(confirmButtonType);
                confirmButton.addEventFilter(ActionEvent.ACTION, event -> {
                    String groupName = popUpController.getGroupNameField();
                    if (groupName.isEmpty()) {
                        new Alert(Alert.AlertType.ERROR, "The field must not be empty!").showAndWait();
                        event.consume();
                    } else if (currentUser.myQuery.conversations().changeConversationName(thisConversation.getConversationId(), groupName)) {
                        getGroupName.setText(groupName);
                    } else{
                        new Alert(Alert.AlertType.ERROR, "Can't change name at the moment.").showAndWait();
                        event.consume();
                    }

                    popUpController.clearAllFields();
                });

                dialog.showAndWait();
                dialog.close();
            }
        });
    }


}
