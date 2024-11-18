db.createCollection("users", {
    validator: {
        $jsonSchema: {
            bsonType: "object",
            required: ["role", "username", "email", "passwordHash", "dateCreated"],
            properties: {
                role: { 
                    bsonType: "string",
                    enum: ["admin, user"]
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
                dateOfBirth: { bsonType: "date" },
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
                    enum: ["online", "offline", "locked"]
                },
                lastLogin: { bsonType: "date" },
                dateCreated: { bsonType: "date" }
            }
        }
    },
    validationAction: "error"
});

db.users.createIndex({ "email": 1 }, { unique: true });
db.users.createIndex({ "username": 1 }, { unique: true });