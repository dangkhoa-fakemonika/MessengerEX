package org.example.mesex.ui.elements;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.bson.types.ObjectId;
import org.example.mesex.App;
import org.example.mesex.data_class.ConversationData;
import org.example.mesex.data_class.UserData;

import java.util.Objects;

public class ConversationListComponent extends HBox{
    private final ConversationData data;
    SimpleStringProperty displayData;
    ObjectId oppositeId;

    public ConversationListComponent(ConversationData conversation) {
        data = conversation;
        displayData = new SimpleStringProperty();
        UserData viewingUser = App.getCurrentUser().getSessionUserData();
        // Set display data here
        if (Objects.equals(data.getType(), "private")){
            if (viewingUser.getUserId().equals(conversation.getMembersId().getFirst())){
                displayData.set(conversation.getMembersName().getLast().get());
                oppositeId = conversation.getMembersId().getLast();
            }
            else{
                displayData.set(conversation.getMembersName().getFirst().get());
                oppositeId = conversation.getMembersId().getFirst();
            }
            data.setConversationName(displayData.get() + "'s private chat");
        }

        else
            displayData.set(data.getConversationName());
        this.getChildren().add(new Label(displayData.get()));
    }

    public ConversationData getConversation(){
        // Set selected data here
        return data;
    }

    public void setDisplayData(String newDisplayData){
        displayData.set(newDisplayData);
    }

    public String getDisplayData(){
        return displayData.get();
    }

    public ObjectId getOppositeId(){
        return oppositeId;
    }

}
