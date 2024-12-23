package org.example.mesex.data_class;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FriendRequestData {
    ObjectId requestId;
    ObjectId senderId;
    ObjectId receiverId;

    SimpleStringProperty senderUsername;
    SimpleStringProperty receiverUsername;
    SimpleObjectProperty<Date> timeSent;

    public FriendRequestData(){
        requestId = new ObjectId();
        senderUsername = new SimpleStringProperty("");
        receiverUsername = new SimpleStringProperty("");
        timeSent = new SimpleObjectProperty<Date>(null);
    }

    public ObjectId getRequestId() {
        return requestId;
    }

    public String getSenderUsername() {
        return senderUsername.get();
    }

    // public Date getTimeSent() {
    //     return timeSent.get();
    // }

    public String getFormattedTimeSent() {
        String pattern = "MM/dd/yyyy HH:mm:ss";
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);

        return dateFormat.format(this.timeSent.get()); 
    }

    public String getReceiverUsername() {
        return receiverUsername.get();
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

    public void setSenderUserame(String senderName) {
        this.senderUsername.set(senderName);
    }

    public void setTimeSent(Date timeSent) {
        this.timeSent.set(timeSent);
    }

    public void setReceiverUserame(String receiverName) {
        this.receiverUsername.set(receiverName);
    }

    public void setSenderId(ObjectId senderId) {
        this.senderId = senderId;
    }

    public void setReceiverId(ObjectId receiverId) {
        this.receiverId = receiverId;
    }

    public SimpleStringProperty getSenderUsernameProperty() {
        return this.senderUsername;
    }

    public SimpleStringProperty getReceiverUsernameProperty() {
        return this.receiverUsername;
    }

    public Document toDocument(){
        Document doc = new Document();
        doc.append("_id",this.requestId)
            .append("senderId",this.senderId)
            .append("receiverId",this.receiverId)
            .append("timeSent", this.timeSent.get());
        return doc;
    }

    @Override
    public boolean equals(Object object) {
        if (object == this)
            return true;
        if (!(object instanceof FriendRequestData))
            return false;
        FriendRequestData friendRequestData = (FriendRequestData) object;
        return this.requestId.equals(friendRequestData.getRequestId());
    }
}