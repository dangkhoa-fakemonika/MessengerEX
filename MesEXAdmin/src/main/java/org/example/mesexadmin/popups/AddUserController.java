package org.example.mesexadmin.popups;

import javafx.event.ActionEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AddUserController {

    public void closePopUp(ActionEvent actionEvent){
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.close();
    }
}
