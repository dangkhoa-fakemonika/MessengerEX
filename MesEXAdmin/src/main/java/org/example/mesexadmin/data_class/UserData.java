package org.example.mesexadmin.data_class;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Date;

import org.bson.Document;
import org.bson.types.ObjectId;

public class UserData {
    private ObjectId userId;

    public SimpleStringProperty username;
    public SimpleStringProperty displayName;
    public SimpleStringProperty email;

    public SimpleStringProperty status;
    public SimpleObjectProperty<Date> lastLogin;
    public SimpleObjectProperty<Date> dateCreated;
    public SimpleStringProperty role;

    
    public ArrayList<ObjectId> friend;
    public ArrayList<ObjectId> blocked;

    public SimpleStringProperty address;
    public SimpleObjectProperty<Date> dateOfBirth;
    public SimpleStringProperty gender;
    public SimpleStringProperty passwordHashed;

    // public SimpleListProperty<SimpleStringProperty> friend;
    // public SimpleListProperty<SimpleStringProperty> blocked;

    public UserData() {

        username = new SimpleStringProperty("");
        email = new SimpleStringProperty("");
        passwordHashed = new SimpleStringProperty("");
        
        displayName = new SimpleStringProperty("");
        dateOfBirth = new SimpleObjectProperty<Date>(null);
        address = new SimpleStringProperty("");
        gender = new SimpleStringProperty("male");
        
        status = new SimpleStringProperty("offline");
        role = new SimpleStringProperty("admin");
        dateCreated = new SimpleObjectProperty<Date>(null);
        lastLogin = new SimpleObjectProperty<Date>(null);
        
        friend = new ArrayList<>();
        blocked = new ArrayList<>();
    }

    public UserData(String newName, String newUsername, String newEmail, String currentStatus){
        displayName = new SimpleStringProperty(newName);
        username = new SimpleStringProperty(newUsername);
        email = new SimpleStringProperty(newEmail);
        status = new SimpleStringProperty(currentStatus);
        lastLogin = new SimpleObjectProperty<>(null);

        // ObservableList<SimpleStringProperty> observableList1 = FXCollections.observableArrayList(new ArrayList<>());
        // ObservableList<SimpleStringProperty> observableList2 = FXCollections.observableArrayList(new ArrayList<>());

        // friend = new SimpleListProperty<>(observableList1);
        // blocked = new SimpleListProperty<>(observableList2);

        // dateCreated = new SimpleStringProperty("");
        address = new SimpleStringProperty("");
        dateOfBirth = new SimpleObjectProperty<Date>(null);
        gender = new SimpleStringProperty("");
        passwordHashed = new SimpleStringProperty("");
        role = new SimpleStringProperty("");
    }

    public UserData(String newName){
        displayName = new SimpleStringProperty(newName);
        username = new SimpleStringProperty("");
        email = new SimpleStringProperty("");
        status = new SimpleStringProperty("");
        // lastLogin = new SimpleStringProperty("27 years ago");

        // dateCreated = new SimpleStringProperty("");
        address = new SimpleStringProperty("");
        dateOfBirth = new SimpleObjectProperty<Date>(null);
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

    public void setUserId(ObjectId userId) {
        this.userId = userId;
    }

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

    public void setLastLogin(Date lastLogin) {
        this.lastLogin.set(lastLogin);
    }

    public void setFriend(String friendCount) {
//        this.friendCount.set(friendCount);
    }

    public void setBlocked(ObservableList<SimpleStringProperty> blocked) {
//        this.blocked.set(blocked);
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated.set(dateCreated);
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public void setRole(String role) {
        this.role.set(role);
    }

    public void setDateOfBirth(Date dateOfBirth) {
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

//     public String getFriend() {
// //        return friendCount.get();
//         StringBuilder res =  new StringBuilder();
//         for (SimpleStringProperty i : friend){
//             if (!i.getValueSafe().trim().isEmpty())
//                 res.append(i.getValueSafe()).append(", ");
//         }

//         return !res.isEmpty() ? res.toString() : "No friends.";
//     }

    // public String getBlocked() {
    //     StringBuilder res =  new StringBuilder();
    //     for (SimpleStringProperty i : blocked){
    //         if (!i.getValueSafe().trim().isEmpty())
    //             res.append(i.getValueSafe()).append(", ");
    //     }

    //     return !res.isEmpty() ? res.toString() : "No blocked";
    // }

    public void setFriend(ArrayList<ObjectId> friend) {
        this.friend = friend;
    }

    public void setBlocked(ArrayList<ObjectId> blocked) {
        this.friend = blocked;
    }

    public ObjectId getUserId() {
        return this.userId;
    }

    public Date getLastLogin() {
        return lastLogin.get();
    }

    public Date getDateCreated() {
        return dateCreated.get();
    }

    public String getAddress() {
        return address.get();
    }

    public Date getDateOfBirth() {
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

    public SimpleObjectProperty<Date> getDateCreatedProperty() {
        return this.dateCreated;
    }

    public Document toDocument() {
        Document doc = new Document();
        doc.append("username", this.username.get())
            .append("displayName", this.displayName.get())
            .append("email", this.email.get())
            .append("status", this.status.get())
            .append("lastLogin", this.lastLogin.get())
            .append("dateCreated", this.dateCreated.get())
            .append("role", this.role.get())
            .append("friend", this.friend)
            .append("blocked", this.blocked)
            .append("address", this.address.get())
            .append("dateOfBirth", this.dateOfBirth.get())
            .append("gender", this.gender.get())
            .append("passwordHash", this.passwordHashed.get());
        return doc;
    }
}
