package org.example.mesexadmin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.mesexadmin.data_class.UserData;

import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import java.util.ResourceBundle;

public class PopUpController implements Initializable {
    SessionUser currentUser;

    @FXML private TextField singleTextField;
    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private TextField groupNameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField repeatPasswordField;
    @FXML private TextField displayNameField;
    @FXML private TextField address;
    @FXML private RadioButton isMale;
    @FXML private RadioButton isFemale;
    @FXML private DatePicker datePicker;
    ToggleGroup toggleGroup;


    public Dialog<Objects> currentDialog;

    public void closeDialog(ActionEvent actionEvent){
        currentDialog.close();
        currentDialog.setResult(null);
        Stage stage = (Stage) currentDialog.getDialogPane().getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currentUser = Main.getCurrentUser();
        if (isMale != null && isFemale != null){
            isMale.setToggleGroup(toggleGroup);
            isFemale.setToggleGroup(toggleGroup);
        }
    }
    
    public String getUsernameField() {
        return usernameField.getText();
    }

    public String getEmailField() {
        return emailField.getText();
    }

    public String getGroupNameField() {
        return groupNameField.getText();
    }

    public void setUsername(String text){
        usernameField.setText(text);
        usernameField.setDisable(true);
    }

    public void setGroupName(String text){
        groupNameField.setText(text);
    }

    public void loadUserInfo(UserData data){
        usernameField.setText(data.getUsername());
        usernameField.setDisable(true);
        displayNameField.setText(data.getDisplayName());
        if (data.getDateOfBirth() != null)
            datePicker.setValue(Instant.ofEpochMilli(data.getDateOfBirth().getTime()).atZone(ZoneId.systemDefault()).toLocalDate());
        if (!data.getGender().isEmpty()){
            if (data.getGender().equals("male")){
                isMale.arm();
            }
            else if (data.getGender().equals("female")){
                isFemale.arm();
            }
        }

    }

    public UserData applyUserEdit(UserData data){
        data.setDisplayName(displayNameField.getText());
        data.setAddress(address.getText());
        if (datePicker.getValue() != null)
            data.setDateOfBirth(Date.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        if (isMale.isArmed())
            data.setGender("male");
        else if (isFemale.isArmed())
            data.setGender("female");
        return data;
    }

    public UserData createUserFromInfo(){
        UserData newUser = new UserData();
        newUser.setUsername(usernameField.getText());
        newUser.setEmail(emailField.getText());
        String hashedPassword = SessionUser.hashPassword(passwordField.getText());
        newUser.setPasswordHashed(hashedPassword);
        newUser.setDateCreated(new Date());
        return newUser;
    }

    public String getHashedPassword(){
        return SessionUser.hashPassword(passwordField.getText());
    }

    public boolean validatePasswordInput(){
        return (!passwordField.toString().isEmpty()
                && !repeatPasswordField.toString().isEmpty()
                && passwordField.toString().equals(repeatPasswordField.toString()));
    }

    public void clearAllFields() {
        if (usernameField != null) {
            usernameField.clear();
            usernameField.setDisable(false);
        }
        if (emailField != null) {
            emailField.clear();
        }

        if (groupNameField != null){
            groupNameField.clear();
        }

        if (passwordField != null){
            passwordField.clear();
        }

        if (repeatPasswordField != null){
            repeatPasswordField.clear();
        }

        if (displayNameField != null){
            displayNameField.clear();
        }

        if (datePicker != null){
            datePicker.cancelEdit();
        }

        if (isMale != null){
            isMale.disarm();
        }

        if (isFemale != null){
            isFemale.disarm();
        }

        if (address != null){
            address.clear();
        }
    }
}
