package org.example.mesexadmin.ui.elements;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.example.mesexadmin.data_class.ConversationData;

public class ConversationListComponent extends HBox{
    private final ConversationData data;

    public ConversationListComponent(ConversationData conversation) {
        data = conversation;
        // Set display data here
        this.getChildren().add(new Label(data.getConversationName()));
    }

    public ConversationData getConversation(){
        // Set selected data here
        return data;
    }


}
