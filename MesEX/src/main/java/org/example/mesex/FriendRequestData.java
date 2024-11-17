package org.example.mesex;

import javafx.beans.property.SimpleStringProperty;

public class FriendRequestData {
    public SimpleStringProperty senderId;
    public SimpleStringProperty receiverId;
    public SimpleStringProperty timeSent;

    public FriendRequestData(String reporter, String reportID, String time){
        senderId = new SimpleStringProperty(reportID);
        receiverId = new SimpleStringProperty(reporter);
        timeSent = new SimpleStringProperty(time);
    }

    public String getSenderId() {
        return senderId.get();
    }

    public String getTimeSent() {
        return timeSent.get();
    }

    public String getReceiverId() {
        return receiverId.get();
    }

    public void setSenderId(String senderId) {
        this.senderId.set(senderId);
    }

    public void setTimeSent(String timeSent) {
        this.timeSent.set(timeSent);
    }

    public void setReceiverId(String receiverId) {
        this.receiverId.set(receiverId);
    }
}
