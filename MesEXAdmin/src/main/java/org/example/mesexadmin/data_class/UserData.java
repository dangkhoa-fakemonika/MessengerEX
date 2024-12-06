package org.example.mesexadmin.data_class;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class UserData {
    public SimpleStringProperty username;
    public SimpleStringProperty displayName;
    public SimpleStringProperty email;

    public SimpleStringProperty status;
    public SimpleStringProperty lastLogin;
    public SimpleStringProperty dateCreated;
    public SimpleStringProperty role;

    // public SimpleListProperty<SimpleStringProperty> friend;
    // public SimpleListProperty<SimpleStringProperty> blocked;
    public ArrayList<SimpleStringProperty> friend;
    public ArrayList<SimpleStringProperty> blocked;

    public SimpleStringProperty address;
    public SimpleStringProperty dateOfBirth;
    public SimpleStringProperty gender;
    public SimpleStringProperty passwordHashed;

    public UserData() {
        username = new SimpleStringProperty("");
        email = new SimpleStringProperty("");
        passwordHashed = new SimpleStringProperty("");
        
        displayName = new SimpleStringProperty("");
        dateOfBirth = new SimpleStringProperty("");
        address = new SimpleStringProperty("");
        gender = new SimpleStringProperty("");
        
        status = new SimpleStringProperty("");
        role = new SimpleStringProperty("admin");
        dateCreated = new SimpleStringProperty("");
        lastLogin = new SimpleStringProperty("");
        
        friend = new ArrayList<>();
        blocked = new ArrayList<>();
    }

    public UserData(String newName, String newUsername, String newEmail, String currentStatus){
        displayName = new SimpleStringProperty(newName);
        username = new SimpleStringProperty(newUsername);
        email = new SimpleStringProperty(newEmail);
        status = new SimpleStringProperty(currentStatus);
        lastLogin = new SimpleStringProperty("27 years ago");

        // ObservableList<SimpleStringProperty> observableList1 = FXCollections.observableArrayList(new ArrayList<>());
        // ObservableList<SimpleStringProperty> observableList2 = FXCollections.observableArrayList(new ArrayList<>());

        // friend = new SimpleListProperty<>(observableList1);
        // blocked = new SimpleListProperty<>(observableList2);

        dateCreated = new SimpleStringProperty("");
        address = new SimpleStringProperty("");
        dateOfBirth = new SimpleStringProperty("");
        gender = new SimpleStringProperty("");
        passwordHashed = new SimpleStringProperty("");
        role = new SimpleStringProperty("");
    }

//    public UserData(UserData u){
//        displayName = new SimpleStringProperty(u.getDisplayName());
//        username = new SimpleStringProperty(u.getUsername());
//        email = new SimpleStringProperty(u.getEmail());
//        status = new SimpleStringProperty(u.getStatus());
//        lastActive = new SimpleStringProperty(u.getLastActive());
//        friendCount = new SimpleStringProperty(u.getFriendCount());
//        dateCreated = new SimpleStringProperty(u.getDateCreated());
//    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public void setDisplayName(String displayName) {
        this.displayName.set(displayName);
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin.set(lastLogin);
    }

    public void setFriend(String friendCount) {
//        this.friendCount.set(friendCount);
    }

    public void setBlocked(ObservableList<SimpleStringProperty> blocked) {
//        this.blocked.set(blocked);
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated.set(dateCreated);
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public void setRole(String role) {
        this.role.set(role);
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth.set(dateOfBirth);
    }

    public void setGender(String gender) {
        this.gender.set(gender);
    }

    public void setPasswordHashed(String passwordHashed) {
        this.passwordHashed.set(passwordHashed);
    }

    public String getDisplayName(){
        return displayName.get();
    }

    public String getEmail() {
        return email.get();
    }

    public String getUsername() {
        return username.get();
    }

    public String getStatus() {
        return status.get();
    }

    public String getFriend() {
//        return friendCount.get();
        StringBuilder res =  new StringBuilder();
        for (SimpleStringProperty i : friend){
            if (!i.getValueSafe().trim().isEmpty())
                res.append(i.getValueSafe()).append(", ");
        }

        return !res.isEmpty() ? res.toString() : "No friends.";
    }

    public String getBlocked() {
        StringBuilder res =  new StringBuilder();
        for (SimpleStringProperty i : blocked){
            if (!i.getValueSafe().trim().isEmpty())
                res.append(i.getValueSafe()).append(", ");
        }

        return !res.isEmpty() ? res.toString() : "No blocked";
    }

    public String getLastLogin() {
        return lastLogin.get();
    }

    public String getDateCreated() {
        return dateCreated.get();
    }

    public String getAddress() {
        return address.get();
    }

    public String getDateOfBirth() {
        return dateOfBirth.get();
    }

    public String getGender() {
        return gender.get();
    }

    public String getRole() {
        return role.get();
    }

    public String getPasswordHashed() {
        return passwordHashed.get();
    }
}
