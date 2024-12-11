db = connect('mongodb://localhost/messenger-ex');

db.createCollection("activities", {
    validator: {
        $jsonSchema: {
            bsonType: "object",
            required: ["userId"],
            properties: {
                userId: { bsonType: "objectId" },
                username: { bsonType: "string" },
                loginDate: { bsonType: "date" },
            }
        }
    },
    validationAction: "error"
});