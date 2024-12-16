package org.example.mesexadmin.data_access;

import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.example.mesexadmin.MongoManagement;
import org.example.mesexadmin.data_class.MessageData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class MessageQuery {
    MongoManagement mongoManagement;

    MessageQuery(MongoManagement mongoManagement){
        this.mongoManagement = mongoManagement;
    }

    // look up message
    public ArrayList<MessageData> lookUpByUser(ObjectId id){
        MongoCollection<Document> messages = mongoManagement.database.getCollection("messages");
        ArrayList<Document> results = new ArrayList<>();
        messages.find(Filters.eq("senderId", id)).into(results);
        ArrayList<MessageData> data = new ArrayList<>();
        for (Document res : results){
            data.add(documentToMessage(res));
        }

        return data;
    }

    public ArrayList<MessageData> lookUpByUserFilter(ObjectId id, String token){
        MongoCollection<Document> messages = mongoManagement.database.getCollection("messages");
        ArrayList<Document> results = new ArrayList<>();
        messages.find(Filters.and(Filters.eq("senderId", id), Filters.regex("content", token, "i")) ).into(results);
        ArrayList<MessageData> data = new ArrayList<>();
        for (Document res : results){
            data.add(documentToMessage(res));
        }

        return data;
    }


    public ArrayList<MessageData> lookUpByConv(ObjectId id){
        MongoCollection<Document> messages = mongoManagement.database.getCollection("messages");
        ArrayList<Document> results = new ArrayList<>();
        messages.find(Filters.eq("conversationId", id)).into(results);
        ArrayList<MessageData> data = new ArrayList<>();
        for (Document res : results){
            data.add(documentToMessage(res));
        }

        return data;
    }

    public ArrayList<MessageData> lookUpByConvFilter(ObjectId id, String token){
        MongoCollection<Document> messages = mongoManagement.database.getCollection("messages");
        ArrayList<Document> results = new ArrayList<>();
        messages.find(Filters.and(Filters.eq("conversationId", id), Filters.regex("content",token, "i"))).into(results);
        ArrayList<MessageData> data = new ArrayList<>();
        for (Document res : results){
            data.add(documentToMessage(res));
        }

        return data;
    }


    public ArrayList<MessageData> lookUpAll(ObjectId id){
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
    public boolean remove(ObjectId msgId){
        MongoCollection<Document> messages = mongoManagement.database.getCollection("messages");

        try {
            messages.deleteOne(Filters.eq("_id", msgId));
        } catch (MongoWriteException e){
            return false;
        }

        return true;
    }

    public boolean removeAllByUser(ObjectId id){
        MongoCollection<Document> messages = mongoManagement.database.getCollection("messages");

        try {
            messages.deleteMany(Filters.eq("senderId", id));
        } catch (MongoWriteException e){
            return false;
        }

        return true;
    }

    public boolean removeAllByGroup(ObjectId id){
        MongoCollection<Document> messages = mongoManagement.database.getCollection("messages");

        try {
            messages.deleteMany(Filters.eq("conversationId", id));
        } catch (MongoWriteException e){
            return false;
        }

        return true;
    }

    public int countChatOccurrences(ObjectId targetId, String type){
        MongoCollection<Document> messages = mongoManagement.database.getCollection("messages");

        Document result = messages.aggregate(Arrays.asList(
                Aggregates.match(Filters.eq("senderId", targetId)),
                Aggregates.lookup("conversations", "conversationId", "_id", "conversationData"),
                Aggregates.match(Filters.eq("conversationData.type", type)),
                Aggregates.project(Projections.fields(
                        Projections.computed("count", "$size : $conversationData")
                ))
        )).first();

        try{
            if (result == null)
                return 0;
            return result.getInteger("count");
        } catch (Exception e){
            return 0;
        }
    }

    public int countChatOccurrencesWithFilters(ObjectId targetId, String type){
        MongoCollection<Document> messages = mongoManagement.database.getCollection("messages");

        Document result = messages.aggregate(Arrays.asList(
                Aggregates.match(Filters.eq("senderId", targetId)),
                Aggregates.lookup("conversations", "conversationId", "_id", "conversationData"),
                Aggregates.match(Filters.eq("conversationData.type", type)),
                Aggregates.project(Projections.fields(
                        Projections.computed("count", "$size : $conversationData")
                ))
        )).first();

        try{
            if (result == null)
                return 0;
            return result.getInteger("count");
        } catch (Exception e){
            return 0;
        }
    }


    private MessageData documentToMessage(Document messageDocument){
        MessageData msg = new MessageData();

        msg.setMessageId(messageDocument.getObjectId("_id"));
        msg.setSenderId(messageDocument.getObjectId("senderId"));
        msg.setConversationId(messageDocument.getObjectId("conversationId"));
        msg.setTimeSent(messageDocument.getDate("timeSent"));
        msg.setContent(messageDocument.getString("content"));
        msg.setSenderName(messageDocument.getString("senderName"));

        return msg;
    }
}
