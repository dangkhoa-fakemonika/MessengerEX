package org.example.mesexadmin.data_class;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;

public class ConversationData {

    ObjectId conversationId;
    SimpleObjectProperty<Date> dateCreated;
    SimpleStringProperty conversationName;

    ArrayList<ObjectId> membersId;
    ArrayList<ObjectId> moderatorsId;
    ArrayList<SimpleStringProperty> membersName;
    ArrayList<SimpleStringProperty> moderatorsName;

    ObjectId lastMessageId;
    SimpleStringProperty type;

    public ConversationData() {
        conversationId = new ObjectId();
        dateCreated = new SimpleObjectProperty<>();
        conversationName = new SimpleStringProperty();
        membersId = new ArrayList<>();
        moderatorsId = new ArrayList<>();
        membersName = new ArrayList<>();
        moderatorsName = new ArrayList<>();
        lastMessageId = null;
        type = new SimpleStringProperty();
    }

    public ConversationData(String newGroupName, String newHostID){
        dateCreated = new SimpleObjectProperty<Date>(null);
        conversationName = new SimpleStringProperty(newGroupName);
        membersId = null;
        moderatorsId = null;
        type = new SimpleStringProperty("chat");
    }

    public ObjectId getConversationId() {
        return conversationId;
    }

    public Date getDateCreated() {
        return dateCreated.get();
    }

    public ObjectId getLastMessageId() {
        return lastMessageId.get();
    }

    public String getConversationName() {
        return conversationName.get();
    }

    public ArrayList<SimpleStringProperty> getModeratorsName() {
        return moderatorsName;
    }

    public ArrayList<SimpleStringProperty> getMembersName() {
        return membersName;
    }

    public ArrayList<ObjectId> getMembersId() {
        return membersId;
    }

    public ArrayList<ObjectId> getModeratorsId() {
        return moderatorsId;
    }

    public void setConversationId(ObjectId conversationId) {
        this.conversationId = conversationId;
    }

    public String getType() {
        return type.get();
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated.set(dateCreated);
    }

    public void setModeratorsId(ArrayList<ObjectId>  moderatorsId) {
//        this.hostsId.set(hostsId);
        this.moderatorsId = moderatorsId;
    }

    public void setMembersId(ArrayList<ObjectId> membersId) {
//        this.participantIDs.set(participantIDs);
        this.membersId = membersId;
    }

    public void setMembersName(ArrayList<SimpleStringProperty> membersName) {
        this.membersName = membersName;
    }

    public void setModeratorsName(ArrayList<SimpleStringProperty> moderatorsName) {
        this.moderatorsName = moderatorsName;
    }

    public void addIDs(SimpleStringProperty[] newIDs){

    }

    public void removeID(String id){

    }

    public void setLastMessageId(ObjectId lastMessageId) {
        this.lastMessageId = lastMessageId;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public void setConversationName(String conversationName) {
        this.conversationName.set(conversationName);
    }

    public String getChatTarget(String currentUsername) {
        String result = "";
        for (SimpleStringProperty id : this.membersName) {
            if (!id.get().equals(currentUsername))
                result = id.get();
        }
        return result;
    }

    public Document toDocument(){
        Document doc = new Document();
        doc.append("_id", this.conversationId)
        .append("dateCreated", this.dateCreated.get())
        .append("conversationName", this.conversationName.get())
        .append("membersId", this.membersId)
        .append("moderatorsId", this.moderatorsId)
        .append("lastMessageId", this.lastMessageId)
        .append("type", this.type.get());

        return doc;
    }
}
