package org.example.mesexadmin.ui.elements;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.example.mesexadmin.data_class.MessageData;

public class MessageListComponent extends HBox {
    private final MessageData data;

    public MessageListComponent(MessageData message){
        data = message;

        this.getChildren().add( new Label(data.getContent()));
    }

    public MessageData getMessage(){
        return data;
    }
}
