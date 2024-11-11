package org.example.mesex;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class MessagingController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private ListView<HBox> myListView;
    @FXML
    private ListView<Label> messages;
    @FXML
    private Label myLabel;
    @FXML
    private TextArea myTextArea;

    List<String> friendList = List.of(new String[]{"f1", "f2", "f3"});
    String currentFriend;

    void bufferScene(ActionEvent actionEvent){
        stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void addFriend(ActionEvent actionEvent){
        String s = "dummy";
        HBox p = new HBox();
        Button b = new Button();
        b.setText("...");
        p.setId(s);
        Label thisLabel = new Label();
        thisLabel.setText(s);
        p.getChildren().add(thisLabel);
        p.getChildren().add(b);
        p.setSpacing(20);
        myListView.getItems().add(p);
    }

    public void updateMessageBuffer(ActionEvent actionEvent){

    }

    public void addMessage(ActionEvent actionEvent){
        if (!myTextArea.getText().trim().isEmpty()){
            Label newLabel = new Label();
            newLabel.setText("admin: " + myTextArea.getText().trim());
            messages.getItems().add(newLabel);
            myTextArea.setText("");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        for (String s : friendList){
            HBox p = new HBox();
            Button b = new Button();
            b.setText("...");
            p.setId(s);
            Label thisLabel = new Label();
            thisLabel.setText(s);
            p.getChildren().add(thisLabel);
            p.getChildren().add(b);
            p.setSpacing(20);
            myListView.getItems().add(p);
        }

        myListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<HBox>() {
            @Override
            public void changed(ObservableValue<? extends HBox> observableValue, HBox hBox, HBox t1) {
                currentFriend = myListView.getSelectionModel().getSelectedItem().getId();
                myLabel.setText(currentFriend);
            }
        });

    }
}
