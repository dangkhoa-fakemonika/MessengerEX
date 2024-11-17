package org.example.mesexadmin;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class MessagingController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private ListView<String> myListView;
    @FXML
    private ListView<Label> messages;
    @FXML
    private Label myLabel;
    @FXML
    private TextArea myTextArea;


    ObservableList<String> friendList = FXCollections.observableArrayList("user1", "user2", "user3");
    String currentFriend;

    void bufferScene(ActionEvent actionEvent){
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    HBox makeFriendBox(String s){
        HBox p = new HBox();

        p.setId(s);
        Label thisLabel = new Label();
        thisLabel.setText(s);
        p.getChildren().add(thisLabel);

        p.setSpacing(20);

        return p;
    }

    public void addFriend(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("pop-up-add.fxml"));
        Dialog<Objects> dialog = new Dialog<>();
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        DialogPane dialogPane = loader.load();
        PopUpController popUpController = loader.getController();
        popUpController.currentDialog = dialog;
        dialog.setDialogPane(dialogPane);
        dialog.showAndWait();
        dialog.close();
    }

    public void updateMessageBuffer(ActionEvent actionEvent){
//        if (actionEvent.getEventType())
    }

    public void addMessage(ActionEvent actionEvent){
        if (!myTextArea.getText().trim().isEmpty()){
            Label newLabel = new Label();
            newLabel.setText("admin: " + myTextArea.getText().trim());
            messages.getItems().add(newLabel);
            myTextArea.setText("");
        }
    }

    public void addMessage(){
        if (!myTextArea.getText().trim().isEmpty()){
            Label newLabel = new Label();
            newLabel.setText("admin: " + myTextArea.getText().trim());
            messages.getItems().add(newLabel);
            myTextArea.setText("");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        myListView.getItems().addAll(friendList);

        myListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                currentFriend = myListView.getSelectionModel().getSelectedItem();
                myLabel.setText(currentFriend);
            }
        });

        myTextArea.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER)  {
                    if (keyEvent.isShiftDown()){
                        myTextArea.appendText("\n");
                    }
                    else {
                        addMessage();
                    }
                }
            }
        });

    }

    public void friendsSettingScene(ActionEvent actionEvent) throws IOException{
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main-friend-config.fxml")));
        Stage thisStage = (Stage) ((MenuItem) actionEvent.getSource()).getParentPopup().getOwnerWindow();
        scene = new Scene(root);
        thisStage.setScene(scene);
        thisStage.show();
    }

    public void userManagementScene(ActionEvent actionEvent) throws IOException{
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main-user-manager.fxml")));
        Stage thisStage = (Stage) ((MenuItem) actionEvent.getSource()).getParentPopup().getOwnerWindow();
        scene = new Scene(root);
        thisStage.setScene(scene);
        thisStage.show();
    }

    public void groupManagementScene(ActionEvent actionEvent) throws IOException{
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main-group-manager.fxml")));
        Stage thisStage = (Stage) ((MenuItem) actionEvent.getSource()).getParentPopup().getOwnerWindow();
        scene = new Scene(root);
        thisStage.setScene(scene);
        thisStage.show();
    }

    public void appManagementScene(ActionEvent actionEvent) throws IOException{
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main-app-manager.fxml")));
        Stage thisStage = (Stage) ((MenuItem) actionEvent.getSource()).getParentPopup().getOwnerWindow();
        scene = new Scene(root);
        thisStage.setScene(scene);
        thisStage.show();
    }

    public void personalGroupManagementScene(ActionEvent actionEvent) throws IOException{
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main-convo-config.fxml")));
        Stage thisStage = (Stage) ((MenuItem) actionEvent.getSource()).getParentPopup().getOwnerWindow();
        scene = new Scene(root);
        thisStage.setScene(scene);
        thisStage.show();
    }

    public void profileScene(ActionEvent actionEvent) throws IOException{
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("edit-user-profile.fxml")));
        Stage thisStage = (Stage) ((MenuItem) actionEvent.getSource()).getParentPopup().getOwnerWindow();
        scene = new Scene(root);
        thisStage.setScene(scene);
        thisStage.show();
    }

    public void returnLogin(ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main-login.fxml")));
        Stage thisStage = (Stage) ((MenuItem) actionEvent.getSource()).getParentPopup().getOwnerWindow();
        scene = new Scene(root);
        thisStage.setScene(scene);
        thisStage.show();
    }

}
