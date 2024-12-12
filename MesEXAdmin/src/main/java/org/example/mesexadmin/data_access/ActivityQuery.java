package org.example.mesexadmin.data_access;

import org.bson.Document;
import org.example.mesexadmin.MongoManagement;
import org.example.mesexadmin.data_class.ActivityData;

import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;

import java.util.ArrayList;

public class ActivityQuery {
    MongoManagement mongoManagement;

    ActivityQuery(MongoManagement mongoManagement) {
        this.mongoManagement = mongoManagement;
    }

    public ArrayList<ActivityData> getLoginLog(){
        return null;
    }

    public ArrayList<ActivityData> getRegisterLog(){
        return null;
    }

    public ArrayList<ActivityData> getReportLog(){
        return null;
    }

    public boolean insertActivity(ActivityData activityData) {
        MongoCollection<Document> activities = mongoManagement.database.getCollection("activities");

        try {
            activities.insertOne(activityData.toDocument());
        } catch (MongoWriteException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

}
