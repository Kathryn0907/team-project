package data_access;

import Entities.Message;
import use_case.messaging.MessageDataAccessInterface;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * MongoDB implementation of message storage
 * Your friend implements this using MongoDB
 */
public class MongoDBMessageDAO implements MessageDataAccessInterface {

    private final MongoClient mongoClient;
    private final MongoDatabase database;
    private final MongoCollection<Document> messagesCollection;

    /**
     * Constructor with connection string
     * @param connectionString MongoDB connection string (e.g., "mongodb://localhost:27017")
     * @param databaseName Name of the database
     */
    public MongoDBMessageDAO(String connectionString, String databaseName) {
        this.mongoClient = MongoClients.create(connectionString);
        this.database = mongoClient.getDatabase(databaseName);
        this.messagesCollection = database.getCollection("messages");

        System.out.println("✅ Connected to MongoDB: " + databaseName);
    }

    /**
     * Default constructor - connects to local MongoDB
     */
    public MongoDBMessageDAO() {
        this("mongodb://localhost:27017", "airbnb_messaging");
    }

    @Override
    public void saveMessage(Message message) {
        try {
            Document doc = new Document("_id", message.getId())
                    .append("fromUsername", message.getFromUsername())
                    .append("toUsername", message.getToUsername())
                    .append("listingId", message.getListingId())
                    .append("content", message.getContent())
                    .append("timestamp", Date.from(message.getTimestamp()))
                    .append("isRead", message.isRead());

            messagesCollection.insertOne(doc);
            System.out.println("💾 [MongoDB] Saved message: " + message.getId());

        } catch (Exception e) {
            System.err.println("Error saving message to MongoDB: " + e.getMessage());
            throw new RuntimeException("Failed to save message", e);
        }
    }

    @Override
    public ArrayList<Message> getMessagesForUser(String username) {
        ArrayList<Message> userMessages = new ArrayList<>();

        try {
            // Query: Find messages where user is sender OR recipient
            Document query = new Document("$or", Arrays.asList(
                    new Document("fromUsername", username),
                    new Document("toUsername", username)
            ));

            for (Document doc : messagesCollection.find(query)) {
                userMessages.add(documentToMessage(doc));
            }

            System.out.println("📥 [MongoDB] Retrieved " + userMessages.size() + " messages for user: " + username);

        } catch (Exception e) {
            System.err.println("Error retrieving messages: " + e.getMessage());
        }

        return userMessages;
    }

    @Override
    public ArrayList<Message> getConversation(String user1, String user2) {
        ArrayList<Message> conversation = new ArrayList<>();

        try {
            // Query: Messages between two specific users (both directions)
            Document query = new Document("$or", Arrays.asList(
                    new Document("fromUsername", user1).append("toUsername", user2),
                    new Document("fromUsername", user2).append("toUsername", user1)
            ));

            // Sort by timestamp ascending (oldest first)
            for (Document doc : messagesCollection.find(query).sort(new Document("timestamp", 1))) {
                conversation.add(documentToMessage(doc));
            }

            System.out.println("💬 [MongoDB] Retrieved conversation: " + user1 + " ↔ " + user2 +
                    " (" + conversation.size() + " messages)");

        } catch (Exception e) {
            System.err.println("Error retrieving conversation: " + e.getMessage());
        }

        return conversation;
    }

    @Override
    public ArrayList<Message> getUnreadMessages(String username) {
        ArrayList<Message> unreadMessages = new ArrayList<>();

        try {
            // Query: Unread messages sent TO this user
            Document query = new Document("toUsername", username)
                    .append("isRead", false);

            for (Document doc : messagesCollection.find(query)) {
                unreadMessages.add(documentToMessage(doc));
            }

            System.out.println("📬 [MongoDB] Retrieved " + unreadMessages.size() +
                    " unread messages for: " + username);

        } catch (Exception e) {
            System.err.println("Error retrieving unread messages: " + e.getMessage());
        }

        return unreadMessages;
    }

    @Override
    public void markAsRead(String messageId) {
        try {
            Document query = new Document("_id", messageId);
            Document update = new Document("$set", new Document("isRead", true));

            messagesCollection.updateOne(query, update);
            System.out.println("✅ [MongoDB] Marked message as read: " + messageId);

        } catch (Exception e) {
            System.err.println("Error marking message as read: " + e.getMessage());
        }
    }

    /**
     * Convert MongoDB Document to Message entity
     */
    private Message documentToMessage(Document doc) {
        String id = doc.getString("_id");
        String fromUsername = doc.getString("fromUsername");
        String toUsername = doc.getString("toUsername");
        String listingId = doc.getString("listingId");
        String content = doc.getString("content");
        Date timestamp = doc.getDate("timestamp");
        boolean isRead = doc.getBoolean("isRead", false);

        Message message = new Message(id, fromUsername, toUsername, listingId, content);
        message.setTimestamp(timestamp.toInstant());
        message.setRead(isRead);

        return message;
    }

    /**
     * Close MongoDB connection
     */
    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("🔌 MongoDB connection closed");
        }
    }
}