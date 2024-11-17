package org.example.mesexadmin.data_class;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Arrays;

public class ConversationData {

    public SimpleStringProperty dateCreated;
    public SimpleStringProperty conversationName;
    public SimpleListProperty<SimpleStringProperty> membersId;
    public SimpleListProperty<SimpleStringProperty> moderatorsId;
    public SimpleStringProperty lastMessageId;
    public SimpleStringProperty type;

    public ConversationData(String newGroupName, String newHostID){
        dateCreated = new SimpleStringProperty("b");
        conversationName = new SimpleStringProperty(newGroupName);
        ObservableList<SimpleStringProperty> observableList = FXCollections.observableArrayList(new ArrayList<>());
        membersId = new SimpleListProperty<>(observableList);
        membersId.add(new SimpleStringProperty(newHostID));
        ObservableList<SimpleStringProperty> observableList1 = FXCollections.observableArrayList(new ArrayList<>());
        moderatorsId = new SimpleListProperty<>(observableList1);
        lastMessageId = new SimpleStringProperty("a");
        type = new SimpleStringProperty("chat");
    }

    public String getMembersId() {
        StringBuilder res =  new StringBuilder();
        for (SimpleStringProperty i : membersId){
            if (!i.getValueSafe().trim().isEmpty())
                res.append(i.getValueSafe()).append(", ");
        }

        return !res.isEmpty() ? res.toString() : "Empty Group";
    }

    public String getDateCreated() {
        return dateCreated.get();
    }

    public String getLastMessageId() {
        return lastMessageId.get();
    }

    public String getConversationName() {
        return conversationName.get();
    }

    public String getModeratorsId() {
        StringBuilder res =  new StringBuilder();
        for (SimpleStringProperty i : moderatorsId){
            if (!i.getValueSafe().trim().isEmpty())
                res.append(i.getValueSafe()).append(", ");
        }

        return !res.isEmpty() ? res.toString() : "No Admin";
    }

    public String getType() {
        return type.get();
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated.set(dateCreated);
    }

    public void setModeratorsId(String moderatorsId) {
//        this.hostsId.set(hostsId);
    }

    public void setMembersId(String membersId) {
//        this.participantIDs.set(participantIDs);
    }

    public void addIDs(SimpleStringProperty[] newIDs){
        membersId.addAll(Arrays.asList(newIDs));
    }

    public void removeID(String id){
        membersId.remove(new SimpleStringProperty(id));
    }

    public void setLastMessageId(String lastMessageId) {
        this.lastMessageId.set(lastMessageId);
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public void setConversationName(String conversationName) {
        this.conversationName.set(conversationName);
    }
}
