package org.example.mesexadmin.data_access;

import org.bson.Document;
import org.example.mesexadmin.MongoManagement;
import org.example.mesexadmin.data_class.ConversationData;
import org.example.mesexadmin.data_class.UserData;

import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

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

    public boolean insertUser(String username, String email, String password){
        MongoCollection<Document> users = mongoManagement.database.getCollection("users");

        if (users.find(Filters.eq("email", email)).first() != null) {
            new Alert(AlertType.ERROR, "This email has registered!").showAndWait();
            return false;
        }
        
        if (users.find(Filters.eq("username", username)).first() != null) {
            new Alert(AlertType.ERROR, "Username already exist!").showAndWait();
            return false;
        }

        UserData user = new UserData();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHashed(hashPassword(password));
        user.setDateCreated(new Date());

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

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
