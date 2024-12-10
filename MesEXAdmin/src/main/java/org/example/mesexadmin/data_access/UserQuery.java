package org.example.mesexadmin.data_access;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.example.mesexadmin.MongoManagement;
import org.example.mesexadmin.data_class.ConversationData;
import org.example.mesexadmin.data_class.UserData;

import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;

public class UserQuery {
    MongoManagement mongoManagement;

    UserQuery(MongoManagement mongoManagement) {
        this.mongoManagement = mongoManagement;
    }

    // credentials
    public UserData getUserById(){
        return null;
    }

    public boolean updateUser(UserData userData){
        return true;
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
        user.setDateOfBirth(userDocument.getString("dateOfBirth"));
        user.setGender(userDocument.getString("gender"));
        user.setPasswordHashed(userDocument.getString("passwordHashed"));

        return user; 
    }
}
