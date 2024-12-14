package org.example.mesexadmin.ui.admin_level;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import org.example.mesexadmin.Main;
import org.example.mesexadmin.SceneManager;
import org.example.mesexadmin.SessionUser;
import org.example.mesexadmin.data_class.ActivityData;
import org.example.mesexadmin.data_class.UserData;
import org.example.mesexadmin.ui.ControllerWrapper;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class AppManagerController implements ControllerWrapper {
    static SceneManager sceneManager;
    static SessionUser currentUser;

    @FXML private TableView<ActivityData> userTable;

    // Login logs
    @FXML private TableView<ActivityData> loginTable;
    @FXML private TableColumn<ActivityData, Date> loginDateCol;
    @FXML private TableColumn<ActivityData, String> loginUsernameCol;
    @FXML private TableColumn<ActivityData, String> loginDisplayNameCol;
    static ObservableList<ActivityData> loginData = FXCollections.observableArrayList();

    // Newly created accounts
    @FXML private TableView<UserData> newAccountTable;
    @FXML private TableColumn<UserData, String> newAccountUsernameCol;
    @FXML private TableColumn<UserData, Date> newAccountDateCreatedCol;
    static ObservableList<UserData> newAccountData = FXCollections.observableArrayList();


    @FXML private TableView<UserData> socialTable;
    @FXML private TableView<UserData> activeTable;

    // Yearly register
    @FXML private BarChart<String, Number> yearlyRegister;
    @FXML private CategoryAxis xRegisterAxis;
    @FXML private NumberAxis yRegisterAxis;

    @FXML private BarChart<String, Number> yearlyActive;
    @FXML private CategoryAxis xActiveAxis;
    @FXML private NumberAxis yActiveAxis;


    void setUpRegisterBarChart(){
        xRegisterAxis.setLabel("Month");
        yRegisterAxis.setLabel("Numbers of new accounts");
        yearlyRegister.setTitle("New account this year");

        XYChart.Series<String, Number> year = new XYChart.Series<>();
        year.setName("No. new accounts");
        for (int i = 0; i < 6; i++){
            year.getData().add(new XYChart.Data<>("Month " + i , 10 + i * 2));
        }

        yearlyRegister.getData().add(year);
    }

    public void returnToMain(ActionEvent actionEvent) throws IOException {
        sceneManager.addScene("Main", "main-messaging.fxml");
        sceneManager.switchScene("Main");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currentUser = Main.getThisUser();
        sceneManager = Main.getSceneManager();

        loginDateCol.setCellValueFactory((a) -> new SimpleObjectProperty<>(a.getValue().getLoginDate()));
        loginUsernameCol.setCellValueFactory((a) -> new SimpleStringProperty(a.getValue().getUsername()));
        loginDisplayNameCol.setCellValueFactory((a) -> new SimpleStringProperty(a.getValue().getDisplayName()));

        newAccountUsernameCol.setCellValueFactory((a) -> new SimpleStringProperty(a.getValue().getUsername()));
        newAccountDateCreatedCol.setCellValueFactory((a) -> new SimpleObjectProperty<>(a.getValue().getDateCreated()));

        setUpRegisterBarChart();
    }

    @Override
    public void myInitialize() {
        ArrayList<ActivityData> loggedHistory = currentUser.myQuery.activities().viewLoginHistoryAll();
        loginData.clear();
        loginData.addAll(loggedHistory);
        loginTable.setItems(loginData);
        loginTable.refresh();

        ArrayList<UserData> newAccount = currentUser.myQuery.users().getNewUsers();
        newAccountData.clear();
        newAccountData.addAll(newAccount);
        newAccountTable.setItems(newAccountData);
        newAccountTable.refresh();

//        userTable.setItems(loginData);
    }
}
