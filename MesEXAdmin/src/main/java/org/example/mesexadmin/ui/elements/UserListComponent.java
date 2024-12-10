package org.example.mesexadmin.ui.elements;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.example.mesexadmin.data_class.UserData;

public class UserListComponent extends HBox {
    private final UserData data;

    public UserListComponent(UserData user){
        data = user;

        this.getChildren().add(new Label(user.getDisplayName()));
    }

    public UserData getUser(){
        return data;
    }
}
