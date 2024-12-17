db = connect('mongodb://localhost/messenger-ex-app');

db.createCollection("conversations", {
    validator: {
        $jsonSchema: {
            bsonType: "object",
            required: ["type", "name", "membersId", "dateCreated"],
            properties: {
                type: {
                    bsonType: "string",
                    enum: ["private", "group"]
                },
                name: {
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
                dateCreated: { bsonType: "date" },
                lastMessageId: { bsonType: ["objectId", "null"] }
            }
        }
    },
    validationAction: "error"
});