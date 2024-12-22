package org.example.mesex.ui.elements;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import org.example.mesex.data_class.MessageData;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;

public class MessageListComponent extends VBox {
    private final MessageData data;

    public MessageListComponent(MessageData message){
        data = message;
        
        Label usernameLabel = new Label(data.getSenderName() + " ");
        usernameLabel.setFont(Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, FontPosture.REGULAR, 13));
        
        Label dateLabel = new Label(formattedDate());
        dateLabel.setFont(Font.font(Font.getDefault().getFamily(), FontWeight.NORMAL, FontPosture.REGULAR, 10));
        
        HBox messageHeader = new HBox();
        messageHeader.setAlignment(Pos.CENTER_LEFT);
        messageHeader.getChildren().addAll(usernameLabel, dateLabel);

        Label contentLabel = new Label(data.getContent());
        contentLabel.setFont(Font.font(Font.getDefault().getFamily(), FontWeight.NORMAL, FontPosture.REGULAR, 13));
        
        HBox messageContent = new HBox();
        messageContent.getChildren().add(contentLabel);

        this.getChildren().addAll(messageHeader, messageContent);
    }

    public MessageData getMessage(){
        return data;
    }

    private String formattedDate() {
        LocalDate localDate = data.getTimeSent().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        String timePattern = "hh:mm a";
        SimpleDateFormat timeFormat = new SimpleDateFormat(timePattern);

        if (localDate.equals(today)) {
            return "Today at " + timeFormat.format(data.getTimeSent());
        } else if (localDate.equals(yesterday)) {
            return "Yesterday at " + timeFormat.format(data.getTimeSent());
        } else {
            String dayPattern = "dd/MM/yyyy";
            SimpleDateFormat dayFormat = new SimpleDateFormat(dayPattern);
            return dayFormat.format(data.getTimeSent()) + ", " + timeFormat.format(data.getTimeSent());
        }
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
