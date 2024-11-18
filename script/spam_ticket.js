db = connect('mongodb://localhost/messenger-ex-app');

db.createCollection("spam_ticket", {
    validator: {
        $jsonSchema: {
            bsonType: "object",
            required: ["reporterId", "reportedUserId", "timeSent"],
            properties: {
                reporterId: { bsonType: "objectId" },
                reportedUserId: { bsonType: "objectId" },
                timeSent: { bsonType: "date" }
            }
        }
    },
    validationAction: "error"
});
