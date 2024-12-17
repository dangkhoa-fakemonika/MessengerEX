package org.example.mesexadmin.ui.elements;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.example.mesexadmin.data_class.UserData;

public class UserListComponent extends HBox {
    private final UserData data;
    SimpleStringProperty displayData;

    public UserListComponent(UserData user){
        data = user;
        displayData = new SimpleStringProperty();
        if (data.getDisplayName().isEmpty())
            displayData.set(data.getUsername());
        else
            displayData.set(data.getUsername() + " (" + data.getDisplayName() + ")");

        this.getChildren().add(new Label(displayData.get()));
    }

    public UserData getUser(){
        return data;
    }

    public void setDisplayData(String displayData) {
        this.displayData.set(displayData);
    }
}
