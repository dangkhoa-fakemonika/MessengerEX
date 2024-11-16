package org.example.mesexadmin.data_class;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Arrays;

public class GroupData {

    public SimpleStringProperty groupID;
    public SimpleStringProperty groupName;
    public SimpleListProperty<SimpleStringProperty> participantIDs;
    public SimpleStringProperty hostID;

    public GroupData(String newGroupID, String newGroupName, String newHostID){
        groupID = new SimpleStringProperty(newGroupID);
        groupName = new SimpleStringProperty(newGroupName);
        ObservableList<SimpleStringProperty> observableList = FXCollections.observableArrayList(new ArrayList<>());
        participantIDs = new SimpleListProperty<>(observableList);
        participantIDs.add(new SimpleStringProperty(newHostID));
        hostID = new SimpleStringProperty(newHostID);
    }

    public String getParticipantIDs() {
        StringBuilder res =  new StringBuilder();
        for (SimpleStringProperty i : participantIDs){
            if (!i.getValueSafe().trim().isEmpty())
                res.append(i.getValueSafe()).append(", ");
        }

        return !res.isEmpty() ? res.toString() : "Empty Group";
    }

    public String getGroupID() {
        return groupID.get();
    }

    public String getGroupName() {
        return groupName.get();
    }

    public String getHostID() {
        return hostID.get();
    }

    public void setGroupID(String groupID) {
        this.groupID.set(groupID);
    }

    public void setHostID(String hostID) {
        this.hostID.set(hostID);
    }

    public void setParticipantIDs(SimpleStringProperty participantIDs) {
//        this.participantIDs.set(participantIDs);
    }

    public void addIDs(SimpleStringProperty[] newIDs){
        participantIDs.addAll(Arrays.asList(newIDs));
    }

    public void removeID(String id){
        participantIDs.remove(new SimpleStringProperty(id));
    }

    public void setGroupName(String groupName) {
        this.groupName.set(groupName);
    }
}
