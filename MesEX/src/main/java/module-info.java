module org.example.mesex {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.base;
    requires java.naming;
    requires transitive mongo.java.driver;
    requires jakarta.mail;
    requires java.desktop;

    exports org.example.mesex;
    exports org.example.mesex.data_class;
    exports org.example.mesex.data_access;
    exports org.example.mesex.ui.user_level;

    opens org.example.mesex to javafx.fxml;
    opens org.example.mesex.data_class to javafx.fxml;
    opens org.example.mesex.ui.user_level to javafx.fxml;
    opens org.example.mesex.ui.elements to javafx.fxml;
}