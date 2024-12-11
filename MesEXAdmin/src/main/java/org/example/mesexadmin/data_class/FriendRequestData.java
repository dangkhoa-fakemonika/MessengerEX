package org.example.mesexadmin.data_class;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import org.bson.types.ObjectId;

import java.util.Date;

public class FriendRequestData {
    ObjectId requestId;
    ObjectId senderId;
    ObjectId receiverId;

    SimpleStringProperty senderName;
    SimpleStringProperty receiverName;
    SimpleObjectProperty<Date> timeSent;

    public FriendRequestData(){
        requestId = null;
        senderName = new SimpleStringProperty("reportID");
        receiverName = new SimpleStringProperty("reporter");
        timeSent = new SimpleObjectProperty<Date>(null);
    }

    public ObjectId getRequestId() {
        return requestId;
    }

    public String getSenderName() {
        return senderName.get();
    }

    public Date getTimeSent() {
        return timeSent.get();
    }

    public String getReceiverName() {
        return receiverName.get();
    }

    public ObjectId getReceiverId() {
        return receiverId;
    }

    public ObjectId getSenderId() {
        return senderId;
    }

    public void setRequestId(ObjectId requestId) {
        this.requestId = requestId;
    }

    public void setSenderName(String senderName) {
        this.senderName.set(senderName);
    }

    public void setTimeSent(Date timeSent) {
        this.timeSent.set(timeSent);
    }

    public void setReceiverName(String receiverName) {
        this.receiverName.set(receiverName);
    }

    public void setSenderId(ObjectId senderId) {
        this.senderId = senderId;
    }

    public void setReceiverId(ObjectId receiverId) {
        this.receiverId = receiverId;
    }
}
