package org.example.mesexadmin.ui.user_level;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.mesexadmin.Main;
import org.example.mesexadmin.PopUpController;
import org.example.mesexadmin.SceneManager;
import org.example.mesexadmin.SessionUser;
import org.example.mesexadmin.data_class.UserData;
import org.example.mesexadmin.ui.ControllerWrapper;

//import javax.mail.Session;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import java.util.Properties;
import java.util.ResourceBundle;

public class EditProfileController implements ControllerWrapper {
    SceneManager sceneManager;
    SessionUser currentUser;

    @FXML private Button editProfileButton;
    @FXML private TextField addressField;
    @FXML private RadioButton maleRadioButton;
    @FXML private RadioButton femaleRadioButton;
    @FXML private ToggleGroup genderToggleGroup;
    @FXML private DatePicker dateOfBirthPicker;
    @FXML private DatePicker dateJoinedPicker;
    @FXML private TextField emailField;
    @FXML private Button returnToMainButton;
    @FXML private Button confirmButton;
    @FXML private Button changePasswordButton;
    @FXML private Button resetPasswordButton;
    @FXML private Text usernameText;
    @FXML private Label nameLabel;
    @FXML private TextField nameField;
    @FXML private Button cancelButton;
    @FXML private Text passwordText;

    public void returnToMain(ActionEvent actionEvent) throws IOException {
        sceneManager.addScene("Main", "main-messaging.fxml");
        sceneManager.switchScene("Main");
    }

    public void changePassword(ActionEvent actionEvent) throws IOException {
        sceneManager.addScene("ChangePassword", "change-password.fxml");
        sceneManager.switchScene("ChangePassword");
    }

    public void changeUsername(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("pop-up-change-name.fxml"));
        Dialog<Objects> dialog = new Dialog<>();
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        DialogPane dialogPane = loader.load();
        PopUpController popUpController = loader.getController();
        popUpController.currentDialog = dialog;
        dialog.setDialogPane(dialogPane);
        dialog.showAndWait();
    }

    public void sendEmail(){
        String receiver = "mcfacebook911@gmail.com";
        String sender = "amoungus69@sussy.com";
        String host = "127.0.0.1";
        Properties properties = System.getProperties();
//        Session session = Session.de
    }

    public void resetPassword(ActionEvent actionEvent) {
        Alert newAlert = new Alert(Alert.AlertType.CONFIRMATION);
        newAlert.setContentText("Send reset request to your email?");
        newAlert.setHeaderText("Reset Password");
        newAlert.showAndWait();
    }

    // public void changeEmail(ActionEvent actionEvent) {
    //     Alert newAlert = new Alert(Alert.AlertType.CONFIRMATION);
    //     newAlert.setContentText("Send email change request to user's email?");
    //     newAlert.setHeaderText("Change Email");
    //     newAlert.showAndWait();
    // }

    @Override
    public void myInitialize() {
        currentUser = Main.getCurrentUser();
        UserData userData = currentUser.getSessionUserData();
        LocalDate localDate;

        usernameText.setText(userData.getUsername());
        addressField.setText(userData.getAddress());
        emailField.setText(userData.getEmail());

        localDate = userData.getDateCreated().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        dateJoinedPicker.setValue(localDate);

        String name = userData.getDisplayName();
        if (!name.isEmpty()) {
            nameLabel.setText(name);
            nameField.setText(userData.getDisplayName());
        }
        
        String gender = userData.getGender();
        if (gender.equals("male")) {
            maleRadioButton.setSelected(true);
        } else {
            femaleRadioButton.setSelected(true);
        }

        Date dob = userData.getDateOfBirth();
        if (dob != null) {
            localDate = dob.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            dateOfBirthPicker.setValue(localDate);
        } else {
            dateOfBirthPicker.setValue(null);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sceneManager = Main.getSceneManager();

        returnToMainButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    sceneManager.addScene("Main", "main-messaging.fxml");
                    sceneManager.switchScene("Main");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        editProfileButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                nameLabel.setVisible(false);
                nameField.setVisible(true);

                returnToMainButton.setVisible(false);
                editProfileButton.setVisible(false);
                confirmButton.setVisible(true);
                cancelButton.setVisible(true);
                
                addressField.setDisable(false);
                maleRadioButton.setDisable(false);
                femaleRadioButton.setDisable(false);
                dateOfBirthPicker.setDisable(false);
                changePasswordButton.setDisable(false);
                resetPasswordButton.setDisable(false);
            }
        });

        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                usernameText.setVisible(true);
                nameLabel.setVisible(true);
                nameField.setVisible(false);

                editProfileButton.setVisible(true);
                returnToMainButton.setVisible(true);
                confirmButton.setVisible(false);
                cancelButton.setVisible(false);
                
                addressField.setDisable(true);
                maleRadioButton.setDisable(true);
                femaleRadioButton.setDisable(true);
                dateOfBirthPicker.setDisable(true);

                changePasswordButton.setDisable(true);
                resetPasswordButton.setDisable(true);
            }
        });

        confirmButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                UserData userData = currentUser.getSessionUserData();
                String address = addressField.getText();
                String gender = maleRadioButton.isSelected() ? "male" : "female";
                String name = nameField.getText();
                LocalDate date = dateOfBirthPicker.getValue();
                Date dob;
                boolean isChanged = false;

                if (date != null) {
                    if (date.isAfter(LocalDate.now())) {
                        new Alert(AlertType.ERROR, "Date of birth invalid!").showAndWait();
                        return;
                    }
                    dob = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
                } else {
                    dob = null;
                }

                if (!address.equals(userData.getAddress())) {
                    userData.setAddress(address);
                    isChanged = true;
                }

                if (!gender.equals(userData.getGender())) {
                    userData.setGender(gender);
                    isChanged = true;
                }

                if (dob != null && !dob.equals(userData.getDateOfBirth())) {
                    userData.setDateOfBirth(dob);
                    isChanged = true;
                }

                if (!name.equals(userData.getDisplayName()) && !name.isEmpty()) {
                    userData.setDisplayName(name);
                    isChanged = true;
                }

                if (isChanged) {
                    if (currentUser.myQuery.users().updateUser(userData)) {
                        new Alert(AlertType.INFORMATION, "Update profile success!").showAndWait();
                        nameField.setText(name);
                        nameLabel.setText(name);
                    }
                }

                nameField.setVisible(false);
                nameLabel.setVisible(true);

                returnToMainButton.setVisible(true);
                confirmButton.setVisible(false);
                cancelButton.setVisible(false);
                
                addressField.setDisable(true);
                maleRadioButton.setDisable(true);
                femaleRadioButton.setDisable(true);
                dateOfBirthPicker.setDisable(true);
                editProfileButton.setVisible(true);
            }
        });

        changePasswordButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                try {
                    sceneManager.addScene("ChangePassword", "change-password.fxml");
                    sceneManager.switchScene("ChangePassword");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        resetPasswordButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                UserData userData = currentUser.getSessionUserData();
                if (currentUser.resetPassword(userData.getEmail())) {
                    new Alert(AlertType.INFORMATION, "Password reset success!").showAndWait();
                }
            }
        });
    }
}