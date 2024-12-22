package org.example.mesex.data_access;

import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.example.mesex.MongoManagement;
import org.example.mesex.data_class.ActivityData;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

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

    public ArrayList<ActivityData> viewUserLoginHistory(ObjectId targetId){
        MongoCollection<Document> activities = mongoManagement.database.getCollection("activities");
        ArrayList<Document> results = new ArrayList<>();
        activities.find(Filters.eq("userId", targetId)).into(results);

        ArrayList<ActivityData> data = new ArrayList<>();
        results.forEach((res) -> data.add(documentToActivity(res)));
        return data;
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
