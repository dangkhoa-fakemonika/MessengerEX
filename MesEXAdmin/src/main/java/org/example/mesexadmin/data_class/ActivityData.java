package org.example.mesexadmin.data_class;

import java.util.Date;

import org.bson.Document;
import org.bson.types.ObjectId;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class ActivityData {

    private SimpleStringProperty username;
    private ObjectId activityId;
    private ObjectId userId;
    private SimpleObjectProperty<Date> loginDate;

    // public ActivityData(String dataID, String dataTime, String dataAction){
        // id = new SimpleStringProperty(dataID);
        // time = new SimpleStringProperty(dataTime);
        // action = new SimpleStringProperty(dataAction);
    // }

    public ActivityData() {
        activityId = new ObjectId();
        loginDate = new SimpleObjectProperty<Date>(null);
        username = new SimpleStringProperty("");
    }

    public ObjectId getActivityId() {
        return activityId;
    }

    public void setActivityId(ObjectId activityId) {
        this.activityId = activityId;
    } 

    public ObjectId getUserId() {
        return this.userId;
    }

    public void setUserId(ObjectId userId) {
        this.userId = userId;
    }

    public Date getLoginDate() {
        return this.loginDate.get();
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate.set(loginDate);
    }

    public String getUsername() {
        return this.username.get();
    }

    public SimpleStringProperty getUsernameProperty() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public Document toDocument() {
        Document activityData = new Document();

        activityData.append("_id", this.getActivityId())
                    .append("loginDate", this.getLoginDate())
                    .append("userId", this.getUserId())
                    .append("name", this.getUsername());

        return activityData;
    }

    // public void setAction(String action) {
    //     this.action.set(action);
    // }

    // public void setId(String id) {
    //     this.id.set(id);
    // }

    // public void setTime(String time) {
    //     this.time.set(time);
    // }

    // public String getAction() {
    //     return action.get();
    // }

    // public String getId() {
    //     return id.get();
    // }

    // public String getTime() {
    //     return time.get();
    // }
}
