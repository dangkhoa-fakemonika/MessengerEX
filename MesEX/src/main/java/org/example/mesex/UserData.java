package org.example.mesex;

import javafx.beans.property.SimpleStringProperty;

public class UserData {
    public SimpleStringProperty name;
    public SimpleStringProperty id;
    public SimpleStringProperty email;
    public SimpleStringProperty status;

    UserData(String newName, String newId, String newEmail){
        name = new SimpleStringProperty(newName);
        id = new SimpleStringProperty(newId);
        email = new SimpleStringProperty(newEmail);
        status = new SimpleStringProperty("Friend");
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
}
