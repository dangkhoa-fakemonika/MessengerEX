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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.mesexadmin.data_class.ActivityData;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class AppManagerController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;


    @FXML
    private TableView<ActivityData> userTable;

    void bufferScene(ActionEvent actionEvent){
//        System.out.println(actionEvent.getSource());
        stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    final ObservableList<ActivityData> data = FXCollections.observableArrayList(
            new ActivityData("user2", "12:00:00 13-11-2024", "login"),
            new ActivityData("user2", "12:00:00 12-11-2024", "reset_password"),
            new ActivityData("user1", "12:00:00 11-11-2024", "login"),
            new ActivityData("user2", "12:00:00 10-11-2024", "register"),
            new ActivityData("user1", "12:00:00 9-11-2024", "register")
    );


    ObservableList<TableColumn<ActivityData, String>> generateColumns(){
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

    public void returnToMain(ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main-messaging.fxml")));
        bufferScene(actionEvent);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userTable.setItems(data);
        userTable.getColumns().addAll(generateColumns());
    }
}
