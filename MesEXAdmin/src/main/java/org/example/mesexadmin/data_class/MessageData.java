package org.example.mesexadmin.data_class;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Date;

public class MessageData {
    ObjectId messageId;
    ObjectId senderId;
    ObjectId receiverId;
    StringProperty content;
    SimpleObjectProperty<Date> timeSent;
    SimpleStringProperty conversationId;

    public MessageData(){
        senderId = null;
        receiverId = null;
        content = new SimpleStringProperty("");
        timeSent = new SimpleObjectProperty<>(null);
        conversationId = new SimpleStringProperty("null");
    }

    public MessageData(String message, String sender, String receiver){
        senderId = null;
        receiverId = null;
        content = new SimpleStringProperty(message);
        timeSent = new SimpleObjectProperty<>(null);
        conversationId = new SimpleStringProperty("null");
    }

    public ObjectId getMessageId() {
        return messageId;
    }

    public ObjectId getSenderId() {
        return senderId;
    }

    public String getContent() {
        return content.get();
    }

    public String getConversationId() {
        return conversationId.get();
    }

    public ObjectId getReceiverId() {
        return receiverId;
    }

    public Date getTimeSent() {
        return timeSent.get();
    }

    public void setMessageId(ObjectId messageId) {
        this.messageId = messageId;
    }

    public void setReceiverId(ObjectId receiverId) {
        this.receiverId = receiverId;
    }

    public void setSenderId(ObjectId senderId) {
        this.senderId = senderId;
    }

    public void setContent(String content) {
        this.content.set(content);
    }

    public void setConversationId(String conversationId) {
        this.conversationId.set(conversationId);
    }

    public void setTimeSent(Date timeSent) {
        this.timeSent.set(timeSent);
    }

    public Document toDocument(){
        Document doc = new Document();
        doc.append("senderId",this.senderId)
            .append("receiverId",this.receiverId)
            .append("content",this.content.get())
            .append("timeSent",this.timeSent.get())
            .append("conversationId",this.conversationId.get());
        return doc;
    }
}
