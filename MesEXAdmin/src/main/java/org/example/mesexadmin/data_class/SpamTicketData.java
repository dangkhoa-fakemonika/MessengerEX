package org.example.mesexadmin.data_class;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import org.bson.types.ObjectId;

import java.util.Date;

public class SpamTicketData {
    ObjectId ticketId;
    ObjectId reportedId;
    ObjectId reporterId;
    SimpleStringProperty reportedName;
    SimpleStringProperty reporterName;
    SimpleObjectProperty<Date> timeSent;

    public SpamTicketData(){

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

    public void setReportedName(String reportedName) {
        this.reportedName.set(reportedName);
    }

    public void setTimeSent(Date timeSent) {
        this.timeSent.set(timeSent);
    }

    public void setReporterName(String reporterName) {
        this.reporterName.set(reporterName);
    }


}
