package org.example.mesex.data_class;

import javafx.beans.property.SimpleStringProperty;

public class ActivityData {
    public SimpleStringProperty id;
    public SimpleStringProperty time;
    public SimpleStringProperty action;

    public ActivityData(String dataID, String dataTime, String dataAction){
        id = new SimpleStringProperty(dataID);
        time = new SimpleStringProperty(dataTime);
        action = new SimpleStringProperty(dataAction);
    }

    public void setAction(String action) {
        this.action.set(action);
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public void setTime(String time) {
        this.time.set(time);
    }

    public String getAction() {
        return action.get();
    }

    public String getId() {
        return id.get();
    }

    public String getTime() {
        return time.get();
    }
}
