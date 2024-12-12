package org.example.mesexadmin.data_access;

import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import javafx.beans.property.SimpleStringProperty;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.example.mesexadmin.MongoManagement;
import org.example.mesexadmin.data_class.ConversationData;

import java.util.ArrayList;

public class ConversationQuery {
    MongoManagement mongoManagement;

    public ConversationQuery(MongoManagement mongoManagement){
        this.mongoManagement = mongoManagement;
    }

    public boolean createConversation(ObjectId ownerId, String name){
        MongoCollection<Document> conversations = mongoManagement.database.getCollection("conversations");
        ConversationData newConvo = new ConversationData();
        newConvo.setConversationName(name);
        newConvo.setType("group");

        try {
            conversations.insertOne(newConvo.toDocument());
        } catch (MongoWriteException e){
            return false;
        }
        return true;
    }

    public ArrayList<ConversationData> getUserAllConversation(ObjectId userId){
        MongoCollection<Document> conversations = mongoManagement.database.getCollection("conversation");
        ArrayList<Document> results = new ArrayList<>();
        conversations.find(Filters.in("membersId", userId)).into(results);
        ArrayList<ConversationData> data = new ArrayList<>();
        for (Document res : results){
            data.add(documentToConversation(res));
        }

        return data;
    }

    public ArrayList<ConversationData> getUserGroupConversation(ObjectId userId){
        MongoCollection<Document> conversations = mongoManagement.database.getCollection("conversation");
        ArrayList<Document> results = new ArrayList<>();
        conversations.find(Filters.and(Filters.in("membersId", userId), Filters.eq("type","group"))).into(results);
        ArrayList<ConversationData> data = new ArrayList<>();
        for (Document res : results){
            data.add(documentToConversation(res));
        }

        return data;
    }

    public ArrayList<ConversationData> getAllGroupConversation(){
        MongoCollection<Document> conversations = mongoManagement.database.getCollection("conversation");
        ArrayList<Document> results = new ArrayList<>();
        conversations.find(Filters.eq("type", "group")).into(results);

        ArrayList<ConversationData> data = new ArrayList<>();
        for (Document res : results){
            data.add(documentToConversation(res));
        }

        return data;
    }

    // get a conversation of the current user
    public ConversationData getConversation(ObjectId id){
        MongoCollection<Document> conversations = mongoManagement.database.getCollection("conversation");
        Document convo = conversations.find(Filters.eq("_id", id)).first();

        if (convo != null)
            return documentToConversation(convo);

        return null;
    }

    // remove conversation
    public boolean removeConversation(ObjectId id){
        MongoCollection<Document> conversations = mongoManagement.database.getCollection("conversation");
        MongoCollection<Document> messages = mongoManagement.database.getCollection("messages");

        try{
            conversations.deleteOne(Filters.eq("_id", id));
            messages.deleteMany(Filters.eq("conversationId", id));
        }catch (MongoWriteException e){
            return false;
        }

        return true;
    }

    // add person
    public boolean addMember(ObjectId con_id, ObjectId user_id){
        MongoCollection<Document> conversations = mongoManagement.database.getCollection("conversation");

        try{
            conversations.updateOne(Filters.eq("_id", con_id) , Updates.addToSet("membersId", user_id));
        }catch (MongoWriteException e){
            return false;
        }

        return true;
    }

    // remove person
    public boolean removeMember(String con_id, String user_id){
        MongoCollection<Document> conversations = mongoManagement.database.getCollection("conversation");

        try{
            conversations.updateOne(Filters.eq("_id", con_id) , Updates.pull("membersId", user_id));
        }catch (MongoWriteException e){
            return false;
        }

        return true;
    }

    // add person
    public boolean addModerator(ObjectId con_id, ObjectId user_id){
        MongoCollection<Document> conversations = mongoManagement.database.getCollection("conversation");

        try{
            conversations.updateOne(Filters.eq("_id", con_id) , Updates.addToSet("moderatorsId", user_id));
        }catch (MongoWriteException e){
            return false;
        }

        return true;
    }

    // remove person
    public boolean removeModerator(String con_id, String user_id){
        MongoCollection<Document> conversations = mongoManagement.database.getCollection("conversation");

        try{
            conversations.updateOne(Filters.eq("_id", con_id) , Updates.pull("moderatorsId", user_id));
        }catch (MongoWriteException e){
            return false;
        }

        return true;
    }


    public ConversationData documentToConversation(Document convoDoc){
        ConversationData convo = new ConversationData();

        convo.setConversationId(convoDoc.getObjectId("_id"));
        convo.setConversationName(convoDoc.getString("conversationName"));
        convo.setDateCreated(convoDoc.getDate("dateCreated"));
        convo.setType(convoDoc.getString("type"));
        convo.setMembersId((ArrayList<ObjectId>) convoDoc.get("membersId"));
        convo.setModeratorsId((ArrayList<ObjectId>) convoDoc.get("moderatorsId"));
        convo.setMembersName((ArrayList<SimpleStringProperty>) convoDoc.get("membersName"));
        convo.setModeratorsName((ArrayList<SimpleStringProperty>) convoDoc.get("moderatorsName"));
        convo.setLastMessageId(convoDoc.getObjectId("lastMessageId"));

        return convo;
    }
}
