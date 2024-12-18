package org.example.mesexadmin.data_access;

import org.example.mesexadmin.MongoManagement;

public class GlobalQuery {
    MongoManagement myMongo;

    public GlobalQuery(MongoManagement mongoManagement){
        myMongo = mongoManagement;
    }

    public UserQuery users(){
        return new UserQuery(myMongo);
    }

    public MessageQuery messages(){
        return new MessageQuery(myMongo);
    }

    public FriendRequestQuery requests(){
        return new FriendRequestQuery(myMongo);
    }

    public ConversationQuery conversations(){
        return new ConversationQuery(myMongo);
    }

    public SpamTicketQuery spams(){
        return new SpamTicketQuery(myMongo);
    }

    public ActivityQuery activities(){
        return new ActivityQuery(myMongo);
    }

    public MongoManagement getConnection() {
        return myMongo;
    }
}
