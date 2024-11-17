db.createCollection("conversations", {
    validator: {
        $jsonSchema: {
            bsonType: "object",
            required: ["type", "membersId", "dateCreated"],
            properties: {
                type: {
                    bsonType: "string",
                    enum: ["private", "group"]
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
                lastMessageId: { bsonType: "objectId" }
            }
        }
    },
    validationAction: "error"
});