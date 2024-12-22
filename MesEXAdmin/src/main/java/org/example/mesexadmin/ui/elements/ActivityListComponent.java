package org.example.mesexadmin.ui.elements;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.example.mesexadmin.data_class.ActivityData;

public class ActivityListComponent extends HBox {
    private final ActivityData data;
    SimpleStringProperty displayData;

    public ActivityListComponent(ActivityData activity){
        data = activity;
        displayData = new SimpleStringProperty();
        displayData.set(data.getLoginDate().toString());

        this.getChildren().add(new Label(displayData.get()));
    }

    public ActivityData getData() {
        return data;
    }
}
