module org.example.mesexadmin {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;

    opens org.example.mesexadmin to javafx.fxml;
    exports org.example.mesexadmin;
    exports org.example.mesexadmin.data_class;
    opens org.example.mesexadmin.data_class to javafx.fxml;
}