package org.example.mesexadmin.data_access;

import com.mongodb.MongoWriteException;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.example.mesexadmin.MongoManagement;
import org.example.mesexadmin.data_class.FriendRequestData;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class FriendRequestQuery {
    MongoManagement mongoManagement;


    public FriendRequestQuery(MongoManagement mongoManagement){
        this.mongoManagement = mongoManagement;
    }

    // create friend request
    public boolean insertFriendRequest(FriendRequestData request){
        MongoCollection<Document> requests = mongoManagement.database.getCollection("friend_requests");

        try {
            requests.insertOne(request.toDocument());
        }
        catch (MongoWriteException e){
            return false;
        }

        return true;
    }

    // unsend friend request
    public boolean removeRequest(ObjectId requestId){
        MongoCollection<Document> requests = mongoManagement.database.getCollection("friend_requests");

        try {
            requests.deleteOne(Filters.eq("_id", requestId));
        }
        catch (MongoWriteException e){
            return false;
        }

        return true;
    }

    public FriendRequestData getSingleRequest(ObjectId senderId, ObjectId receiverId) {
        MongoCollection<Document> requests = mongoManagement.database.getCollection("friend_requests");

        Document request = requests.find(Filters.and(
            Filters.eq("senderId", senderId),
            Filters.eq("receiverId", receiverId))).first();

        if (request != null) 
            return documentToRequest(request);

        return null;
    }

    public ArrayList<FriendRequestData> getAllRequestsDetails(ObjectId senderId, ObjectId receiverId) {
        MongoCollection<Document> requests = mongoManagement.database.getCollection("friend_requests");
        ArrayList<FriendRequestData> data = new ArrayList<>();

        List<Document> pipeline = new ArrayList<>();

        Document filter = new Document();
        
        // Add filter base on parameters
        // Get specific request
        if (senderId != null && receiverId != null) { filter.append("senderId", senderId).append("receiverId", receiverId); } 
        // Get all request sent by user with sendeId
        else if (senderId != null) { filter.append("senderId", senderId); } 
        // Get all request received by user with receiverId
        else if (receiverId != null) { filter.append("receiverId", receiverId); }

        pipeline.add(new Document("$match", filter));

        // Join to get sender name
        pipeline.add(new Document("$lookup", new Document()
            .append("from", "users")
            .append("localField", "senderId")
            .append("foreignField", "_id")
            .append("as", "sender")
        ));

        // Join to get receiver name
        pipeline.add(new Document("$lookup", new Document()
            .append("from", "users")
            .append("localField", "receiverId")
            .append("foreignField", "_id")
            .append("as", "receiver")
        ));

        // Flatten document
        pipeline.add(new Document("$unwind", new Document("path", "$sender")));
        pipeline.add(new Document("$unwind", new Document("path", "$receiver")));

        // Extract neccessary property
        pipeline.add(new Document("$project", new Document("senderUsername", "$sender.username")
            .append("receiverUsername", "$receiver.username")
            .append("senderId", 1)
            .append("receiverId", 1)
            .append("timeSent", 1)
            .append("_id", 1)));

        // Apply aggregate
        AggregateIterable<Document> result = requests.aggregate(pipeline);

        // Map to FriendRequestData
        for (Document doc : result) 
            data.add(documentToRequest(doc));

        return data;
    }

    // public ArrayList<FriendRequestData> getAllRequestByReceiver(ObjectId receiverId){
    //     MongoCollection<Document> pending = mongoManagement.database.getCollection("friend_requests");
        
    //     ArrayList<Document> results = new ArrayList<>();
    //     pending.find(Filters.eq("receiverId", receiverId)).into(results);

    //     ArrayList<FriendRequestData> data = new ArrayList<>();

    //     for (Document res : results){
    //         data.add(documentToRequest(res));
    //     }

    //     return data;
    // }


    private FriendRequestData documentToRequest(Document requestDocument){
        FriendRequestData requestData = new FriendRequestData();

        requestData.setRequestId(requestDocument.getObjectId("_id"));
        requestData.setReceiverId(requestDocument.getObjectId("receiverId"));
        requestData.setSenderId(requestDocument.getObjectId("senderId"));
        requestData.setReceiverUserame(requestDocument.getString("receiverUsername"));
        requestData.setSenderUserame(requestDocument.getString("senderUsername"));
        requestData.setTimeSent(requestDocument.getDate("timeSent"));
        return requestData;
    }
}
