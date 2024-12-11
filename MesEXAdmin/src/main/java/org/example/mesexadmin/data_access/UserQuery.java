package org.example.mesexadmin.data_access;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.example.mesexadmin.MongoManagement;
import org.example.mesexadmin.data_class.UserData;

import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.UpdateResult;

import java.util.ArrayList;

public class UserQuery {
    MongoManagement mongoManagement;

    UserQuery(MongoManagement mongoManagement) {
        this.mongoManagement = mongoManagement;
    }

    public UserData getUserById(ObjectId userId){
        MongoCollection<Document> users = mongoManagement.database.getCollection("users");
        Document user = users.find(Filters.eq("_id", userId)).first();
        
        if (user != null)
            return documentToUser(user);

        return null;
    }

    public UserData getUserByUsername(String username) {
        MongoCollection<Document> users = mongoManagement.database.getCollection("users");
        Document user = users.find(Filters.eq("username", username)).first();

        if (user != null)
            return documentToUser(user);

        return null;
    }

    public UserData getUserByEmail(String email) {
        MongoCollection<Document> users = mongoManagement.database.getCollection("users");
        Document user = users.find(Filters.eq("email", email)).first();

        if (user != null)
            return documentToUser(user);

        return null;
    }

    public boolean updateUser(UserData user){
        MongoCollection<Document> users = mongoManagement.database.getCollection("users");
        Document userDocument = user.toDocument();
        Document filter = new Document("_id", user.getUserId());
        
        try {
            UpdateResult result = users.updateOne(filter, new Document("$set", userDocument));
            return result.getModifiedCount() > 0;
        } catch (MongoWriteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean insertUser(UserData user){
        MongoCollection<Document> users = mongoManagement.database.getCollection("users");

        try {
            users.insertOne(user.toDocument());
        } catch (MongoWriteException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public ArrayList<UserData> findUser(){
        return null;
    }

    // add friend
    public boolean addFriend(String id1, String id2){
        return false;
    }

    // remove friend
    public boolean removeFriend(String id1, String id2){
        return false;
    }

    // get friend list
    public ArrayList<UserData> getFriends(String id){
        return null;
    }

    private UserData documentToUser(Document userDocument) {
        UserData user = new UserData();

        user.setUserId(userDocument.getObjectId("_id"));
        user.setUsername(userDocument.getString("username"));
        user.setDisplayName(userDocument.getString("displayName"));
        user.setEmail(userDocument.getString("email"));
        user.setStatus(userDocument.getString("status"));
        user.setLastLogin(userDocument.getDate("lastLogin"));
        user.setDateCreated(userDocument.getDate("dateCreated"));
        user.setRole(userDocument.getString("role"));
        user.setFriend((ArrayList<ObjectId>) userDocument.get("friend"));
        user.setBlocked((ArrayList<ObjectId>) userDocument.get("blocked"));
        user.setAddress(userDocument.getString("address"));
        user.setDateOfBirth(userDocument.getDate("dateOfBirth"));
        user.setGender(userDocument.getString("gender"));
        user.setPasswordHashed(userDocument.getString("passwordHash"));

        return user; 
    }
}
