package org.example.mesexadmin.data_access;

import org.example.mesexadmin.data_class.ConversationData;

public class ConversationQuery {
    // get a conversation of the current user
    public ConversationData getConversation(String id){
        return null;
    }

    // add message to conversation
    public boolean addMessageCon(String id, String message){
        return true;
    }

    // remove conversation
    public boolean removeConversation(String id){
        return true;
    }

    // add person
    public boolean addPersonCon(String con_id, String user_id){
        return true;
    }

    // remove person
    public boolean removePersonCon(String con_id, String user_id){
        return true;
    }

    // delete conversation
    public boolean removeCon(String con_id){
        return true;
    }

    // create conversation
    public boolean createCon(String host_id){
        return true;
    }

    // update information
    public boolean updateInformation(ConversationData conversationData){
        return true;
    }
}
