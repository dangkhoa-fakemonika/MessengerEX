package org.example.mesexadmin.data_class;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

    public ConversationData() {}

    public ConversationData(String newGroupName, String newHostID){
        dateCreated = new SimpleObjectProperty<Date>(null);
        conversationName = new SimpleStringProperty(newGroupName);
        ObservableList<SimpleStringProperty> observableList = FXCollections.observableArrayList(new ArrayList<>());
        membersId = null;
        ObservableList<SimpleStringProperty> observableList1 = FXCollections.observableArrayList(new ArrayList<>());
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

    public String getMembersId() {
        return "za placeholda";
    }

    public String getModeratorsId() {
        return "za placeholda";
    }

    public String getMembersName() {
        return "za placeholda";
    }

    public String getModeratorsName() {
        return "za placeholda";
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
    }

    public void setMembersId(ArrayList<ObjectId> membersId) {
//        this.participantIDs.set(participantIDs);
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
}
