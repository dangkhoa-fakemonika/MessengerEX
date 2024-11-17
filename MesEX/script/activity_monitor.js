db.createCollection("activity_monitor", {
    validator: {
        $jsonSchema: {
            bsonType: "object",
            required: ["userId"],
            properties: {
                userId: { bsonType: "objectId" },
                name: { bsonType: "string" },
                loginLogs: { 
                    bsonType: "array",
                    items: { bsonType: "date" }
                },
            }
        }
    },
    validationAction: "error"
});

db.users.createIndex({ "userId": 1 }, { unique: true });