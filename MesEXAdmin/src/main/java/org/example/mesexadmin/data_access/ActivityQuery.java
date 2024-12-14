package org.example.mesexadmin.data_access;

import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import org.bson.Document;
import org.example.mesexadmin.MongoManagement;
import org.example.mesexadmin.data_class.ActivityData;

import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.Arrays;

public class ActivityQuery {
    MongoManagement mongoManagement;

    ActivityQuery(MongoManagement mongoManagement) {
        this.mongoManagement = mongoManagement;
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

    public ArrayList<ActivityData> viewLoginHistoryAll(){
        MongoCollection<Document> activities = mongoManagement.database.getCollection("activities");
        ArrayList<Document> results = new ArrayList<>();

        activities.aggregate(Arrays.asList(
                Aggregates.lookup("users", "userId","_id","userDetails"),
                Aggregates.unwind("$userDetails"),
                Aggregates.project(Projections.fields(
                        Projections.include("userId"),
                        Projections.computed("username", "$userDetails.username"),
                        Projections.computed("displayName", "$userDetails.displayName"),
                        Projections.include("loginDate")
                ))
        )).into(results);

        ArrayList<ActivityData> loginLogs = new ArrayList<>();
        for (Document res : results){
            loginLogs.add(documentToActivity(res));
        }

        return loginLogs;
    }

    public ActivityData documentToActivity(Document activityDoc){
        ActivityData act = new ActivityData();
        act.setActivityId(activityDoc.getObjectId("_id"));
        act.setUsername(activityDoc.getString("username"));
        act.setDisplayName(activityDoc.getString("displayName"));
        act.setUserId(activityDoc.getObjectId("userId"));
        act.setLoginDate(activityDoc.getDate("loginDate"));
        return act;
    }

}
