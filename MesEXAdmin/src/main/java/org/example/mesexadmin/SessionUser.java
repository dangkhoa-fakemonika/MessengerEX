package org.example.mesexadmin;

import org.example.mesexadmin.data_access.GlobalQuery;
import org.example.mesexadmin.data_class.UserData;

public class SessionUser {
    public UserData currentUser;
    public GlobalQuery myQuery;

    public SessionUser(GlobalQuery globalQuery){
        myQuery = globalQuery;
    }

    public boolean loginSession(){
        currentUser = myQuery.users().login();
        return true;
    }

    public boolean logoutSession(){
        currentUser = null;
        return true;
    }

    public boolean registerUser(){
        currentUser = myQuery.users().register();
        return true;
    }
}
