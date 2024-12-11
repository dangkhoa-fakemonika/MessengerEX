package org.example.mesexadmin.data_access;

import java.util.Arrays;

import org.bson.Document;
import org.example.mesexadmin.MongoManagement;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Projections;

public class SpamTicketQuery {
    MongoManagement mongoManagement;

    SpamTicketQuery(MongoManagement mongoManagement) {
        this.mongoManagement = mongoManagement;
    }

    public void getSpamTicketDetails() {
        // Test query

        MongoCollection<Document> tickets = mongoManagement.database.getCollection("spam_ticket");

        AggregateIterable<Document> result = tickets.aggregate(

            Arrays.asList(

                Aggregates.lookup("users", "reporterId", "_id", "reporterDetails"),
                Aggregates.lookup("users", "reportedUserId", "_id", "reportedUserDetails"),

                Aggregates.project(Projections.fields(
                    Projections.include("reporterId", "reportedUserId"),
                    Projections.computed("reporterName", "$reporterDetails.name"),
                    Projections.computed("reportedUserName", "$reportedUserDetails.name")
                ))
            )
        );
    }
 
}
