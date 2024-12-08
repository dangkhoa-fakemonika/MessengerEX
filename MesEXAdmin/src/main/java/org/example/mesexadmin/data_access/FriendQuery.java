package org.example.mesexadmin.data_access;

import org.example.mesexadmin.data_class.ConversationData;
import org.example.mesexadmin.data_class.UserData;

import java.util.ArrayList;

public class FriendQuery {
    // create friend request
    public boolean sendRequest(String id){
        return false;
    }

    // unsend friend request
    public boolean unsendRequest(String id){
        return false;
    }

    // add friend
    public boolean addFriend(String id1, String id2){
        return false;
    }

    // remove friend
    public boolean removeFriend(String id1, String id2){
        return false;
    }

    // get or create conversation
    public ConversationData getConversation(String id){
        return null;
    }

    // get friend list
    public ArrayList<UserData> getFriends(String id){
        return null;
    }
}
