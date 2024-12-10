package org.example.mesexadmin.data_access;

import org.example.mesexadmin.data_class.ConversationData;
import org.example.mesexadmin.data_class.UserData;

import java.util.ArrayList;

public class UserQuery {
    

    // credentials
    public UserData getUserById(){
        return null;
    }

    public boolean updateUser(UserData userData){
        return true;
    }

    public boolean insertUser(UserData userData){

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
}
