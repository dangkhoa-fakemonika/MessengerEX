package org.example.mesex.data_class;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Date;

public class SpamTicketData {
    ObjectId ticketId;
    ObjectId reportedId;
    ObjectId reporterId;
    SimpleStringProperty reportedName;
    SimpleStringProperty reporterName;
    SimpleObjectProperty<Date> timeSent;
    SimpleStringProperty reportedEmail;

    public SpamTicketData(){
        ticketId = new ObjectId();
        reportedId = new ObjectId();
        reporterId = new ObjectId();
        reportedName = new SimpleStringProperty();
        reporterName = new SimpleStringProperty();
        timeSent = new SimpleObjectProperty<>(null);
        reportedEmail = new SimpleStringProperty();
    }

    public SpamTicketData(String reporter, String reportID, String time){
        reportedName = new SimpleStringProperty(reportID);
        reporterName = new SimpleStringProperty(reporter);
        timeSent = new SimpleObjectProperty<>(null);
    }

    public ObjectId getReportedId() {
        return reportedId;
    }

    public ObjectId getReporterId() {
        return reporterId;
    }

    public ObjectId getTicketId() {
        return ticketId;
    }

    public String getReportedEmail() {
        return reportedEmail.get();
    }

    public String getReportedName() {
        return reportedName.get();
    }

    public Date getTimeSent() {
        return timeSent.get();
    }

    public String getReporterName() {
        return reporterName.get();
    }

    public void setReporterId(ObjectId reporterId) {
        this.reporterId = reporterId;
    }

    public void setReportedId(ObjectId reportedId) {
        this.reportedId = reportedId;
    }

    public void setTicketId(ObjectId ticketId) {
        this.ticketId = ticketId;
    }

    public void setReportedEmail(String reportedEmail) {
        this.reportedEmail.set(reportedEmail);
    }

    public void setReportedName(String reportedName) {
        this.reportedName.set(reportedName);
    }

    public void setTimeSent(Date timeSent) {
        this.timeSent.set(timeSent);
    }

    public void setReporterName(String reporterName) {
        this.reporterName.set(reporterName);
    }

    public Document toDocument(){
        Document doc = new Document();
        doc.append("reportedUserId", this.reportedId).append("reporterId", this.reporterId).append("timeSent", this.timeSent.get());
        return doc;
    }
}
