db = connect('mongodb://localhost/messenger-ex-app');

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
