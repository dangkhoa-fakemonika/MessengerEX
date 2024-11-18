db = connect('mongodb://localhost/messenger-ex-app');

db.createCollection("messages", {
    validator: {
        $jsonSchema: {
            bsonType: "object",
            required: ["senderId", "content", "timeSent", "conversationId"],
            properties: {
                senderId: { bsonType: "objectId" },
                content: { 
                    bsonType: "string",
                    maxLength: 1000
                },
                timeSent: { bsonType: "date" },
                conversationId: { bsonType: "objectId" }
            }
        }
    },
    validationAction: "error"
});
