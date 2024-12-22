module org.example.mesexadmin {
    requires java.naming;
    requires java.desktop;
    requires javafx.fxml;
    requires jakarta.mail;
    requires transitive javafx.controls;
    requires transitive javafx.base;
    requires transitive mongo.java.driver;

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