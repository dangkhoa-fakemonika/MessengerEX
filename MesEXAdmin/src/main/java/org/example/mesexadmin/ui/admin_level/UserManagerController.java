package org.example.mesexadmin.ui.admin_level;

import javafx.animation.PauseTransition;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.util.Duration;
import org.example.mesexadmin.Main;
import org.example.mesexadmin.PopUpController;
import org.example.mesexadmin.SceneManager;
import org.example.mesexadmin.SessionUser;
import org.example.mesexadmin.data_class.ActivityData;
import org.example.mesexadmin.data_class.UserData;
import org.example.mesexadmin.ui.ControllerWrapper;
import org.example.mesexadmin.ui.elements.ActivityListComponent;
import org.example.mesexadmin.ui.elements.UserListComponent;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.ResourceBundle;

public class UserManagerController implements ControllerWrapper {
    static SceneManager sceneManager;
    static UserData selectedUser;
    static SessionUser currentUser;

    @FXML
    private TableView<UserData> userTable;
    @FXML
    private TableColumn<UserData, String> usernameCol;
    @FXML
    private TableColumn<UserData, String> displayNameCol;
    @FXML
    private TableColumn<UserData, String> statusCol;
    @FXML
    private TableColumn<UserData, Date> dayCreatedCol;
    @FXML
    private Button addUser, banUser, deleteUser, editDetails, resetPassword, changePassword;
    @FXML
    private ListView<UserListComponent> friends;
    @FXML
    private ListView<ActivityListComponent> loginTimes;
    @FXML
    private ChoiceBox<String> userFilter;
    final static ObservableList<String> filterOptions = FXCollections.observableArrayList("None", "username", "displayName", "status");
    static String currentFilter;
    @FXML
    private TextField filterField;

    PauseTransition filterPause;

    final ObservableList<UserData> userData = FXCollections.observableArrayList();
    final ObservableList<UserListComponent> friendsData = FXCollections.observableArrayList();
    final ObservableList<ActivityListComponent> loginData = FXCollections.observableArrayList();


    public void returnToMain(ActionEvent actionEvent) throws IOException {
        sceneManager.addScene("Main", "main-messaging.fxml");
        sceneManager.switchScene("Main");
    }

    @Override
    public void myInitialize() {
        currentUser = Main.getCurrentUser();
        userTable.setItems(userData);
        userFilter.setValue("None");
        filterField.setDisable(true);
        friends.setItems(friendsData);
        loginTimes.setItems(loginData);
        banUser.setDisable(true);
        deleteUser.setDisable(true);
        editDetails.setDisable(true);
        resetPassword.setDisable(true);
        changePassword.setDisable(true);

        loadUserData();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sceneManager = Main.getSceneManager();

        filterPause = new PauseTransition(Duration.millis(500));
        filterPause.setOnFinished((e) -> updateData());

        filterField.textProperty().addListener((e) -> {
            filterPause.stop();
            filterPause.playFromStart();
        });

        userFilter.setOnAction((e) -> updateData());

        userTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<UserData>() {
            @Override
            public void changed(ObservableValue<? extends UserData> observableValue, UserData userData, UserData t1) {
                selectedUser = userTable.getSelectionModel().getSelectedItem();
                if (selectedUser != null)
                    loadSelectionData();
            }
        });

        usernameCol.setCellValueFactory((a) -> new SimpleStringProperty(a.getValue().getUsername()));
        displayNameCol.setCellValueFactory((a) -> new SimpleStringProperty(a.getValue().getDisplayName()));
        statusCol.setCellValueFactory((a) -> new SimpleStringProperty(a.getValue().getStatus()));
        dayCreatedCol.setCellValueFactory((a) -> new SimpleObjectProperty<>(a.getValue().getDateCreated()));

        userFilter.setItems(filterOptions);

