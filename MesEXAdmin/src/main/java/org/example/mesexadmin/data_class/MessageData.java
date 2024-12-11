package org.example.mesexadmin.data_class;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Date;

public class MessageData {
    public ObjectId messageId;
    public SimpleStringProperty senderId;
    public SimpleStringProperty receiverId;
    public StringProperty content;
    public SimpleObjectProperty<Date> timeSent;
    public SimpleStringProperty conversationId;

    public MessageData(){
        senderId = new SimpleStringProperty("");
        receiverId = new SimpleStringProperty("");
        content = new SimpleStringProperty("");
        timeSent = new SimpleObjectProperty<>(null);
        conversationId = new SimpleStringProperty("null");
    }

    public MessageData(String message, String sender, String receiver){
        senderId = new SimpleStringProperty(sender);
        receiverId = new SimpleStringProperty(receiver);
        content = new SimpleStringProperty(message);
        timeSent = new SimpleObjectProperty<>(null);
        conversationId = new SimpleStringProperty("null");
    }

    public ObjectId getMessageId() {
        return messageId;
    }

    public String getSenderId() {
        return senderId.get();
    }

    public String getContent() {
        return content.get();
    }

    public String getConversationId() {
        return conversationId.get();
    }

    public String getReceiverId() {
        return receiverId.get();
    }

    public Date getTimeSent() {
        return timeSent.get();
    }

    public void setMessageId(ObjectId messageId) {
        this.messageId = messageId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId.set(receiverId);
    }

    public void setSenderId(String senderId) {
        this.senderId.set(senderId);
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
        doc.append("senderId",this.senderId.get())
            .append("receiverId",this.receiverId.get())
            .append("content",this.content.get())
            .append("timeSent",this.timeSent.get())
            .append("conversationId",this.conversationId.get());
        return doc;
    }
}
