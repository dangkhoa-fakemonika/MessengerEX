package org.example.mesexadmin.data_access;

import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.example.mesexadmin.MongoManagement;
import org.example.mesexadmin.data_class.FriendRequestData;

import java.util.ArrayList;
import java.util.Date;

public class FriendRequestQuery {
    MongoManagement mongoManagement;


    public FriendRequestQuery(MongoManagement mongoManagement){
        this.mongoManagement = mongoManagement;
    }

    // create friend request
    public boolean sendRequest(ObjectId idSrc, ObjectId idTarget){
        MongoCollection<Document> requests = mongoManagement.database.getCollection("requests");

        FriendRequestData request = new FriendRequestData();
        request.setTimeSent(new Date());
        request.setSenderId(idSrc);
        request.setReceiverId(idTarget);

        try {
            requests.insertOne(request.toDocument());
        }
        catch (MongoWriteException e){
            return false;
        }

        return false;
    }

    // unsend friend request
    public boolean removeRequest(ObjectId idSrc, ObjectId idTarget){
        MongoCollection<Document> requests = mongoManagement.database.getCollection("requests");

        try {
            requests.deleteOne(
                    Filters.and(Filters.eq("senderId", idSrc), Filters.eq("receiverId", idTarget))
            );
        }
        catch (MongoWriteException e){
            return false;
        }

        return false;
    }

    public ArrayList<FriendRequestData> getAllRequest(ObjectId idSrc){
        MongoCollection<Document> requests = mongoManagement.database.getCollection("requests");
        ArrayList<Document> results = new ArrayList<>();
        requests.find(Filters.eq("senderId",idSrc)).into(results);
        ArrayList<FriendRequestData> data = new ArrayList<>();

        for (Document res : results){
            data.add(documentToRequest(res));
        }

        return data;
    }

    public ArrayList<FriendRequestData> getAllPending(ObjectId idTarget){
        MongoCollection<Document> pending = mongoManagement.database.getCollection("requests");
        ArrayList<Document> results = new ArrayList<>();
        pending.find(Filters.eq("receiverId",idTarget)).into(results);
        ArrayList<FriendRequestData> data = new ArrayList<>();

        for (Document res : results){
            data.add(documentToRequest(res));
        }

        return data;
    }


    private FriendRequestData documentToRequest(Document requestDocument){
        FriendRequestData requestData = new FriendRequestData();

        requestData.setRequestId(requestDocument.getObjectId("_id"));
        requestData.setReceiverId(requestDocument.getObjectId("receiverId"));
        requestData.setSenderId(requestDocument.getObjectId("senderId"));
        requestData.setReceiverName(requestDocument.getString("receiverName"));
        requestData.setSenderName(requestDocument.getString("senderName"));
        requestData.setTimeSent(requestDocument.getDate("timeSent"));
        return requestData;
    }
}
