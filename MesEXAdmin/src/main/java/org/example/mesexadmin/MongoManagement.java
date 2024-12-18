package org.example.mesexadmin;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class MongoManagement {

    public MongoDatabase database;
    public MongoClient client;
    public static ListView<Label> importedMessages;
    String username;

    public MongoManagement(String uname) {
        client = new MongoClient("localhost", 27017);
        database = client.getDatabase("messenger-ex-app");
        username = uname;
        importedMessages = null;
    }
}
