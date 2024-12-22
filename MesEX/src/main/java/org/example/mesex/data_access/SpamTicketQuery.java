package org.example.mesex.data_access;

import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.example.mesex.MongoManagement;
import org.example.mesex.data_class.SpamTicketData;

import java.util.ArrayList;
import java.util.Arrays;

public class SpamTicketQuery {
    MongoManagement mongoManagement;

    SpamTicketQuery(MongoManagement mongoManagement) {
        this.mongoManagement = mongoManagement;
    }

    public ArrayList<SpamTicketData> getSpamTicketDetails() {

        MongoCollection<Document> tickets = mongoManagement.database.getCollection("spam_tickets");
        ArrayList<Document> results = new ArrayList<>();
        tickets.aggregate(
            Arrays.asList(

                Aggregates.lookup("users", "reporterId", "_id", "reporterDetails"),
                Aggregates.lookup("users", "reportedUserId", "_id", "reportedUserDetails"),
                
                Aggregates.unwind("$reporterDetails"),
                Aggregates.unwind("$reportedUserDetails"),

                Aggregates.project(Projections.fields( 
                    Projections.include("reporterId", "reportedUserId", "timeSent"),
                    Projections.computed("reporterUsername", "$reporterDetails.username"),
                    Projections.computed("username", "$reportedUserDetails.username"),
                    Projections.computed("email", "$reportedUserDetails.email")
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
        MongoCollection<Document> tickets = mongoManagement.database.getCollection("spam_tickets");

        try {
            tickets.insertOne(spamTicketData.toDocument());
        } catch (MongoWriteException e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public SpamTicketData documentToSpamTicket(Document ticket){
        SpamTicketData spamTicket = new SpamTicketData();
        spamTicket.setTicketId(ticket.getObjectId("_id"));
        spamTicket.setReportedId(ticket.getObjectId("reportedUserId"));
        spamTicket.setReportedName(ticket.getString("username"));
        spamTicket.setReporterId(ticket.getObjectId("reporterId"));
        spamTicket.setReporterName(ticket.getString("reporterUsername"));
        spamTicket.setTimeSent(ticket.getDate("timeSent"));
        spamTicket.setReportedEmail(ticket.getString("email"));
        return spamTicket;
    }
}
