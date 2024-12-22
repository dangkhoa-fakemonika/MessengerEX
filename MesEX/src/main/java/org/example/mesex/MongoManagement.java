package org.example.mesex;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class MongoManagement {

    public MongoDatabase database;
    public MongoClient client;

    public MongoManagement() {
        
        client = new MongoClient(App.appProperties.getProperty("dbhost"), Integer.parseInt(App.appProperties.getProperty("dbport")));
        database = client.getDatabase(App.appProperties.getProperty("database"));
    }
}
