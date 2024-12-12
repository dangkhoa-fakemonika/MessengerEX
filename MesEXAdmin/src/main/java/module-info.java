module org.example.mesexadmin {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires java.naming;
    requires mongo.java.driver;
    // requires javafx.web;
    
    // requires org.controlsfx.controls;
    // requires javax.mail;
    // requires eu.hansolo.tilesfx;
    // requires org.kordamp.bootstrapfx.core;
    // requires com.dlsc.formsfx;
    // requires net.synedra.validatorfx;

    exports org.example.mesexadmin;
    exports org.example.mesexadmin.data_class;
    exports org.example.mesexadmin.data_access;
    exports org.example.mesexadmin.ui.user_level;
    exports org.example.mesexadmin.ui.admin_level;

    opens org.example.mesexadmin to javafx.fxml;
    opens org.example.mesexadmin.data_class to javafx.fxml;
    opens org.example.mesexadmin.ui.user_level to javafx.fxml;
    opens org.example.mesexadmin.ui.admin_level to javafx.fxml;
    opens org.example.mesexadmin.ui.elements to javafx.fxml;
}