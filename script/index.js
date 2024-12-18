db.messages.createIndex({ "timeSent": -1 });
db.friend_requests.createIndex({ "timeSent": -1 });
db.users.createIndex({ "email": 1 }, { unique: true });
db.users.createIndex({ "username": 1 }, { unique: true });