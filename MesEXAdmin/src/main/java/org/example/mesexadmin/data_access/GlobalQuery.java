package org.example.mesexadmin.data_access;

import org.example.mesexadmin.MongoManagement;

public class GlobalQuery {
    MongoManagement myMongo;

    public GlobalQuery(MongoManagement mongoManagement){
        myMongo = mongoManagement;
    }

    public UserQuery users(){
        return null;
    }

    public MessageQuery messages(){
        return null;
    }

    public FriendQuery friends(){
        return null;
    }

    public ConversationQuery conversations(){
        return null;
    }

    public SpamTicketQuery spams(){
        return null;
    }

    public ActivityQuery activities(){
        return null;
    }
}
