package org.example.mesexadmin.ui.admin_level;

import javafx.animation.PauseTransition;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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

    public void banUser(ActionEvent actionEvent) {
        Alert newAlert = new Alert(Alert.AlertType.CONFIRMATION);
        newAlert.setContentText("Ban this user?");
        newAlert.setHeaderText("Ban User");
        newAlert.showAndWait();
    }

    public void unbanUser(ActionEvent actionEvent) {
        Alert newAlert = new Alert(Alert.AlertType.CONFIRMATION);
        newAlert.setContentText("Remove ban this user?");
        newAlert.setHeaderText("Unban User");
        newAlert.showAndWait();
    }

    public void resetPassword(ActionEvent actionEvent) {
        Alert newAlert = new Alert(Alert.AlertType.CONFIRMATION);
        newAlert.setContentText("Send reset request to user's email?");
        newAlert.setHeaderText("Reset User's Password");
        newAlert.showAndWait();
    }

    public void unSpam(ActionEvent actionEvent) {
        Alert newAlert = new Alert(Alert.AlertType.CONFIRMATION);
        newAlert.setContentText("Unmark this user as spam?");
        newAlert.setHeaderText("Unmark as spam");
        newAlert.showAndWait();
    }

    public void editProfile(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("pop-up-modify-user.fxml"));
        Dialog<Objects> dialog = new Dialog<>();
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        DialogPane dialogPane = loader.load();
        PopUpController popUpController = loader.getController();
        popUpController.currentDialog = dialog;
        dialog.setDialogPane(dialogPane);
        dialog.showAndWait();
        dialog.close();
    }

    public void createNewProfile(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("pop-up-add-user.fxml"));
        Dialog<Objects> dialog = new Dialog<>();
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        DialogPane dialogPane = loader.load();
        PopUpController popUpController = loader.getController();
        popUpController.currentDialog = dialog;
        dialog.setDialogPane(dialogPane);
        dialog.showAndWait();
        dialog.close();
    }

    public void removeUser(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("pop-up-delete-member.fxml"));
        Dialog<Objects> dialog = new Dialog<>();
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        DialogPane dialogPane = loader.load();
        PopUpController popUpController = loader.getController();
        popUpController.currentDialog = dialog;
        dialog.setDialogPane(dialogPane);
        dialog.showAndWait();
        dialog.close();
    }


    public void changePassword(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("pop-up-reset-password.fxml"));
        Dialog<Objects> dialog = new Dialog<>();
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        DialogPane dialogPane = loader.load();
        PopUpController popUpController = loader.getController();
        popUpController.currentDialog = dialog;
        dialog.setDialogPane(dialogPane);
        dialog.showAndWait();
    }

}
