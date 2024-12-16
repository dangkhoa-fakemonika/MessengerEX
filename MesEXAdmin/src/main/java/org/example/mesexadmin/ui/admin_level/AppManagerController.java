package org.example.mesexadmin.ui.admin_level;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

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
    @FXML private ChoiceBox<String> loginFilter;
    static final String[] loginFilterKeys = {"None", "username", "displayName"};
    String currentLoginFilter = "None";
    @FXML private TextField loginFilterField;

    // Newly created accounts
    @FXML private TableView<UserData> newAccountTable;
    @FXML private TableColumn<UserData, String> newAccountUsernameCol;
    @FXML private TableColumn<UserData, String> newAccountEmailCol;
    @FXML private TableColumn<UserData, Date> newAccountDateCreatedCol;
    static ObservableList<UserData> newAccountData = FXCollections.observableArrayList();
    @FXML private ChoiceBox<String> newAccountFilter;
    @FXML private TextField newAccountFilterField;
    static final String[] newAccountFilterKeys = {"None", "username", "email"};
    String currentNewAccountFilter = "None";
    @FXML private DatePicker newAccountStartDate;
    @FXML private DatePicker newAccountEndDate;
    @FXML private CheckBox showAllNewAccounts;

    static final String[] monthNames = {"January", "February", "March", "April",
            "May", "June", "July", "August", "September", "October", "November", "December"};

    // Yearly register
    @FXML private BarChart<String, Number> yearlyRegister;
    @FXML private CategoryAxis xRegisterAxis;
    @FXML private NumberAxis yRegisterAxis;
    @FXML private ChoiceBox<String> registerSelectYear;
    String registerSelectedYear = "All";
    static ArrayList<String> registerYearOptions = new ArrayList<>();
    static XYChart.Series<String, Number> registerChartData = new XYChart.Series<>();

    // Yearly login
    @FXML private BarChart<String, Number> yearlyActive;
    @FXML private CategoryAxis xActiveAxis;
    @FXML private NumberAxis yActiveAxis;
    @FXML private ChoiceBox<String> activeSelectYear;
    String activeSelectedYear = "All";
    static ArrayList<String> activeYearOptions = new ArrayList<>();
    static XYChart.Series<String, Number> activeChartData = new XYChart.Series<>();

    // Mutual friends
    @FXML private TableView<UserData> socialTable;
    static ObservableList<UserData> socialUserData = FXCollections.observableArrayList();
    @FXML private TableColumn<UserData, String> socialUsernameCol;
    @FXML private TableColumn<UserData, String> socialDisplayNameCol;
    @FXML private TableColumn<UserData, Integer> socialDirectFriendCountCol;
    @FXML private TableColumn<UserData, Integer> socialIndirectFriendCountCol;
    @FXML private TextField socialCompareField;
    @FXML private TextField socialFilterField;
    @FXML private ChoiceBox<String> socialFilter;
    static final String[] socialFilterKeys = {"None", "username", "displayName"};
    String socialSelectedFilter = "None";
    @FXML private ChoiceBox<String> socialCompare;
    static final String[] socialCompareKeys = {"None", "Equal to", "Greater than", "Lesser than"};
    String socialSelectedCompare = "None";

    @FXML private TableView<UserData> activeTable;

    public void returnToMain(ActionEvent actionEvent) throws IOException {
        sceneManager.addScene("Main", "main-messaging.fxml");
        sceneManager.switchScene("Main");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currentUser = Main.getCurrentUser();
        sceneManager = Main.getSceneManager();

        loginDateCol.setCellValueFactory((a) -> new SimpleObjectProperty<>(a.getValue().getLoginDate()));
        loginUsernameCol.setCellValueFactory((a) -> new SimpleStringProperty(a.getValue().getUsername()));
        loginDisplayNameCol.setCellValueFactory((a) -> new SimpleStringProperty(a.getValue().getDisplayName()));
        loginFilter.getItems().addAll(loginFilterKeys);
        loginFilter.setOnAction(this::selectLoginDataFilter);
        loginFilterField.textProperty().addListener((observableValue, o, n) -> {
            selectLoginDataFilter(null);
        });

        newAccountUsernameCol.setCellValueFactory((a) -> new SimpleStringProperty(a.getValue().getUsername()));
        newAccountEmailCol.setCellValueFactory((a) -> new SimpleStringProperty(a.getValue().getEmail()));
        newAccountDateCreatedCol.setCellValueFactory((a) -> new SimpleObjectProperty<>(a.getValue().getDateCreated()));
        newAccountFilter.getItems().addAll(newAccountFilterKeys);
        newAccountFilter.setOnAction(this::selectNewAccountFilter);
        newAccountFilterField.textProperty().addListener((observableValue, o, n) -> {
            selectNewAccountFilter(null);
        });
        showAllNewAccounts.setOnAction((e) -> {
            newAccountStartDate.setDisable(showAllNewAccounts.isSelected());
            newAccountEndDate.setDisable(showAllNewAccounts.isSelected());
            selectNewAccountFilter(e);
        });
        newAccountStartDate.setOnAction(this::selectNewAccountFilter);
        newAccountEndDate.setOnAction(this::selectNewAccountFilter);

        xRegisterAxis.setLabel("Month");
        yRegisterAxis.setLabel("Numbers of new accounts");
        yearlyRegister.setTitle("New account this year");
        registerSelectYear.setOnAction(this::selectRegisterBarChart);
        yearlyRegister.setAnimated(false);

        xActiveAxis.setLabel("Month");
        yActiveAxis.setLabel("Numbers of new accounts");
        yearlyActive.setTitle("New account this year");
        activeSelectYear.setOnAction(this::selectActiveBarChart);
        yearlyActive.setAnimated(false);

        socialUsernameCol.setCellValueFactory((a) -> new SimpleStringProperty(a.getValue().getUsername()));
        socialDisplayNameCol.setCellValueFactory((a) -> new SimpleStringProperty(a.getValue().getDisplayName()));
        socialDirectFriendCountCol.setCellValueFactory((a) -> new SimpleObjectProperty<Integer>(a.getValue().getFriend().size()));
        socialIndirectFriendCountCol.setCellValueFactory((a) -> new SimpleObjectProperty<Integer>(
                currentUser.GetIndirectFriendsCount(a.getValue().getUserId())));
        socialFilter.getItems().addAll(socialFilterKeys);
        socialCompare.getItems().addAll(socialCompareKeys);
        socialFilter.setOnAction(this::selectSocialTable);
        socialCompare.setOnAction(this::selectSocialTable);
        socialFilterField.textProperty().addListener(((observableValue, s, t1) -> {
            selectSocialTable(null);
        }));
        socialCompareField.textProperty().addListener(((observableValue, s, t1) -> {
            selectSocialTable(null);
        }));

        socialCompareField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    socialCompareField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

    }

    void refreshLoginData(){
        loginFilterField.setDisable(true);
        loginFilter.setValue("None");
        ArrayList<ActivityData> loggedHistory = currentUser.myQuery.activities().viewLoginHistoryAll();
        loginData.clear();
        loginData.addAll(loggedHistory);
        loginTable.setItems(loginData);
        loginTable.refresh();
    }

    void selectLoginDataFilter(ActionEvent event){
        currentLoginFilter = loginFilter.getValue();
        ArrayList<ActivityData> loggedHistory;

        if (Objects.equals(currentLoginFilter, "None")){
            loginFilterField.setDisable(true);
            loggedHistory = currentUser.myQuery.activities().viewLoginHistoryAll();
        }
        else {
            loginFilterField.setDisable(false);
            String filterText = loginFilterField.getText().trim();

            if (!filterText.isEmpty()) {
                loggedHistory = currentUser.myQuery.activities().viewLoginHistoryWithFilters(currentLoginFilter, filterText);
            } else {
                loggedHistory = currentUser.myQuery.activities().viewLoginHistoryAll();
            }
        }

        loginData.clear();
        loginData.addAll(loggedHistory);
        loginTable.setItems(loginData);
        loginTable.refresh();
    }

    void refreshNewAccountData(){
        newAccountFilterField.setDisable(true);
        newAccountFilter.setValue("None");
        showAllNewAccounts.setSelected(true);
        newAccountStartDate.setDisable(true);
        newAccountEndDate.setDisable(true);
        newAccountStartDate.setValue(LocalDate.now().minusYears(2));
        newAccountEndDate.setValue(LocalDate.now());
        ArrayList<UserData> newAccount = currentUser.myQuery.users().getNewUsers();
        newAccountData.clear();
        newAccountData.addAll(newAccount);
        newAccountTable.setItems(newAccountData);
        newAccountTable.refresh();
    }

    void selectNewAccountFilter(ActionEvent event){
        currentNewAccountFilter = newAccountFilter.getValue();
        ArrayList<UserData> newAccount;

        Date startDate;

        if (newAccountStartDate.getValue() != null){
            startDate = Date.from(newAccountStartDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        } else {
            startDate = new Date();
        }


        Date endDate;
        if (newAccountEndDate.getValue() != null){
            endDate = Date.from(newAccountEndDate.getValue().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        } else {
            endDate = new Date();
        }

        if (Objects.equals(currentNewAccountFilter, "None")){
            newAccountFilterField.setDisable(true);
            if (showAllNewAccounts.isSelected())
                newAccount = currentUser.myQuery.users().getNewUsers();
            else
                newAccount = currentUser.myQuery.users().getNewUsers(startDate, endDate);
        }
        else {
            newAccountFilterField.setDisable(false);
            String filterText = newAccountFilterField.getText().trim();

            if (showAllNewAccounts.isSelected()){
                newAccount = currentUser.myQuery.users().getNewUsersWithFilters(currentNewAccountFilter, filterText);
            }

            else {
                if (!filterText.isEmpty()) {
                    newAccount = currentUser.myQuery.users().getNewUsersWithFilters(currentNewAccountFilter, filterText, startDate, endDate);
                } else {
                    newAccount = currentUser.myQuery.users().getNewUsers(startDate, endDate);
                }
            }
        }

        newAccountData.clear();
        newAccountData.addAll(newAccount);
        newAccountTable.setItems(newAccountData);
        newAccountTable.refresh();
    }

    void selectRegisterBarChart(ActionEvent actionEvent){
        registerSelectedYear = registerSelectYear.getValue();

        registerChartData = new XYChart.Series<>();

        if (Objects.equals(registerSelectedYear, "All")){
            registerChartData.setName("No. new accounts");
            for (int i = 1; i < registerYearOptions.size(); i++){
                int userRegisterCount = currentUser.myQuery.users().getNewUsersOnSectionCount(null, Integer.parseInt(registerYearOptions.get(i)));
                if (userRegisterCount > 0)
                    registerChartData.getData().add(new XYChart.Data<>(registerYearOptions.get(i), userRegisterCount));
            }
        }
        else {
            registerChartData.setName("No. new accounts");
            for (int i = 1; i <= 12; i++) {
                registerChartData.getData().add(new XYChart.Data<>(Integer.toString(i), currentUser.myQuery.users().getNewUsersOnSectionCount(i, Integer.parseInt(registerSelectedYear))));
            }
        }

        yearlyRegister.getData().clear();
        yearlyRegister.getData().add(registerChartData);
    }

    void refreshRegisterBarChart(){
        registerYearOptions.clear();
        registerYearOptions.add("All");
        registerYearOptions.addAll(currentUser.myQuery.users().getRegisterUserYearIndexes());
        registerSelectYear.getItems().clear();
        registerSelectYear.getItems().addAll(registerYearOptions);
        registerSelectYear.setValue("All");
        registerSelectedYear = "All";

        registerChartData = new XYChart.Series<>();

        registerChartData.setName("No. new accounts");
        for (int i = 1; i < registerYearOptions.size(); i++){
            int userRegisterCount = currentUser.myQuery.users().getNewUsersOnSectionCount(null, Integer.parseInt(registerYearOptions.get(i)));
            if (userRegisterCount > 0)
                registerChartData.getData().add(new XYChart.Data<>(registerYearOptions.get(i), userRegisterCount));
        }

        yearlyRegister.getData().clear();
        yearlyRegister.getData().add(registerChartData);
    }

    void selectActiveBarChart(ActionEvent actionEvent){
        activeSelectedYear = activeSelectYear.getValue();

        activeChartData = new XYChart.Series<>();

        if (Objects.equals(activeSelectedYear, "All")){
            activeChartData.setName("No. new accounts");
            for (int i = 1; i < activeYearOptions.size(); i++){
                int userRegisterCount = currentUser.myQuery.activities().getLoginOnSectionCount(null, Integer.parseInt(activeYearOptions.get(i)));
                if (userRegisterCount > 0)
                    activeChartData.getData().add(new XYChart.Data<>(activeYearOptions.get(i), userRegisterCount));
            }
        }
        else {
            activeChartData.setName("No. new accounts");
            for (int i = 1; i <= 12; i++) {
                activeChartData.getData().add(new XYChart.Data<>(Integer.toString(i), currentUser.myQuery.activities().getLoginOnSectionCount(i, Integer.parseInt(activeSelectedYear))));
            }
        }

        yearlyActive.getData().clear();
        yearlyActive.getData().add(activeChartData);
    }

    void refreshActiveBarChart(){
        activeYearOptions.clear();
        activeYearOptions.add("All");
        activeYearOptions.addAll(currentUser.myQuery.activities().getLoginUserYearIndexes());
        activeSelectYear.getItems().clear();
        activeSelectYear.getItems().addAll(activeYearOptions);
        activeSelectYear.setValue("All");
        activeSelectedYear = "All";

        activeChartData = new XYChart.Series<>();

        activeChartData.setName("No. new accounts");
        for (int i = 1; i < activeYearOptions.size(); i++){
            int userRegisterCount = currentUser.myQuery.activities().getLoginOnSectionCount(null, Integer.parseInt(activeYearOptions.get(i)));
            if (userRegisterCount > 0)
                activeChartData.getData().add(new XYChart.Data<>(activeYearOptions.get(i), userRegisterCount));
        }

        yearlyActive.getData().clear();
        yearlyActive.getData().add(activeChartData);
    }

    void selectSocialTable(ActionEvent actionEvent){
        socialSelectedFilter = socialFilter.getValue();
        socialSelectedCompare = socialCompare.getValue();

        socialFilterField.setDisable(Objects.equals(socialSelectedFilter, "None"));
        socialCompareField.setDisable(Objects.equals(socialSelectedCompare, "None"));

        String filterField = socialFilterField.getText().trim();
        int compareField = 0;
        if (!socialCompareField.getText().trim().isEmpty())
            compareField = Integer.parseInt(socialCompareField.getText().trim());

        if (Objects.equals(socialSelectedFilter, "None") || filterField.isEmpty()){
            filterField = null;
        }

        ArrayList<UserData> socialUsers = currentUser.loadUserFriendsStatusFilter(socialSelectedCompare, compareField, socialSelectedFilter, filterField);

        socialUserData.clear();
        socialUserData.addAll(socialUsers);
        socialTable.setItems(socialUserData);
        socialTable.refresh();
    }

    void refreshSocialTable(){
        socialSelectedFilter = "None";
        socialSelectedCompare = "None";
        socialFilter.setValue("None");
        socialCompare.setValue("None");
        socialFilterField.setDisable(true);
        socialCompareField.setDisable(true);
        ArrayList<UserData> socialUsers = currentUser.loadUserFriendsStatusFilter("None", 0, null, null);
        socialUserData.clear();
        socialUserData.addAll(socialUsers);
        socialTable.setItems(socialUserData);
        socialTable.refresh();
    }

    @Override
    public void myInitialize() {
        refreshLoginData();
        refreshNewAccountData();
        refreshRegisterBarChart();
        refreshActiveBarChart();
        refreshSocialTable();
    }
}
