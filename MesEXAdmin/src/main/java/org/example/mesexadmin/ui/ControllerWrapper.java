package org.example.mesexadmin.ui;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public interface ControllerWrapper extends Initializable {

    void myInitialize();

    @Override
    default void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
