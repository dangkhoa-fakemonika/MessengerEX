package org.example.mesex;

import javafx.beans.property.SimpleStringProperty;

public class MessageData {
    public SimpleStringProperty senderId;
    public SimpleStringProperty receiverId;
    public SimpleStringProperty content;
    public SimpleStringProperty timeSent;
    public SimpleStringProperty conversationId;

    public MessageData(String message, String sender, String receiver){
        senderId = new SimpleStringProperty(sender);
        receiverId = new SimpleStringProperty(receiver);
        content = new SimpleStringProperty(message);
        timeSent = new SimpleStringProperty("2024");
        conversationId = new SimpleStringProperty("null");
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

    public String getTimeSent() {
        return timeSent.get();
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

    public void setTimeSent(String timeSent) {
        this.timeSent.set(timeSent);
    }
}
