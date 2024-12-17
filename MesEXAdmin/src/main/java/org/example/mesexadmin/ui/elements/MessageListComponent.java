package org.example.mesexadmin.ui.elements;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.example.mesexadmin.data_class.MessageData;

public class MessageListComponent extends HBox {
    private final MessageData data;

    public MessageListComponent(MessageData message){
        data = message;

        this.getChildren().add( new Label( "[" + data.getTimeSent().toString() + " | " + data.getSenderName() + "]: " + data.getContent()));
    }

    public MessageData getMessage(){
        return data;
    }

    @Override
    public boolean equals(Object object) {
        if (object == this)
            return true;
        if (!(object instanceof MessageListComponent))
            return false;
        MessageListComponent messageListComponent = (MessageListComponent) object;
        return messageListComponent.getMessage().equals(this.data);
    }
}
