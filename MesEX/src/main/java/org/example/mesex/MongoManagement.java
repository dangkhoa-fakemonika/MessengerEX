package org.example.mesex;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class MongoManagement {

    public MongoDatabase database;
    public MongoClient client;

    public MongoManagement() {
        client = new MongoClient("localhost", 27017);
        System.out.println("Mongo created");

        database = client.getDatabase("messenger-ex");
        System.out.println("Database got");

        for (String s : client.listDatabaseNames()) {
            System.out.println(s);
        }
    }

    public static void main(String[] args) {
        MongoManagement mm = new MongoManagement();
    }
}
