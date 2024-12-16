package org.example.mesexadmin.ui.elements;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.example.mesexadmin.Main;
import org.example.mesexadmin.data_class.ConversationData;
import org.example.mesexadmin.data_class.UserData;

import java.util.Objects;

public class ConversationListComponent extends HBox{
    private final ConversationData data;

    public ConversationListComponent(ConversationData conversation) {
        data = conversation;
        UserData viewingUser = Main.getCurrentUser().getSessionUserData();
        // Set display data here
        if (Objects.equals(data.getType(), "private")){
            if (viewingUser.getUserId() == conversation.getMembersId().getFirst())
                this.getChildren().add(new Label(conversation.getMembersName().getFirst().get()));
            else
                this.getChildren().add(new Label(conversation.getMembersName().getLast().get()));
        }

        else
            this.getChildren().add(new Label(data.getConversationName()));
    }

    public ConversationData getConversation(){
        // Set selected data here
        return data;
    }


}
