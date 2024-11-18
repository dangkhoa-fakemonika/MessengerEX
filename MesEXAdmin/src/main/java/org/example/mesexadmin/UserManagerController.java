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
import org.example.mesexadmin.data_class.SpamTicketData;
import org.example.mesexadmin.data_class.UserData;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class UserManagerController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;


    @FXML
    private TableView<UserData> userTable;
    @FXML
    private TableView<SpamTicketData> spamTable;
    @FXML
    private TableView<UserData> bannedTable;

    void bufferScene(ActionEvent actionEvent){
//        System.out.println(actionEvent.getSource());
        stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

//    ObservableList<UserData> spamData = FXCollections.observableArrayList(new ArrayList<>());
//    ObservableList<UserData> bannedData = FXCollections.observableArrayList(new ArrayList<>());


    final ObservableList<UserData> data = FXCollections.observableArrayList(
            new UserData("FakeMonika", "fakemonika", "example@email.com", "Active"),
            new UserData("KanCh", "kanch", "example@email.com", "Active"),
            new UserData("Ryan Gosling", "him", "example@email.com", "Offline")
    );

    final ObservableList<SpamTicketData> spamData = FXCollections.observableArrayList(
            new SpamTicketData("user2", "fakemonika4", "now"),
            new SpamTicketData("user2", "kanch4", "now")
    );

    final ObservableList<UserData> bannedData = FXCollections.observableArrayList(
            new UserData("FakeMonika4", "fakemonika4", "example@email.com", "Banned"),
            new UserData("KanCh4", "kanch4", "example@email.com", "Banned")
    );

    public ObservableList<TableColumn<UserData, String>> generateUserColumns(){
        TableColumn<UserData, String> usernameCol;
        TableColumn<UserData, String> nameCol;
        TableColumn<UserData, String> emailCol;

        TableColumn<UserData, String> statusCol;
        TableColumn<UserData, String> cDateCol;

        TableColumn<UserData, String> addressCol;
        TableColumn<UserData, String> genderCol;
        TableColumn<UserData, String> birthCol;


        nameCol = new TableColumn<>("Name");
        usernameCol = new TableColumn<>("Username");
        emailCol = new TableColumn<>("Email");

        statusCol = new TableColumn<>("Status");
        cDateCol = new TableColumn<>("Date Created");

        addressCol = new TableColumn<>("Address");
        birthCol = new TableColumn<>("Date of Birth");
        genderCol = new TableColumn<>("Gender");

        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("displayName"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        cDateCol.setCellValueFactory(new PropertyValueFactory<>("dateCreated"));

        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        birthCol.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        genderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));

        usernameCol.setMinWidth (100);
        nameCol.setMinWidth (100);
        emailCol.setMinWidth (100);

        statusCol.setMinWidth (100);
        cDateCol.setMinWidth (100);

        addressCol.setMinWidth (100);
        birthCol.setMinWidth (100);
        genderCol.setMinWidth (100);

        return FXCollections.observableArrayList(usernameCol, nameCol, emailCol, statusCol, cDateCol, addressCol, birthCol, genderCol);
    }

    public ObservableList<TableColumn<SpamTicketData, String>> generateSpamColumns(){
        TableColumn<SpamTicketData, String> reporterCol = new TableColumn<>("Reporter");
        reporterCol.setCellValueFactory(new PropertyValueFactory<>("reporterId"));
        reporterCol.setMinWidth(100);

        TableColumn<SpamTicketData, String> reportedCol = new TableColumn<>("Reported Account");
        reportedCol.setCellValueFactory(new PropertyValueFactory<>("reportedId"));
        reportedCol.setMinWidth(100);

        TableColumn<SpamTicketData, String> timeCol = new TableColumn<>("Reporter");
        timeCol.setCellValueFactory(new PropertyValueFactory<>("reportedId"));
        timeCol.setMinWidth(100);

        return FXCollections.observableArrayList(reportedCol, reporterCol, timeCol);
    }

    public void returnToMain(ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main-messaging.fxml")));
        bufferScene(actionEvent);
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
        newAlert.setHeaderText("Reset User");
        newAlert.showAndWait();
    }

    public void unSpam(ActionEvent actionEvent) {
        Alert newAlert = new Alert(Alert.AlertType.CONFIRMATION);
        newAlert.setContentText("Unmark this user as spam?");
        newAlert.setHeaderText("Unmark as spam");
        newAlert.showAndWait();
    }

    public void editProfile(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("pop-up-modify-user.fxml"));
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("pop-up-add-user.fxml"));
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("pop-up-delete-member.fxml"));
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("pop-up-reset-password.fxml"));
        Dialog<Objects> dialog = new Dialog<>();
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        DialogPane dialogPane = loader.load();
        PopUpController popUpController = loader.getController();
        popUpController.currentDialog = dialog;
        dialog.setDialogPane(dialogPane);
        dialog.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        userTable.setItems(data);
        userTable.getColumns().addAll(generateUserColumns());
        spamTable.setItems(spamData);
        spamTable.getColumns().addAll(generateSpamColumns());
        bannedTable.setItems(bannedData);
        bannedTable.getColumns().addAll(generateUserColumns());
    }
}
