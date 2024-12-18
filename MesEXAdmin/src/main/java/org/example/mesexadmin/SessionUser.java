package org.example.mesexadmin;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.Properties;

import javafx.beans.property.SimpleStringProperty;

import org.bson.types.ObjectId;
import org.example.mesexadmin.data_access.GlobalQuery;
import org.example.mesexadmin.data_class.*;

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
    private UserData currentUser;
    public GlobalQuery myQuery;

    public SessionUser(GlobalQuery globalQuery){
        myQuery = globalQuery;
        currentUser = new UserData();
    }

    public boolean isLoggedIn() {
        return currentUser.getStatus().equals("online");
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

        if (currentUser.getFriend().contains(receiverUserData.getUserId())) {
            new Alert(AlertType.INFORMATION, "This user is your friend!").showAndWait();
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

            //
            currentUser.getFriend().add(request.getSenderId());

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

    public ArrayList<UserData> getFriendList() {
        return myQuery.users().getUserList(currentUser.getFriend());
    }

    public ArrayList<UserData> getFriendListWithFilter(String field, String value) {
        return myQuery.users().getUserListWithFilter(currentUser.getFriend(), field, value);
    }

    public ArrayList<UserData>  getBlockedList() {
        return myQuery.users().getUserList(currentUser.getBlocked());
    }

    public ArrayList<UserData>  getBlockedListWithFilter(String field, String value) {
        return myQuery.users().getUserListWithFilter(currentUser.getBlocked(), field, value);
    }

    public ArrayList<UserData> getOnlineFriendList() {
        return myQuery.users().getOnlineUserList(currentUser.getFriend());
    }

    public ArrayList<UserData> getOnlineFriendListWithFilter(String field, String value) {
        return myQuery.users().getOnlineUserListWithFilter(currentUser.getFriend(), field, value);
    }

    public ArrayList<FriendRequestData> getReceivedRequests() {
        return myQuery.requests().getAllRequestsDetails(null, currentUser.getUserId());
    }

    public ArrayList<FriendRequestData> getSentRequests() {
        return myQuery.requests().getAllRequestsDetails(currentUser.getUserId() , null);
    }

    public boolean unfriendUser(ObjectId targetId) {
        currentUser.getFriend().remove(targetId);
        return myQuery.users().removeFriend(currentUser.getUserId(), targetId);
    }

    public boolean blockUser(ObjectId targetId) {
        currentUser.getFriend().remove(targetId);
        currentUser.getBlocked().add(targetId);
        return myQuery.users().addBlock(currentUser.getUserId(), targetId);
    }

    public boolean unblockUser(ObjectId targetId) {
        currentUser.getBlocked().remove(targetId);
        return myQuery.users().removeBlock(currentUser.getUserId(), targetId);
    }

    public boolean reportUser(ObjectId targetId){
        SpamTicketData newTicket = new SpamTicketData();
        newTicket.setReporterId(currentUser.getUserId());
        newTicket.setReportedId(targetId);
        newTicket.setTimeSent(new Date());
        return myQuery.spams().addSpamTicket(newTicket);
    }

    public ArrayList<ConversationData> loadAllConversations(){
        ArrayList<ConversationData> convData = myQuery.conversations().getUserAllConversation(currentUser.getUserId());
        convData.forEach((conv) -> {
            conv.getMembersId().forEach((id) ->
                    conv.getMembersName().add(new SimpleStringProperty(myQuery.users().getUserById(id).getUsername())));
            conv.getModeratorsId().forEach((id) ->
                    conv.getModeratorsName().add(new SimpleStringProperty(myQuery.users().getUserById(id).getUsername())));
        });
        return convData;
    }

    public ArrayList<ConversationData> loadPrivateConversations(){
        ArrayList<ConversationData> convData = myQuery.conversations().getUserPrivateConversation(currentUser.getUserId());
        convData.forEach((conv) -> {
            conv.getMembersId().forEach((id) ->
                    conv.getMembersName().add(new SimpleStringProperty(myQuery.users().getUserById(id).getUsername())));
            conv.getModeratorsId().forEach((id) ->
                    conv.getModeratorsName().add(new SimpleStringProperty(myQuery.users().getUserById(id).getUsername())));
        });
        return convData;
    }

    public ArrayList<ConversationData> loadGroupConversations(){
        ArrayList<ConversationData> convData = myQuery.conversations().getUserGroupConversation(currentUser.getUserId());
        convData.forEach((conv) -> {
            conv.getMembersId().forEach((id) ->
                    conv.getMembersName().add(new SimpleStringProperty(myQuery.users().getUserById(id).getUsername())));
            conv.getModeratorsId().forEach((id) ->
                    conv.getModeratorsName().add(new SimpleStringProperty(myQuery.users().getUserById(id).getUsername())));
        });
        return convData;
    }

    public ConversationData loadSingleGroup(ObjectId targetId){
        ConversationData conv = myQuery.conversations().getConversation(targetId);
        if (conv == null)
            return null;
        conv.getMembersId().forEach((id) ->
                conv.getMembersName().add(new SimpleStringProperty(myQuery.users().getUserById(id).getUsername())));
        conv.getModeratorsId().forEach((id) ->
                conv.getModeratorsName().add(new SimpleStringProperty(myQuery.users().getUserById(id).getUsername())));
        return conv;
    }

    public int GetIndirectFriendsCount(ObjectId targetId){
        return myQuery.users().getFriendsOfFriends(targetId);
    }

    public boolean createPrivateConversation(String username){
        UserData findUser = myQuery.users().getUserByUsername(username);
        if (findUser == null || findUser.getUserId() == currentUser.getUserId()){
            return false;
        }

        ConversationData getConvo = myQuery.conversations().findExistingPrivateConversation(findUser.getUserId(), currentUser.getUserId());
        if (getConvo != null) {
            return false;
        }

        ConversationData newConvo = new ConversationData();
        newConvo.setType("private");
        newConvo.getMembersId().add(currentUser.getUserId());
        newConvo.setDateCreated(new Date());
        newConvo.getMembersId().add(findUser.getUserId());
        newConvo.setConversationName("private conversation");
        return myQuery.conversations().createConversation(newConvo);
    }

    public ArrayList<UserData> loadUserFriendsStatus(){
        return myQuery.users().getAllUsers();
    }

    public ArrayList<UserData> loadUserFriendsStatusFilter(String filterRange, Integer value, String key, String token){
        ArrayList<UserData> friendsData;
        if (key != null && token != null)
             friendsData = myQuery.users().getAllUsersFilter(key, token);
        else
            friendsData = myQuery.users().getAllUsers();

        ArrayList<UserData> returnData = new ArrayList<>();

        friendsData.forEach((d) -> {
            if ((Objects.equals(filterRange, "Equal to") && d.getFriend().size() == value) ||
                    (Objects.equals(filterRange, "Greater than") && d.getFriend().size() > value) ||
                    (Objects.equals(filterRange, "Lesser than") && d.getFriend().size() < value) ||
                    Objects.equals(filterRange, "None"))
                returnData.add(d);
        });

        return returnData;
    }

    public boolean createGroup(String groupName, String secondUser){
        UserData ndUser = myQuery.users().getUserByUsername(secondUser);
        if (ndUser == null)
            return false;

        ConversationData newConv = new ConversationData();
        newConv.setType("group");
        newConv.setConversationName(groupName);
        newConv.getMembersId().add(currentUser.getUserId());
        newConv.getMembersId().add(ndUser.getUserId());
        newConv.getModeratorsId().add(currentUser.getUserId());
        newConv.setDateCreated(new Date());

        return myQuery.conversations().createConversation(newConv);
    }

    public UserData getChatTarget(ConversationData currentConversation) {
        UserData result = null;
        for (ObjectId id : currentConversation.getMembersId()) {
            if (!currentUser.getUserId().equals(id)) {
                result = myQuery.users().getUserById(id);
            }
        }
        return result;
    }

    public boolean getBLockedStatus(ObjectId blocker) {
        return myQuery.users().getUserById(blocker).getBlocked().contains(currentUser.getUserId());
    }

    public boolean sendMessage(String content, ObjectId conversationId){
        MessageData messageData = new MessageData();
        messageData.setSenderId(currentUser.getUserId());
        messageData.setSenderName(currentUser.getUsername());
        messageData.setContent(content);
        messageData.setConversationId(conversationId);

        return myQuery.messages().postMessage(messageData);
    }

    public boolean resetUserPassword(UserData user){
        String newPassword = generatePassword();
        if (sendResetPasswordEmail(user.getEmail(), newPassword)){
            String hashedPassword = hashPassword(newPassword);
            return myQuery.users().changeUserPassword(user.getUserId(), hashedPassword);
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

    public static String hashPassword(String password) {
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