package org.example.mesexadmin.ui.admin_level;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.mesexadmin.Main;
import org.example.mesexadmin.SceneManager;
import org.example.mesexadmin.data_class.ActivityData;
import org.example.mesexadmin.data_class.UserData;
import org.example.mesexadmin.ui.ControllerWrapper;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AppManagerController implements ControllerWrapper {
    static SceneManager sceneManager;

    @FXML private TableView<ActivityData> userTable;
    @FXML private TableView<UserData> loginTable;
    @FXML private TableView<UserData> newAccountTable;
    @FXML private TableView<UserData> socialTable;
    @FXML private TableView<UserData> activeTable;
    @FXML private BarChart<String, Number> yearlyRegister;
    @FXML private CategoryAxis xRegisterAxis;
    @FXML private NumberAxis yRegisterAxis;

    @FXML private BarChart<String, Number> yearlyActive;
    @FXML private CategoryAxis xActiveAxis;
    @FXML private NumberAxis yActiveAxis;

    static ObservableList<ActivityData> activityData;


    ObservableList<TableColumn<ActivityData, String>> generateActivityColumns(){
        TableColumn<ActivityData, String> idCol = new TableColumn<>("ID");
        idCol.setMinWidth(250);
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<ActivityData, String> timeCol = new TableColumn<>("Time");
        timeCol.setMinWidth(250);
        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
        TableColumn<ActivityData, String> actionCol = new TableColumn<>("Action Performed");
        actionCol.setMinWidth(300);
        actionCol.setCellValueFactory(new PropertyValueFactory<>("action"));

        return FXCollections.observableArrayList(idCol, timeCol, actionCol);
    }

    ObservableList<TableColumn<UserData, String>> generateLoginColumns(){
        TableColumn<UserData, String> idCol = new TableColumn<>("Username");
        idCol.setMinWidth(250);
//        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<UserData, String> timeCol = new TableColumn<>("Full name");
        timeCol.setMinWidth(250);
//        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
        TableColumn<UserData, String> actionCol = new TableColumn<>("Time");
        actionCol.setMinWidth(300);
//        actionCol.setCellValueFactory(new PropertyValueFactory<>("action"));

        return FXCollections.observableArrayList(idCol, timeCol, actionCol);
    }

    ObservableList<TableColumn<UserData, String>> generateNewUsersColumns(){
        TableColumn<UserData, String> idCol = new TableColumn<>("Username");
        idCol.setMinWidth(250);
//        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<UserData, String> timeCol = new TableColumn<>("Full name");
        timeCol.setMinWidth(250);
//        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
        TableColumn<UserData, String> actionCol = new TableColumn<>("Time");
        actionCol.setMinWidth(300);
//        actionCol.setCellValueFactory(new PropertyValueFactory<>("action"));

        return FXCollections.observableArrayList(idCol, timeCol, actionCol);
    }

    ObservableList<TableColumn<UserData, String>> generateFriendColumns(){
        TableColumn<UserData, String> idCol = new TableColumn<>("Username");
        idCol.setMinWidth(160);
//        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<UserData, String> timeCol = new TableColumn<>("Full name");
        timeCol.setMinWidth(160);
//        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
        TableColumn<UserData, String> actionCol = new TableColumn<>("Time");
        actionCol.setMinWidth(160);
//        actionCol.setCellValueFactory(new PropertyValueFactory<>("action"));
        TableColumn<UserData, String> directFriendCol = new TableColumn<>("Direct Friends");
        directFriendCol.setMinWidth(160);
        TableColumn<UserData, String> indirectFriendCol = new TableColumn<>("Indirect Friends");
        indirectFriendCol.setMinWidth(160);

        return FXCollections.observableArrayList(idCol, timeCol, actionCol, directFriendCol, indirectFriendCol);
    }

    ObservableList<TableColumn<UserData, String>> generatePersonalActiveColumns(){
        TableColumn<UserData, String> idCol = new TableColumn<>("Username");
        idCol.setMinWidth(160);
        TableColumn<UserData, String> actionCol = new TableColumn<>("Time");
        actionCol.setMinWidth(160);
        TableColumn<UserData, String> timeCol = new TableColumn<>("App Open Frequency");
        timeCol.setMinWidth(160);
        TableColumn<UserData, String> directFriendCol = new TableColumn<>("Groups Chat");
        directFriendCol.setMinWidth(160);
        TableColumn<UserData, String> indirectFriendCol = new TableColumn<>("Private Chat");
        indirectFriendCol.setMinWidth(160);

        return FXCollections.observableArrayList(idCol, timeCol, actionCol, directFriendCol, indirectFriendCol);
    }

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
        sceneManager = Main.getSceneManager();
        setUpRegisterBarChart();

        userTable.getColumns().addAll(generateActivityColumns());
        loginTable.getColumns().addAll(generateLoginColumns());
        newAccountTable.getColumns().addAll(generateNewUsersColumns());
        socialTable.getColumns().addAll(generateFriendColumns());
        activeTable.getColumns().addAll(generatePersonalActiveColumns());
    }

    @Override
    public void myInitialize() {
//        userTable.setItems(FXCollections.observableArrayList());
//        loginTable.setItems(FXCollections.observableArrayList());
//        newAccountTable.setItems(FXCollections.observableArrayList());
//        socialTable.setItems(FXCollections.observableArrayList());
//        activeTable.setItems(FXCollections.observableArrayList());
//        userTable.getColumns().clear();
//        loginTable.getColumns().clear();
//        newAccountTable.getColumns().clear();
//        socialTable.getColumns().clear();
//        activeTable.getColumns().clear();


        userTable.setItems(activityData);
    }
}
