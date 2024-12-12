package org.example.mesexadmin.data_access;

import java.util.ArrayList;
import java.util.Arrays;

import com.mongodb.MongoWriteException;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.example.mesexadmin.MongoManagement;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Projections;
import org.example.mesexadmin.data_class.SpamTicketData;

import javax.print.Doc;

public class SpamTicketQuery {
    MongoManagement mongoManagement;

    SpamTicketQuery(MongoManagement mongoManagement) {
        this.mongoManagement = mongoManagement;
    }

    public ArrayList<SpamTicketData> getSpamTicketDetails() {
        // Test query

        MongoCollection<Document> tickets = mongoManagement.database.getCollection("spam_ticket");
        ArrayList<Document> results = new ArrayList<>();
        tickets.aggregate(

            Arrays.asList(

                Aggregates.lookup("users", "reporterId", "_id", "reporterDetails"),
                Aggregates.lookup("users", "reportedId", "_id", "reportedUserDetails"),

                Aggregates.project(Projections.fields(
                    Projections.include("reporterId", "reportedUserId"),
                    Projections.computed("reporterUsername", "$reporterDetails.username"),
                    Projections.computed("reportedUsername", "$reportedUserDetails.username")
                ))
            )
        ).into(results);

        ArrayList<SpamTicketData> spamData = new ArrayList<>();
        for (Document res : results){
            spamData.add(documentToSpamTicket(res));
        }

        return spamData;

    }

    public boolean addSpamTicket(SpamTicketData spamTicketData){
        MongoCollection<Document> tickets = mongoManagement.database.getCollection("spam_ticket");

        try {
            tickets.insertOne(spamTicketData.toDocument());
        } catch (MongoWriteException e){
            return false;
        }

        return true;
    }

    public boolean removeSpamTicket(ObjectId id){
        MongoCollection<Document> tickets = mongoManagement.database.getCollection("spam_ticket");

        try {
            tickets.deleteOne(Filters.eq("_id", id));
        } catch (MongoWriteException e){
            return false;
        }

        return true;
    }

    public SpamTicketData documentToSpamTicket(Document ticket){
        SpamTicketData spamTicket = new SpamTicketData();
        spamTicket.setTicketId(ticket.getObjectId("_id"));
        spamTicket.setReportedId(ticket.getObjectId("reportedId"));
        spamTicket.setReportedName(ticket.getString("reportedName"));
        spamTicket.setReporterId(ticket.getObjectId("reporterId"));
        spamTicket.setReporterName(ticket.getString("reporterName"));
        spamTicket.setTimeSent(ticket.getDate("timeSent"));
        return spamTicket;
    }
}
