package org.example.mesexadmin;

import com.mongodb.MongoClient;
import com.mongodb.client.ChangeStreamIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Consumer;

public class MongoManagement {

    public MongoDatabase database;
    public MongoClient client;
    public MongoCollection<Document> messages;
    public static ListView<Label> importedMessages;
    String username;

    public MongoManagement(String uname) {
        client = new MongoClient("localhost", 27017); 
        database = client.getDatabase("messenger-ex");
        messages = database.getCollection("messages");
        username = uname;
        importedMessages = null;
    }

    public ArrayList<String> loadLocalMessages(){
        ArrayList<String> localMsg = new ArrayList<>();
        FindIterable<Document> msgs = messages.find();
        for (Document tempDoc : msgs) {
            localMsg.add(tempDoc.get("sender") + " : " + tempDoc.get("content"));
        }
        return localMsg;
    }

    public void showMessages(){
        ChangeStreamIterable<Document> changeStream = messages.watch();
        changeStream.forEach((Consumer<? super ChangeStreamDocument<Document>>) System.out::println);
    }

    public static void pushMessage(String t, ListView<Label> l){
        Label newLabel = new Label();
        newLabel.setText(t);
        l.getItems().add(newLabel);
    }

    public static void processPushMongo(ChangeStreamDocument<Document> changeDoc, ListView<Label> l){
        Document thisDoc = changeDoc.getFullDocument();
        System.out.println(changeDoc);
        if (thisDoc != null){
            System.out.println(thisDoc);
            pushMessage(thisDoc.get("sender").toString() + " : " + thisDoc.get("content"), l);
        }
    }

    public void pushNewMessage(String t){
        Document newDoc = new Document();
        newDoc.append("_id", new ObjectId());
        newDoc.append("sender", username);
        newDoc.append("content", t);
        messages.insertOne(newDoc);
    }
}
