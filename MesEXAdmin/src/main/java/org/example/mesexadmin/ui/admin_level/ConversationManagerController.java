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
import javafx.scene.control.*;
import javafx.util.Duration;
import org.bson.types.ObjectId;
import org.example.mesexadmin.App;
import org.example.mesexadmin.SceneManager;
import org.example.mesexadmin.SessionUser;
import org.example.mesexadmin.data_class.ConversationData;
import org.example.mesexadmin.data_class.UserData;
import org.example.mesexadmin.ui.ControllerWrapper;
import org.example.mesexadmin.ui.elements.UserListComponent;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class ConversationManagerController implements ControllerWrapper {
    static SceneManager sceneManager;
    static SessionUser currentUser;

    @FXML
    private TableView<ConversationData> groupsTable;
    @FXML
    private ListView<UserListComponent> membersList, moderatorsList;
    @FXML
    private TextField groupNameField;
    @FXML
    private TableColumn<ConversationData, String> groupNameCol;
    @FXML
    private TableColumn<ConversationData, Date> groupCreatedCol;

    static ConversationData selectedGroup;
    PauseTransition searchPause;
    final ObservableList<ConversationData> groupData = FXCollections.observableArrayList();
    final ObservableList<UserListComponent> membersData = FXCollections.observableArrayList();
    final ObservableList<UserListComponent> moderatorsData = FXCollections.observableArrayList();

    public void returnToMain(ActionEvent actionEvent) throws IOException {
        sceneManager.addScene("Main", "main-messaging.fxml");
        sceneManager.switchScene("Main");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sceneManager = App.getSceneManager();
        searchPause = new PauseTransition(Duration.millis(500));
        searchPause.setOnFinished((e) -> loadFilter());


        groupsTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ConversationData>() {
            @Override
            public void changed(ObservableValue<? extends ConversationData> observableValue, ConversationData conversationData, ConversationData t1) {
                ConversationData c = groupsTable.getSelectionModel().getSelectedItem();
                if (c != null){
                    selectedGroup = c;
                    loadSelectedData();
                }
            }
        });

        groupNameCol.setCellValueFactory((a) -> new SimpleStringProperty(a.getValue().getConversationName()));
        groupCreatedCol.setCellValueFactory((a) -> new SimpleObjectProperty<Date>(a.getValue().getDateCreated()));

        groupNameField.textProperty().addListener((a) -> {
            searchPause.stop();
            searchPause.playFromStart();
        });

    }

    public void loadFilter(){
        String text = groupNameField.getText().trim();

        if (text.isEmpty()){
            groupData.setAll(currentUser.myQuery.conversations().getAllGroupConversation());
        }
        else {
            groupData.setAll(currentUser.myQuery.conversations().getAllGroupConversationWithFilter(text));
        }
    }

    public void loadSelectedData() {
        ArrayList<ObjectId> membersId = selectedGroup.getMembersId();
        ArrayList<ObjectId> moderatorsId = selectedGroup.getModeratorsId();
        ArrayList<UserData> members = currentUser.myQuery.users().getUserList(membersId);
        ArrayList<UserData> moderators = currentUser.myQuery.users().getUserList(moderatorsId);

        membersData.clear();
        members.forEach((a) -> membersData.add(new UserListComponent(a)));
        moderatorsData.clear();
        moderators.forEach((a) -> moderatorsData.add(new UserListComponent(a)));
    }

    @Override
    public void myInitialize() {
        currentUser = App.getCurrentUser();
        groupsTable.setItems(groupData);
        membersList.setItems(membersData);
        moderatorsList.setItems(moderatorsData);
        groupData.setAll(currentUser.myQuery.conversations().getAllGroupConversation());
    }
}
