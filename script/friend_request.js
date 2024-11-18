db.createCollection("friend_request", {
    validator: {
        $jsonSchema: {
            bsonType: "object",
            required: ["senderId", "receiverId", "timeSent"],
            properties: {
                senderId: { bsonType: "objectId" },
                receiverId: { bsonType: "objectId" },
                timeSent: { bsonType: "date" }
            }
        }
    },
    validationAction: "error"
});

db.friend_request.createIndex({ "timeSent": -1 });