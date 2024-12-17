package org.example.mesexadmin.data_access;

import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import javafx.beans.property.SimpleStringProperty;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.example.mesexadmin.MongoManagement;
import org.example.mesexadmin.data_class.ConversationData;

import java.util.ArrayList;
import java.util.Arrays;

public class ConversationQuery {
    MongoManagement mongoManagement;

    public ConversationQuery(MongoManagement mongoManagement){
        this.mongoManagement = mongoManagement;
    }

    public boolean createConversation(ConversationData newConvo){
        MongoCollection<Document> conversations = mongoManagement.database.getCollection("conversations");

        try {
            conversations.insertOne(newConvo.toDocument());
        } catch (MongoWriteException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public ArrayList<ConversationData> getUserAllConversation(ObjectId userId){
        MongoCollection<Document> conversations = mongoManagement.database.getCollection("conversations");
        ArrayList<Document> results = new ArrayList<>();

        conversations.find(Filters.in("membersId", userId)).into(results);

        ArrayList<ConversationData> data = new ArrayList<>();
        for (Document res : results){
            ConversationData thisData = documentToConversation(res);
            data.add(thisData);
        }

        return data;
    }

    public ArrayList<ConversationData> getUserGroupConversation(ObjectId userId){
        MongoCollection<Document> conversations = mongoManagement.database.getCollection("conversations");
        ArrayList<Document> results = new ArrayList<>();
        conversations.find(Filters.and(Filters.in("membersId", userId), Filters.eq("type","group"))).into(results);
        ArrayList<ConversationData> data = new ArrayList<>();
        for (Document res : results){
            data.add(documentToConversation(res));
        }

        return data;
    }

    public ArrayList<ConversationData> getUserPrivateConversation(ObjectId userId){
        MongoCollection<Document> conversations = mongoManagement.database.getCollection("conversations");
        ArrayList<Document> results = new ArrayList<>();
        conversations.find(Filters.and(Filters.in("membersId", userId), Filters.eq("type","private"))).into(results);
        ArrayList<ConversationData> data = new ArrayList<>();
        for (Document res : results){
            data.add(documentToConversation(res));
        }

        return data;
    }


    public ArrayList<ConversationData> getAllGroupConversation(){
        MongoCollection<Document> conversations = mongoManagement.database.getCollection("conversations");
        ArrayList<Document> results = new ArrayList<>();
        conversations.find(Filters.eq("type", "group")).into(results);

        ArrayList<ConversationData> data = new ArrayList<>();
        for (Document res : results){
            data.add(documentToConversation(res));
        }

        return data;
    }

    public ArrayList<ConversationData> getAllGroupConversationWithFilter(String token){
        MongoCollection<Document> conversations = mongoManagement.database.getCollection("conversations");
        ArrayList<Document> results = new ArrayList<>();
        conversations.find(Filters.and(Filters.eq("type", "group"), Filters.regex("conversationName", token, "i"))).into(results);

        ArrayList<ConversationData> data = new ArrayList<>();
        for (Document res : results){
            data.add(documentToConversation(res));
        }

        return data;
    }


    public ConversationData findExistingPrivateConversation(ObjectId id1, ObjectId id2){
        MongoCollection<Document> conversations = mongoManagement.database.getCollection("conversations");
        // Document result = conversations.find(Filters.and(Filters.in("membersId", id1, id2))).first();

        Document query = new Document("type", "private").append("membersId", new Document("$all", Arrays.asList(id1, id2)));
        Document result = conversations.find(query).first();

        if (result != null)
            return documentToConversation(result);
        else return null;
    }

    // get a conversation of the current user
    public ConversationData getConversation(ObjectId id){
        MongoCollection<Document> conversations = mongoManagement.database.getCollection("conversations");
        Document convo = conversations.find(Filters.eq("_id", id)).first();

        if (convo != null)
            return documentToConversation(convo);

        return null;
    }

    // remove conversation
    public boolean removeConversation(ObjectId id){
        MongoCollection<Document> conversations = mongoManagement.database.getCollection("conversations");
        MongoCollection<Document> messages = mongoManagement.database.getCollection("messages");

        try{
            conversations.deleteOne(Filters.eq("_id", id));
            messages.deleteMany(Filters.eq("conversationId", id));
        }catch (MongoWriteException e){
            return false;
        }

        return true;
    }

    public boolean deletePrivateConversation(Object person1, Object person2){
        MongoCollection<Document> conversations = mongoManagement.database.getCollection("conversations");

        try{
            conversations.deleteOne(Filters.and(Filters.in("membersId", person1), Filters.in("membersId", person2), Filters.eq("type", "private")));
        } catch (MongoWriteException e){
            return false;
        }

        return true;
    }

    // add person
    public boolean addMember(ObjectId con_id, ObjectId user_id){
        MongoCollection<Document> conversations = mongoManagement.database.getCollection("conversations");

        try{
            conversations.updateOne(Filters.eq("_id", con_id) , Updates.addToSet("membersId", user_id));
        }catch (MongoWriteException e){
            return false;
        }

        return true;
    }

    // remove person
    public boolean removeMember(ObjectId con_id, ObjectId user_id){
        MongoCollection<Document> conversations = mongoManagement.database.getCollection("conversations");

        try{
            conversations.updateOne(Filters.eq("_id", con_id) , Updates.pull("membersId", user_id));
            conversations.updateOne(Filters.eq("_id", con_id) , Updates.pull("moderatorsId", user_id));



        }catch (MongoWriteException e){
            return false;
        }

        Document res = conversations.find(Filters.and(Filters.eq("_id", con_id), Filters.size("membersId", 0))).first();
        if (res != null)
            return removeConversation(con_id);

        return true;
    }

    public boolean changeConversationName(ObjectId con_id, String newName){
        MongoCollection<Document> conversations = mongoManagement.database.getCollection("conversations");

        try{
            conversations.updateOne(Filters.eq("_id", con_id) , Updates.set("conversationName", newName));
        }catch (MongoWriteException e){
            return false;
        }

        return true;
    }

    // add person
    public boolean addModerator(ObjectId con_id, ObjectId user_id){
        MongoCollection<Document> conversations = mongoManagement.database.getCollection("conversations");

        try{
            conversations.updateOne(Filters.eq("_id", con_id) , Updates.addToSet("moderatorsId", user_id));
        }catch (MongoWriteException e){
            return false;
        }

        return true;
    }

    // remove person
    public boolean removeModerator(String con_id, String user_id){
        MongoCollection<Document> conversations = mongoManagement.database.getCollection("conversations");

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
        convo.setMembersId(new ArrayList<>(convoDoc.getList("membersId", ObjectId.class)));
        convo.setModeratorsId(new ArrayList<>(convoDoc.getList("moderatorsId", ObjectId.class)));
        convo.setLastMessageId(convoDoc.getObjectId("lastMessageId"));


        return convo;
    }
}
