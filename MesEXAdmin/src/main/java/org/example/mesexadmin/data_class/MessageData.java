package org.example.mesexadmin.data_class;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.example.mesexadmin.ui.elements.MessageListComponent;

import java.util.Date;

public class MessageData {
    ObjectId messageId;
    ObjectId senderId;
    SimpleStringProperty senderName;
    SimpleStringProperty content;
    SimpleObjectProperty<Date> timeSent;
    ObjectId conversationId;

    public MessageData(){
        messageId = new ObjectId();
        senderId = null;
        senderName = new SimpleStringProperty();
        content = new SimpleStringProperty("");
        timeSent = new SimpleObjectProperty<>(new Date());
        conversationId = null;
    }

    public MessageData(String message, String sender, String receiver){
        messageId = new ObjectId();
        senderId = null;
        senderName = new SimpleStringProperty();
        content = new SimpleStringProperty(message);
        timeSent = new SimpleObjectProperty<>(null);
        conversationId = null;
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

    public ObjectId getConversationId() {
        return conversationId;
    }

    public String getSenderName() {
        return senderName.get();
    }

    public Date getTimeSent() {
        return timeSent.get();
    }

    public void setMessageId(ObjectId messageId) {
        this.messageId = messageId;
    }

    public void setSenderId(ObjectId senderId) {
        this.senderId = senderId;
    }

    public void setContent(String content) {
        this.content.set(content);
    }

    public void setConversationId(ObjectId conversationId) {
        this.conversationId = conversationId;
    }

    public void setTimeSent(Date timeSent) {
        this.timeSent.set(timeSent);
    }

    public void setSenderName(String senderName) {
        this.senderName.set(senderName);
    }

    public Document toDocument(){
        Document doc = new Document();
        doc.append("senderId",this.senderId)
            .append("senderName", this.senderName.get())
            .append("content",this.content.get())
            .append("timeSent",this.timeSent.get())
            .append("conversationId",this.conversationId);
        return doc;
    }

    @Override
    public boolean equals(Object object) {
        if (object == this)
            return true;
        if (!(object instanceof MessageData))
            return false;
        MessageData messageData = (MessageData) object;
        return messageData.getMessageId().equals(this.messageId);
    }
}
