module org.example.mesex {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.desktop;

    opens org.example.mesex to javafx.fxml;
    exports org.example.mesex;
    exports org.example.mesex.data_class;
    opens org.example.mesex.data_class to javafx.fxml;
}