        banUser.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                if (!selectedUser.getStatus().equals("banned")){
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setHeaderText("Ban " + selectedUser.getUsername() + " ?");
                    alert.setTitle("Ban User");

                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            if (currentUser.myQuery.users().changeUserStatus(selectedUser.getUserId(), "banned")) {
                                new Alert(Alert.AlertType.INFORMATION, selectedUser.getUsername() + " is banned").showAndWait();
                                updateData();
                            }
                            else {
                                new Alert(Alert.AlertType.ERROR, "Can't ban user.").showAndWait();
                            }
                        }
                    });
                } else {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setHeaderText("Remove ban from " + selectedUser.getUsername() + " ?");
                    alert.setTitle("Unban User");

                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            if (currentUser.myQuery.users().changeUserStatus(selectedUser.getUserId(), "offline")) {
                                new Alert(Alert.AlertType.INFORMATION, selectedUser.getUsername() + " is un-banned").showAndWait();
                                updateData();
                            }
                            else {
                                new Alert(Alert.AlertType.ERROR, "Can't un-ban user.").showAndWait();
                            }
                        }
                    });
                }
            }
        });

        deleteUser.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                FXMLLoader loader = new FXMLLoader((Main.class.getResource("pop-up-delete-user.fxml")));
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
                        if (username.equals(selectedUser.getUsername()) && currentUser.myQuery.users().removeUserFromSystem(selectedUser.getUserId())) {
                            new Alert(Alert.AlertType.INFORMATION, username + " deleted!").showAndWait();
                            updateData();
                        } else {
                            new Alert(Alert.AlertType.ERROR, "Can't delete user.").showAndWait();
                            event.consume();
                        }
                    }

                    popUpController.clearAllFields();
                });

                dialog.showAndWait();
                dialog.close();
            }
        });

        resetPassword.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText("Reset " + selectedUser.getUsername() + "'s password via email?");
                alert.setTitle("Reset Password");

                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        if (currentUser.resetUserPassword(selectedUser)) {
                            new Alert(Alert.AlertType.INFORMATION, selectedUser.getUsername() + "'s password is reset.").showAndWait();
                        }
                        else {
                            new Alert(Alert.AlertType.ERROR, "Can't reset user's password").showAndWait();
                        }
                    }
                });
            }
        });

        addUser.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                FXMLLoader loader = new FXMLLoader((Main.class.getResource("pop-up-add-user.fxml")));
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
                    UserData getUser = popUpController.createUserFromInfo();
                    if (getUser.getUsername().isEmpty() || getUser.getEmail().isEmpty()) {
                        new Alert(Alert.AlertType.ERROR, "The fields must not be empty!").showAndWait();
                        event.consume();
                    } else if (popUpController.validatePasswordInput()) {
                        new Alert(Alert.AlertType.ERROR, "Invalid password fields").showAndWait();
                        event.consume();
                    } else {
                        if (currentUser.myQuery.users().insertUser(getUser)) {
                            new Alert(Alert.AlertType.INFORMATION, "New user added!").showAndWait();
                            updateData();
                        } else {
                            new Alert(Alert.AlertType.ERROR, "Can't add user.").showAndWait();
                            event.consume();
                        }
                    }

                    popUpController.clearAllFields();
                });

                dialog.showAndWait();
                dialog.close();
            }
        });

        editDetails.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                FXMLLoader loader = new FXMLLoader((Main.class.getResource("pop-up-modify-user.fxml")));
                Dialog<Objects> dialog = new Dialog<>();

                try {
                    DialogPane dialogPane = loader.load();
                    dialog.setDialogPane(dialogPane);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                PopUpController popUpController = loader.getController();
                popUpController.loadUserInfo(selectedUser);
                ButtonType confirmButtonType = new ButtonType("Confirm", ButtonBar.ButtonData.YES);
                ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, cancelButtonType);

                final Button confirmButton = (Button) dialog.getDialogPane().lookupButton(confirmButtonType);
                confirmButton.addEventFilter(ActionEvent.ACTION, event -> {
                    UserData getUser = popUpController.applyUserEdit(selectedUser);
                    if (currentUser.myQuery.users().updateUser(getUser)) {
                        new Alert(Alert.AlertType.INFORMATION, "User info modified!").showAndWait();
                        updateData();
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Can't update user's data.").showAndWait();
                        event.consume();
                    }

                    popUpController.clearAllFields();
                });

                dialog.showAndWait();
                dialog.close();
            }
        });

        changePassword.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                FXMLLoader loader = new FXMLLoader((Main.class.getResource("pop-up-change-user-password.fxml")));
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
                    if (popUpController.validatePasswordInput()) {
                        new Alert(Alert.AlertType.ERROR, "Invalid password fields").showAndWait();
                        event.consume();
                    } else {
                        if (currentUser.myQuery.users().changeUserPassword(selectedUser.getUserId(), popUpController.getHashedPassword())) {
                            new Alert(Alert.AlertType.INFORMATION, "Password changed!").showAndWait();
                            updateData();
                        } else {
                            new Alert(Alert.AlertType.ERROR, "Can't change user's password").showAndWait();
                            event.consume();
                        }
                    }

                    popUpController.clearAllFields();
                });

                dialog.showAndWait();
                dialog.close();
            }
        });
    }

    public void updateData(){
        banUser.setDisable(true);
        deleteUser.setDisable(true);
        editDetails.setDisable(true);
        resetPassword.setDisable(true);
        changePassword.setDisable(true);
        filterField.setDisable(userFilter.getValue().equals("None"));

        String text = filterField.getText().trim();
        currentFilter = userFilter.getValue();
        if (!text.isEmpty()){
            userData.setAll(currentUser.myQuery.users().getAllUsersFilter(currentFilter, text));
        } else {
            userData.setAll(currentUser.myQuery.users().getAllUsers());
        }
    }

    public void loadSelectionData(){
        banUser.setDisable(false);
        deleteUser.setDisable(false);
        editDetails.setDisable(false);
        resetPassword.setDisable(false);
        changePassword.setDisable(false);

        ArrayList<UserData> getFriends = currentUser.myQuery.users().getUserList(selectedUser.getFriend());
        ArrayList<ActivityData> getLogin = currentUser.myQuery.activities().viewUserLoginHistory(selectedUser.getUserId());
        friendsData.clear();
        getFriends.forEach((a) -> friendsData.add(new UserListComponent(a)));
        loginData.clear();
        getLogin.forEach((a) -> loginData.add(new ActivityListComponent(a)));
    }

    public void loadUserData(){
        userData.setAll(currentUser.myQuery.users().getAllUsers());
    }
}
