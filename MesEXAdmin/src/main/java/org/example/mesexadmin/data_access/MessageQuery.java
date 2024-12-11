package org.example.mesexadmin.data_access;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.example.mesexadmin.MongoManagement;
import org.example.mesexadmin.data_class.MessageData;

import java.util.ArrayList;

public class MessageQuery {
    MongoManagement mongoManagement;

    MessageQuery(MongoManagement mongoManagement){
        this.mongoManagement = mongoManagement;
    }

    // look up message
    public ArrayList<MessageData> lookUpByUser(String id){
        MongoCollection<Document> messages = mongoManagement.database.getCollection("messages");
        ArrayList<Document> results = new ArrayList<>();
        messages.find(Filters.eq("senderId", id)).into(results);
        ArrayList<MessageData> data = new ArrayList<>();
        for (Document res : results){
            data.add(documentToMessage(res));
        }

        return data;
    }

    public ArrayList<MessageData> matchStringByUser(String key, String id){
        MongoCollection<Document> messages = mongoManagement.database.getCollection("messages");
        ArrayList<Document> results = new ArrayList<>();
        messages.find(
                Filters.and(Filters.regex("content", key), Filters.eq("_id", id))
        ).into(results);

        ArrayList<MessageData> data = new ArrayList<>();
        for (Document res : results){
            data.add(documentToMessage(res));
        }

        return data;
    }

    public ArrayList<MessageData> matchStringByConversation(String key, String id){
        MongoCollection<Document> messages = mongoManagement.database.getCollection("messages");
        ArrayList<Document> results = new ArrayList<>();
        messages.find(
                Filters.and(Filters.regex("content", key), Filters.eq("_id", id))
        ).into(results);

        ArrayList<MessageData> data = new ArrayList<>();
        for (Document res : results){
            data.add(documentToMessage(res));
        }

        return data;
    }

    public ArrayList<MessageData> lookUpByConv(String id){
        MongoCollection<Document> messages = mongoManagement.database.getCollection("messages");
        ArrayList<Document> results = new ArrayList<>();
        messages.find(Filters.eq("conversationId", id)).into(results);
        ArrayList<MessageData> data = new ArrayList<>();
        for (Document res : results){
            data.add(documentToMessage(res));
        }

        return data;
    }

    public ArrayList<MessageData> lookUpAll(String id){
        MongoCollection<Document> messages = mongoManagement.database.getCollection("messages");
        ArrayList<Document> results = new ArrayList<>();
        messages.find().into(results);
        ArrayList<MessageData> data = new ArrayList<>();
        for (Document res : results){
            data.add(documentToMessage(res));
        }

        return data;
    }

    public boolean postMessage(MessageData message){
        MongoCollection<Document> messages = mongoManagement.database.getCollection("messages");

        try {
            messages.insertOne(message.toDocument());
        } catch (MongoWriteException e){
            return false;
        }

        return true;
    }

    // delete message
    public boolean remove(MessageData message){
        MongoCollection<Document> messages = mongoManagement.database.getCollection("messages");

        try {
            messages.deleteOne(Filters.eq("_id", message.getMessageId()));
        } catch (MongoWriteException e){
            return false;
        }

        return true;
    }

    public boolean removeAllByUser(String id){
        MongoCollection<Document> messages = mongoManagement.database.getCollection("messages");

        try {
            messages.deleteMany(Filters.eq("senderId", id));
        } catch (MongoWriteException e){
            return false;
        }

        return true;
    }

    public boolean removeAllByGroup(String id){
        MongoCollection<Document> messages = mongoManagement.database.getCollection("messages");

        try {
            messages.deleteMany(Filters.eq("conversationId", id));
        } catch (MongoWriteException e){
            return false;
        }

        return true;
    }

    private MessageData documentToMessage(Document messageDocument){
        MessageData msg = new MessageData();

        msg.setMessageId(messageDocument.getObjectId("_id"));
        msg.setSenderId(messageDocument.getObjectId("senderId"));
        msg.setConversationId(messageDocument.getString("conversationId"));
        msg.setReceiverId(messageDocument.getObjectId("receiverId"));
        msg.setTimeSent(messageDocument.getDate("timeSent"));
        msg.setContent(messageDocument.getString("content"));

        return msg;
    }
}
