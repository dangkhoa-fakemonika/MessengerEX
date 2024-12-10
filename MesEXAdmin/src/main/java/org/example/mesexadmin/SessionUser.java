package org.example.mesexadmin;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import org.bson.Document;
import org.example.mesexadmin.data_access.GlobalQuery;
import org.example.mesexadmin.data_class.UserData;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class SessionUser {
    public UserData currentUser;
    public GlobalQuery myQuery;

    public SessionUser(GlobalQuery globalQuery){
        myQuery = globalQuery;
        currentUser = new UserData();
    }

    public boolean loginSession(String username, String password){
        
        UserData user = myQuery.users().getUserByUsername(username);
        
        if (user == null) {
            new Alert(AlertType.ERROR, "Username does not exist!").showAndWait();
            return false;
        }
        
        if (!user.getPasswordHashed().equals(hashPassword(password))) {
            new Alert(AlertType.ERROR, "Incorrect password!").showAndWait();
            return false;
        }
        
        user.setStatus("online");
        user.setLastLogin(new Date());

        if (!myQuery.users().updateUser(user)) {
            return false;
        }

        currentUser = user;

        return true;
    }

    public boolean logoutSession(){

        currentUser = new UserData();
        return true;
    }

    public boolean registerUser(String username, String email, String password){

        if (myQuery.users().getUserByEmail(email) != null) {
            new Alert(AlertType.ERROR, "This email has registered!").showAndWait();
            return false;
        }
        
        if (myQuery.users().getUserByUsername(username) != null) {
            new Alert(AlertType.ERROR, "Username already exist!").showAndWait();
            return false;
        }

        UserData user = new UserData();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHashed(hashPassword(password));
        user.setDateCreated(new Date());

        return myQuery.users().insertUser(user);
    }

    public UserData getSessionUserData() {
        return this.currentUser;
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
