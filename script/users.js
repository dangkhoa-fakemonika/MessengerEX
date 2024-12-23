db = connect('mongodb://localhost/messenger-ex-app');

db.createCollection("users", {
    validator: {
        $jsonSchema: {
            bsonType: "object",
            required: ["role", "username", "email", "passwordHash", "dateCreated"],
            properties: {
                role: { 
                    bsonType: "string",
                    enum: ["admin", "user"]
                },
                username: { 
                    bsonType: "string",
                    minLength: 5,
                    maxLength: 50
                },
                name: { 
                    bsonType: "string",
                    maxLength: 100
                },
                address: { 
                    bsonType: "string",
                    maxLength: 200 
                },
                dateOfBirth: { bsonType: ["date", "null"] },
                gender: { 
                    bsonType: "string",
                    enum: ["male", "female"]
                },
                email: { 
                    bsonType: "string",
                    minLength: 3
                },
                passwordHash: { bsonType: "string" },
                friendList: { 
                    bsonType: "array",
                    items: { bsonType: "objectId" } 
                },
                blockList: { 
                    bsonType: "array",
                    items: { bsonType: "objectId" } 
                },
                status: { 
                    bsonType: "string",
                    enum: ["online", "offline", "banned"]
                },
                lastLogin: { bsonType: ["date", "null"] },
                dateCreated: { bsonType: "date" }
            }
        }
    },
    validationAction: "error"
});
