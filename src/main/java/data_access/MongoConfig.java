package data_access;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

public class MongoConfig {


    // You have to set your MongoDBURL environment variable. I will send it in Discord.
    private static MongoClient mongoClient;

    public static MongoClient getClient() {
        if (mongoClient == null) {
            String uri = System.getenv("MONGODB_URI");
            if (uri == null) {
                throw new RuntimeException("MONGODB_URI environment variable is not set.");
            }

            ConnectionString connectionString = new ConnectionString(uri);
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(connectionString)
                    .build();

            mongoClient = MongoClients.create(settings);
        }
        return mongoClient;
    }
}
