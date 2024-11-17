package org.example.mesex.data_class;

import javafx.beans.property.SimpleStringProperty;

public class SpamTicketData {
    public SimpleStringProperty reportedId;
    public SimpleStringProperty reporterId;
    public SimpleStringProperty timeSent;

    public SpamTicketData(String reporter, String reportID, String time){
        reportedId = new SimpleStringProperty(reportID);
        reporterId = new SimpleStringProperty(reporter);
        timeSent = new SimpleStringProperty(time);
    }

    public String getReportedId() {
        return reportedId.get();
    }

    public String getTimeSent() {
        return timeSent.get();
    }

    public String getReporterId() {
        return reporterId.get();
    }

    public void setReportedId(String reportedId) {
        this.reportedId.set(reportedId);
    }

    public void setTimeSent(String timeSent) {
        this.timeSent.set(timeSent);
    }

    public void setReporterId(String reporterId) {
        this.reporterId.set(reporterId);
    }
}
