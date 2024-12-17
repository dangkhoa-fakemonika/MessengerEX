package org.example.mesexadmin.data_access;

import com.mongodb.client.model.*;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.example.mesexadmin.MongoManagement;
import org.example.mesexadmin.data_class.ActivityData;

import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

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

    public ArrayList<ActivityData> viewLoginHistoryWithFilters(String key, String token){
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
                )),
                Aggregates.match(Filters.regex(key, token, "i"))
        )).into(results);

        ArrayList<ActivityData> loginLogs = new ArrayList<>();
        for (Document res : results){
            loginLogs.add(documentToActivity(res));
        }

        return loginLogs;
    }

    public Integer getLoginOnSectionCount(Integer month, Integer year){
        MongoCollection<Document> activities = mongoManagement.database.getCollection("activities");
        ArrayList<ObjectId> results = new ArrayList<>();

        Date startDate, endDate;

        if (month == null){
            startDate = Date.from(LocalDate.of(year, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
            endDate = Date.from(LocalDate.of(year, 1, 1).plusYears(1).minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
        else {
            startDate = Date.from(LocalDate.of(year, month, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
            endDate = Date.from(LocalDate.of(year, month, 1).plusMonths(1).minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        }

        activities.distinct("userId", Filters.and(Filters.lte("loginDate", endDate), Filters.gte("loginDate", startDate)), ObjectId.class).into(results);

        return results.size();
    }

    public ArrayList<String> getLoginUserYearIndexes(){
        MongoCollection<Document> activities = mongoManagement.database.getCollection("activities");
        Document userFirst = activities.find().sort(Sorts.ascending("loginDate")).first();
        Document userLast = activities.find().sort(Sorts.descending("loginDate")).first();
        ArrayList<String> yearList = new ArrayList<>();

        if (userFirst != null && userLast != null){
            ActivityData u1 = documentToActivity(userFirst), u2 = documentToActivity(userLast);
            int yearStart = Instant.ofEpochMilli(u1.getLoginDate().getTime()).atZone(ZoneId.systemDefault()).toLocalDate().getYear();
            int yearEnd = Instant.ofEpochMilli(u2.getLoginDate().getTime()).atZone(ZoneId.systemDefault()).toLocalDate().getYear();
            for (int i = yearStart; i <= yearEnd; i++)
                yearList.add(Integer.toString(i));
        }

        return yearList;
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
