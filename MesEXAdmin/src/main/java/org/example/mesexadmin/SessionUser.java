package org.example.mesexadmin;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import org.example.mesexadmin.data_access.GlobalQuery;
import org.example.mesexadmin.data_class.ActivityData;
import org.example.mesexadmin.data_class.FriendRequestData;
import org.example.mesexadmin.data_class.UserData;
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
        
        UserData userData = myQuery.users().getUserByUsername(username);
        
        if (userData == null) {
            new Alert(AlertType.ERROR, "Username does not exist!").showAndWait();
            return false;
        }
        
        if (!userData.getPasswordHashed().equals(hashPassword(password))) {
            new Alert(AlertType.ERROR, "Incorrect password!").showAndWait();
            return false;
        }
        
        userData.setStatus("online");
        userData.setLastLogin(new Date());

        if (!myQuery.users().updateUser(userData)) {
            return false;
        }

        ActivityData activityData = new ActivityData();
        activityData.setUserId(userData.getUserId());
        activityData.setLoginDate(userData.getLastLogin());
        activityData.setUsername(userData.getUsername());
        myQuery.activities().insertActivity(activityData);

        currentUser = userData;

        return true;
    }

    public boolean logoutSession(){
        currentUser.setStatus("offline");

        if (!myQuery.users().updateUser(currentUser)) {
            return false;
        }

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

    public boolean sendFriendRequest(String receiverUsername) {
        UserData receiverUserData = myQuery.users().getUserByUsername(receiverUsername);

        if (receiverUserData == null) {
            new Alert(AlertType.ERROR, "User does not exist!").showAndWait();
            return false;
        }

        FriendRequestData request = myQuery.requests().getSingleRequest(currentUser.getUserId(), receiverUserData.getUserId());
        if (request != null) {
            new Alert(AlertType.ERROR, "You have already sent request to this user!").showAndWait();
            return false;
        }

        request = myQuery.requests().getSingleRequest(receiverUserData.getUserId(), currentUser.getUserId());
        if (request != null) {
            new Alert(AlertType.ERROR, "This user have already sent request to you!").showAndWait();
            return false;
        }

        request = new FriendRequestData();
        request.setTimeSent(new Date());
        request.setSenderId(currentUser.getUserId());
        request.setReceiverId(receiverUserData.getUserId());

        return myQuery.requests().insertFriendRequest(request);
    }

    public boolean changePassword(String oldPassword, String newPassword, String confirmPassword) {
        UserData userData = myQuery.users().getUserById(currentUser.getUserId());

        if (!hashPassword(oldPassword).equals(userData.getPasswordHashed())) {
            new Alert(AlertType.ERROR, "Old password incorrect!").showAndWait();
            return false;
        }

        if (hashPassword(newPassword).equals(hashPassword(oldPassword))) {
            new Alert(AlertType.ERROR, "New password must be different!").showAndWait();
            return false;
        }

        if (!hashPassword(confirmPassword).equals(hashPassword(newPassword))) {
            new Alert(AlertType.ERROR, "Confirm password does not match!").showAndWait();
            return false;
        }

        userData.setPasswordHashed(hashPassword(newPassword));

        if (!myQuery.users().updateUser(userData)) {
            return false;
        }

        currentUser = userData;

        return true;
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
