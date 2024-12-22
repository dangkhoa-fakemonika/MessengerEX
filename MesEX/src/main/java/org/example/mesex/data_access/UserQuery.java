package org.example.mesex.data_access;

import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.*;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.example.mesex.MongoManagement;
import org.example.mesex.data_class.UserData;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

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

    public boolean changeUserPassword(ObjectId id, String newHashedPassword){
        MongoCollection<Document> users = mongoManagement.database.getCollection("users");

        try {
            users.updateOne(Filters.eq("_id", id), Updates.set("passwordHash", newHashedPassword));
        } catch (MongoWriteException e) {
            e.printStackTrace();
            return false;
        }

        return true;
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
    public boolean addFriend(ObjectId userId1, ObjectId userId2){
        MongoCollection<Document> users = mongoManagement.database.getCollection("users");

        try {
            users.updateOne(Filters.eq("_id", userId1), Updates.addToSet("friend", userId2));
            users.updateOne(Filters.eq("_id", userId2), Updates.addToSet("friend", userId1));
        } catch (MongoWriteException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    // remove friend
    public boolean removeFriend(ObjectId id1, ObjectId id2){
        MongoCollection<Document> users = mongoManagement.database.getCollection("users");

        try {
            users.updateOne(Filters.eq("_id", id1), Updates.pull("friend", id2));
            users.updateOne(Filters.eq("_id", id2), Updates.pull("friend", id1));
        } catch (MongoWriteException e) {
            return false;
        }

        return true;
    }

    public boolean addBlock(ObjectId blocker, ObjectId blocked){
        MongoCollection<Document> users = mongoManagement.database.getCollection("users");

        try {
            users.updateOne(Filters.eq("_id", blocker), Updates.addToSet("blocked", blocked));
        } catch (MongoWriteException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean removeBlock(ObjectId blocker, ObjectId blocked){
        MongoCollection<Document> users = mongoManagement.database.getCollection("users");

        try {
            users.updateOne(Filters.eq("_id", blocker), Updates.pull("blocked", blocked));
        } catch (MongoWriteException e) {
            return false;
        }

        return true;
    }

    public ArrayList<UserData> getAllUsers(){
        MongoCollection<Document> users = mongoManagement.database.getCollection("users");
        ArrayList<Document> results = new ArrayList<>();
        users.find().into(results);
        ArrayList<UserData> allUserData = new ArrayList<>();
        for (Document res : results){
            allUserData.add(documentToUser(res));
        }
        return allUserData;
    }

    public ArrayList<UserData> getAllUsersFilter(String key, String token){
        MongoCollection<Document> users = mongoManagement.database.getCollection("users");
        ArrayList<Document> results = new ArrayList<>();
        users.find(Filters.regex(key, token, "i")).into(results);
        ArrayList<UserData> allUserData = new ArrayList<>();
        for (Document res : results){
            allUserData.add(documentToUser(res));
        }
        return allUserData;
    }

    public ArrayList<UserData> getAllUsersNameFilter(String token, ObjectId currentId){
        MongoCollection<Document> users = mongoManagement.database.getCollection("users");
        ArrayList<Document> results = new ArrayList<>();
        users.find(Filters.and(
                Filters.or(Filters.regex("username", token, "i"), Filters.regex("displayName", token, "i")),
                Filters.not(Filters.in("blocked", currentId))
            )).into(results);
        ArrayList<UserData> allUserData = new ArrayList<>();
        for (Document res : results){
            allUserData.add(documentToUser(res));
        }
        return allUserData;
    }

    // get friend/ban list
    public ArrayList<UserData> getUserList(ArrayList<ObjectId> idList){
        MongoCollection<Document> users = mongoManagement.database.getCollection("users");
        ArrayList<UserData> data = new ArrayList<>();

        Document query = new Document("_id", new Document("$in", idList));

        ArrayList<Document> result = users.find(query).into(new ArrayList<>());

        for (Document doc : result) {
            data.add(documentToUser(doc));
        }

        return data;
    }

    public ArrayList<UserData> getUserListWithFilter(ArrayList<ObjectId> idList, String field, String value){
        MongoCollection<Document> users = mongoManagement.database.getCollection("users");
        ArrayList<UserData> data = new ArrayList<>();

        Document query = new Document("_id", new Document("$in", idList)).append(field, new Document("$regex", value).append("$options", "i"));

        ArrayList<Document> result = users.find(query).into(new ArrayList<>());

        for (Document doc : result) {
            data.add(documentToUser(doc));
        }

        return data;
    }

    public ArrayList<UserData> getOnlineUserList(ArrayList<ObjectId> idList) {
        MongoCollection<Document> users = mongoManagement.database.getCollection("users");
        ArrayList<UserData> data = new ArrayList<>();

        Document query = new Document("_id", new Document("$in", idList)).append("status", "online");

        ArrayList<Document> result = users.find(query).into(new ArrayList<>());

        for (Document doc : result) {
            data.add(documentToUser(doc));
        }

        return data;
    }

    public ArrayList<UserData> getOnlineUserListWithFilter(ArrayList<ObjectId> idList, String field, String value) {
        MongoCollection<Document> users = mongoManagement.database.getCollection("users");
        ArrayList<UserData> data = new ArrayList<>();

        Document query = new Document("_id", new Document("$in", idList)).append("status", "online").append(field, new Document("$regex", value).append("$options", "i"));

        ArrayList<Document> result = users.find(query).into(new ArrayList<>());

        for (Document doc : result) {
            data.add(documentToUser(doc));
        }

        return data;
    }

    public int getFriendsOfFriends(ObjectId targetId){
        MongoCollection<Document> users = mongoManagement.database.getCollection("users");
//        ArrayList<Document> results = new ArrayList<>();

        Document result = users.aggregate(Arrays.asList(
                Aggregates.match(Filters.eq("_id", targetId)),
                Aggregates.lookup("users", "friend", "_id", "FriendsList"),
                Aggregates.unwind("$FriendsList"),
                Aggregates.lookup("users", "FriendsList.friend", "_id", "SecondFriendsList"),
                Aggregates.unwind("$SecondFriendsList"),
                Aggregates.match(Filters.ne("SecondFriendsList._id",targetId)),
//                Aggregates.match(Filters.ne("SecondFriendsList._id", "_id")),
//                BasicDBObject.parse("{$match: {$expr: {$ne : [\"$_id\", \"$SecondFriendsList._id\"]}}},"),
                Aggregates.group("$_id", Accumulators.sum("count", 1))
        )).first();


        try{
            if (result == null)
                return 0;
            return result.getInteger("count");
        } catch (Exception e){
            return 0;
        }
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
        user.setFriend(new ArrayList<>(userDocument.getList("friend", ObjectId.class)));
        user.setBlocked(new ArrayList<>(userDocument.getList("blocked", ObjectId.class)));
        user.setAddress(userDocument.getString("address"));
        user.setDateOfBirth(userDocument.getDate("dateOfBirth"));
        user.setGender(userDocument.getString("gender"));
        user.setPasswordHashed(userDocument.getString("passwordHash"));

        return user;
    }
}
