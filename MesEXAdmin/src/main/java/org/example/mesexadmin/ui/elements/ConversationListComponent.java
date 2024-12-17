package org.example.mesexadmin.ui.elements;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.example.mesexadmin.Main;
import org.example.mesexadmin.data_class.ConversationData;
import org.example.mesexadmin.data_class.UserData;

import java.util.Objects;

public class ConversationListComponent extends HBox{
    private final ConversationData data;
    SimpleStringProperty displayData;

    public ConversationListComponent(ConversationData conversation) {
        data = conversation;
        displayData = new SimpleStringProperty();
        UserData viewingUser = Main.getCurrentUser().getSessionUserData();
        // Set display data here
        if (Objects.equals(data.getType(), "private")){
            if (viewingUser.getUserId().equals(conversation.getMembersId().getFirst()))
                displayData.set(conversation.getMembersName().getLast().get());
            else
                displayData.set(conversation.getMembersName().getFirst().get());
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

}
