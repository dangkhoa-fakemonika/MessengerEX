package org.example.mesexadmin;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Properties;

import org.example.mesexadmin.data_access.GlobalQuery;
import org.example.mesexadmin.data_class.ActivityData;
import org.example.mesexadmin.data_class.FriendRequestData;
import org.example.mesexadmin.data_class.UserData;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
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

        if (currentUser.getUserId().equals(receiverUserData.getUserId())) {
            new Alert(AlertType.INFORMATION, "You can not send request to yourself!").showAndWait();
            return false;
        }

        FriendRequestData request = myQuery.requests().getSingleRequest(currentUser.getUserId(), receiverUserData.getUserId());
        if (request != null) {
            new Alert(AlertType.INFORMATION, "You have already sent request to this user!").showAndWait();
            return false;
        }

        request = myQuery.requests().getSingleRequest(receiverUserData.getUserId(), currentUser.getUserId());
        if (request != null) {
            new Alert(AlertType.INFORMATION, "This user have already sent request to you!").showAndWait();
            return false;
        }

        request = new FriendRequestData();
        request.setTimeSent(new Date());
        request.setSenderId(currentUser.getUserId());
        request.setReceiverId(receiverUserData.getUserId());

        return myQuery.requests().insertFriendRequest(request);
    }

    public boolean acceptFriendRequest(FriendRequestData request) {
        
        if (myQuery.users().addFriend(request.getSenderId(), request.getReceiverId())) {
            return myQuery.requests().removeRequest(request.getRequestId());
        }

        return false;
    }

    public boolean removeFriendRequest(FriendRequestData request) {
        return myQuery.requests().removeRequest(request.getRequestId());
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

    public boolean resetPassword(String email) {
        UserData userData = myQuery.users().getUserByEmail(email);

        if (userData == null) {
            new Alert(AlertType.ERROR, "Account with this email does not exist!").showAndWait();
            return false;
        }

        String newPassword = generatePassword();

        if (sendResetPasswordEmail(email, newPassword)) {
            userData.setPasswordHashed(hashPassword(newPassword));
            return myQuery.users().updateUser(userData);
        }

        return false;
    }
                
    private static boolean sendResetPasswordEmail(String emailTo, String newPassword) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        final String emailFrom = null; // Email goes here
        final String appPassword = null; // App password goes 

        String subject = "Reset password for account with email: " + emailTo;
        String body = newPassword;

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailFrom, appPassword);
            }
        });

        try {
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(emailFrom));
            msg.setReplyTo(InternetAddress.parse(emailFrom, false));
            msg.setSubject(subject, "UTF-8");
            msg.setText(body, "UTF-8");
            msg.setSentDate(new Date());
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailTo, false));
            Transport.send(msg);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static String generatePassword() {
        final String charSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();
        
        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(charSet.length());
            password.append(charSet.charAt(index));
        }

        return password.toString();
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