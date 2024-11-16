package org.example.mesexadmin.data_class;

import javafx.beans.property.SimpleStringProperty;

public class UserData {
    public SimpleStringProperty name;
    public SimpleStringProperty id;
    public SimpleStringProperty email;
    public SimpleStringProperty status;
    public SimpleStringProperty lastActive;
    public SimpleStringProperty friendCount;
    public SimpleStringProperty dateCreated;


    public UserData(String newName, String newId, String newEmail){
        name = new SimpleStringProperty(newName);
        id = new SimpleStringProperty(newId);
        email = new SimpleStringProperty(newEmail);
        status = new SimpleStringProperty("Active");
        lastActive = new SimpleStringProperty("27 years ago");
        friendCount = new SimpleStringProperty("-1");
        dateCreated = new SimpleStringProperty("Tomorrow");
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public void setLastActive(String lastActive) {
        this.lastActive.set(lastActive);
    }

    public void setFriendCount(String friendCount) {
        this.friendCount.set(friendCount);
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated.set(dateCreated);
    }

    public String getName(){
        return name.get();
    }

    public String getEmail() {
        return email.get();
    }

    public String getId() {
        return id.get();
    }

    public String getStatus() {
        return status.get();
    }

    public String getFriendCount() {
        return friendCount.get();
    }

    public String getLastActive() {
        return lastActive.get();
    }

    public String getDateCreated() {
        return dateCreated.get();
    }
}
