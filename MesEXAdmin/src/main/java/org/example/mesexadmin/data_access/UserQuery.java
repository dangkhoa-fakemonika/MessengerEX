package org.example.mesexadmin.data_access;

import com.mongodb.client.model.Updates;
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

        if (user != null){
//            UserData createdUser = documentToUser(user);
            return documentToUser(user);
        }

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

    // add friend
    public boolean addFriend(ObjectId id1, ObjectId id2){
        MongoCollection<Document> users = mongoManagement.database.getCollection("users");
        MongoCollection<Document> requests = mongoManagement.database.getCollection("request");

        try {
            users.updateOne(Filters.eq("_id", id1), Updates.addToSet("friends", id2));
            users.updateOne(Filters.eq("_id", id2), Updates.addToSet("friends", id1));
        } catch (MongoWriteException e) {
            return false;
        }

        return true;
    }

    // remove friend
    public boolean removeFriend(ObjectId id1, ObjectId id2){
        MongoCollection<Document> users = mongoManagement.database.getCollection("users");

        try {
            users.updateOne(Filters.eq("_id", id1), Updates.pull("friends", id2));
            users.updateOne(Filters.eq("_id", id2), Updates.pull("friends", id1));
        } catch (MongoWriteException e) {
            return false;
        }

        return true;
    }

    public boolean removeUserFromSystem(ObjectId id){
        MongoCollection<Document> users = mongoManagement.database.getCollection("users");
        MongoCollection<Document> conversations = mongoManagement.database.getCollection("conversations");
        MongoCollection<Document> activities = mongoManagement.database.getCollection("activities");
        MongoCollection<Document> requests = mongoManagement.database.getCollection("requests");


        try {
            users.deleteOne(Filters.eq("_id", id));
//            conversations.deleteMany(Filters.eq("_id", id));
//            activities.deleteMany(Filters.eq("_id", id));
//            requests.deleteMany(Filters.eq("_id", id));


        } catch (MongoWriteException e) {
            return false;
        }

        return true;
    }

    public boolean changeUserStatus(ObjectId id, String newStatus){
        MongoCollection<Document> users = mongoManagement.database.getCollection("users");

        try {
            users.updateOne(Filters.eq("_id", id), Updates.set("status", newStatus));
        } catch (MongoWriteException e){
            return false;
        }

        return true;
    }

    // get friend/ban list
    public ArrayList<UserData> getUserLists(ArrayList<ObjectId> idList){
        ArrayList<UserData> data = new ArrayList<>();

        for (ObjectId uid : idList){
            data.add(getUserById(uid));
        }

        return data;
    }


//    public ArrayList

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
        ArrayList<ObjectId> fr = new ArrayList<>(userDocument.getList("friend", ObjectId.class));
        fr.forEach(System.out::println);
        user.setFriend(new ArrayList<>(userDocument.getList("friend", ObjectId.class)));
        System.out.println(user.getFriend().size());
        System.out.println(user.getDummyValue().size());

        user.setBlocked(new ArrayList<>(userDocument.getList("blocked", ObjectId.class)));
        user.setAddress(userDocument.getString("address"));
        user.setDateOfBirth(userDocument.getDate("dateOfBirth"));
        user.setGender(userDocument.getString("gender"));
        user.setPasswordHashed(userDocument.getString("passwordHash"));

        return user;
    }
}
