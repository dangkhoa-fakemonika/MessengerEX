package org.example.mesexadmin;

import org.example.mesexadmin.data_access.GlobalQuery;
import org.example.mesexadmin.data_class.UserData;

public class SessionUser {
    public UserData currentUser;
    public GlobalQuery myQuery;

    public SessionUser(GlobalQuery globalQuery){
        myQuery = globalQuery;
        currentUser = new UserData();
    }

    public boolean loginSession(){
        currentUser = myQuery.users().getUserById();
        return true;
    }

    public boolean logoutSession(){

        currentUser = new UserData();
        return true;
    }

    public boolean registerUser(String username, String email, String password){
        return myQuery.users().insertUser(username, email, password);
    }

    public UserData getSessionUserData() {
        return this.currentUser;
    }
}
