db = connect('mongodb://localhost/messenger-ex-app');

db.createCollection("conversations", {
    validator: {
        $jsonSchema: {
            bsonType: "object",
            required: ["type", "conversationName", "membersId", "dateCreated"],
            properties: {
                type: {
                    bsonType: "string",
                    enum: ["private", "group"]
                },
                conversationName: {
                    bsonType: "string",
                    minLength: 3,
                    maxLength: 50
                },
                membersId: {
                    bsonType: "array",
                    items: { bsonType: "objectId" }
                },
                moderatorsId: {
                    bsonType: "array",
                    items: { bsonType: "objectId" }
                },
                dateCreated: { bsonType: "date" }
            }
        }
    },
    validationAction: "error"
});