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
import javafx.scene.control.*;
import javafx.util.Duration;
import org.example.mesexadmin.App;
import org.example.mesexadmin.SceneManager;
import org.example.mesexadmin.SessionUser;
import org.example.mesexadmin.data_class.SpamTicketData;
import org.example.mesexadmin.ui.ControllerWrapper;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class SpamManagerController implements ControllerWrapper {
    static SceneManager sceneManager;
    static SessionUser currentUser;

    @FXML private TableView<SpamTicketData> spamTable;
    final ObservableList<SpamTicketData> spamData = FXCollections.observableArrayList();
    @FXML private TableColumn<SpamTicketData, String> usernameCol;
    @FXML private TableColumn<SpamTicketData, String> emailCol;
    @FXML private TableColumn<SpamTicketData, Date> spamDateCol;
    @FXML private TableColumn<SpamTicketData, String> reporterNameCol;
    @FXML private TextField filterField;
    PauseTransition filterPause;
    @FXML private ChoiceBox<String> spamFilter;
    final ObservableList<String> filterOptions = FXCollections.observableArrayList("None", "username", "email");
    @FXML private Button removeSpam, banUser;

    static SpamTicketData selectedTicket;
    static String currentFilter = "None";

    @Override
    public void myInitialize() {
        currentUser = App.getCurrentUser();
        removeSpam.setDisable(true);
        banUser.setDisable(true);
        currentFilter = "None";
        spamFilter.setValue("None");

        loadData();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sceneManager = App.getSceneManager();
        spamTable.setItems(spamData);
        spamFilter.setItems(filterOptions);
        filterPause = new PauseTransition(Duration.millis(500));
        filterPause.setOnFinished((e) -> updateData());

        spamTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<SpamTicketData>() {
            @Override
            public void changed(ObservableValue<? extends SpamTicketData> observableValue, SpamTicketData spamTicketData, SpamTicketData t1) {
                SpamTicketData s = spamTable.getSelectionModel().getSelectedItem();
                if (s != null){
                    selectedTicket = s;
                    removeSpam.setDisable(false);
                    banUser.setDisable(false);
                }
            }
        });

        usernameCol.setCellValueFactory((a) -> new SimpleStringProperty(a.getValue().getReportedName()));
        emailCol.setCellValueFactory((a) -> new SimpleStringProperty(a.getValue().getReportedEmail()));
        spamDateCol.setCellValueFactory((a) -> new SimpleObjectProperty<Date>(a.getValue().getTimeSent()));
        reporterNameCol.setCellValueFactory((a) -> new SimpleStringProperty(a.getValue().getReporterName()));

        filterField.textProperty().addListener((e) -> {
            filterPause.stop();
            filterPause.playFromStart();
        });

        spamFilter.setOnAction((e) -> updateData());

        banUser.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText("Ban " + selectedTicket.getReportedName() + " ?");
                alert.setTitle("Ban User");

                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        if (currentUser.myQuery.users().changeUserStatus(selectedTicket.getReportedId(), "banned")) {
                            currentUser.myQuery.spams().removeSpamTicket(selectedTicket.getTicketId());
                            new Alert(Alert.AlertType.INFORMATION, selectedTicket.getReportedName() + " is banned").showAndWait();
                            updateData();
                        }
                        else {
                            new Alert(Alert.AlertType.ERROR, "Can't ban user.").showAndWait();
                        }
                    }
                });
            }
        });

        removeSpam.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText("Remove this spam ticket? ?");
                alert.setTitle("Remove ticket");

                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        if (currentUser.myQuery.spams().removeSpamTicket(selectedTicket.getTicketId())) {
                            updateData();
                        }
                        else {
                            new Alert(Alert.AlertType.ERROR, "Can't remove ticket.").showAndWait();
                        }
                    }
                });
            }
        });
    }

    public void updateData(){
        removeSpam.setDisable(true);
        banUser.setDisable(true);
        filterField.setDisable(spamFilter.getValue().equals("None"));

        String token = filterField.getText().trim();
        if (spamFilter.getValue() != null)
            currentFilter = spamFilter.getValue();

        if (!token.isEmpty() && !spamFilter.getValue().equals("None")){
            spamData.setAll(currentUser.myQuery.spams().getSpamTicketDetailsFilter(currentFilter, token));
        }
        else {
            spamData.setAll(currentUser.myQuery.spams().getSpamTicketDetails());
        }

    }

    public void loadData(){
        spamData.setAll(currentUser.myQuery.spams().getSpamTicketDetails());
    }

    public void returnToMain(ActionEvent actionEvent) throws IOException {
        sceneManager.addScene("Main", "main-messaging.fxml");
        sceneManager.switchScene("Main");
    }
}
