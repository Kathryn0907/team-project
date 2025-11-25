package data_access;

import Entities.Message;
import use_case.messaging.MessageDataAccessInterface;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * MongoDB implementation of message storage with conversation tracking
 */
public class MongoDBMessageDAO implements MessageDataAccessInterface {

    private final MongoDatabase database;
    private final MongoCollection<Document> messagesCollection;

    public MongoDBMessageDAO() {
        MongoClient client = MongoConfig.getClient();
        this.database = client.getDatabase("CSC207_group_project_2025");
        this.messagesCollection = database.getCollection("Messages");

        System.out.println("✅ Connected to MongoDB Messages collection");
    }

    @Override
    public void saveMessage(Message message) {
        try {
            Document doc = new Document("_id", message.getId())
                    .append("fromUsername", message.getFromUsername())
                    .append("toUsername", message.getToUsername())
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
            Bson query = Filters.or(
                    Filters.eq("fromUsername", username),
                    Filters.eq("toUsername", username)
            );

            for (Document doc : messagesCollection.find(query).sort(Sorts.ascending("timestamp"))) {
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
            Bson query = Filters.or(
                    Filters.and(
                            Filters.eq("fromUsername", user1),
                            Filters.eq("toUsername", user2)
                    ),
                    Filters.and(
                            Filters.eq("fromUsername", user2),
                            Filters.eq("toUsername", user1)
                    )
            );

            for (Document doc : messagesCollection.find(query).sort(Sorts.ascending("timestamp"))) {
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
            Bson query = Filters.and(
                    Filters.eq("toUsername", username),
                    Filters.eq("isRead", false)
            );

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
            Bson query = Filters.eq("_id", messageId);
            Document update = new Document("$set", new Document("isRead", true));

            messagesCollection.updateOne(query, update);
            System.out.println("✅ [MongoDB] Marked message as read: " + messageId);

        } catch (Exception e) {
            System.err.println("Error marking message as read: " + e.getMessage());
        }
    }

    /**
     * Get all unique conversation partners for a user
     */
    public ArrayList<String> getConversationPartners(String username) {
        Set<String> partners = new HashSet<>();

        try {
            Bson query = Filters.or(
                    Filters.eq("fromUsername", username),
                    Filters.eq("toUsername", username)
            );

            for (Document doc : messagesCollection.find(query)) {
                String from = doc.getString("fromUsername");
                String to = doc.getString("toUsername");

                if (!from.equals(username)) {
                    partners.add(from);
                }
                if (!to.equals(username)) {
                    partners.add(to);
                }
            }

            System.out.println("👥 [MongoDB] Found " + partners.size() + " conversation partners for: " + username);

        } catch (Exception e) {
            System.err.println("Error getting conversation partners: " + e.getMessage());
        }

        return new ArrayList<>(partners);
    }

    /**
     * Get the last message in a conversation
     */
    public Message getLastMessage(String user1, String user2) {
        try {
            Bson query = Filters.or(
                    Filters.and(
                            Filters.eq("fromUsername", user1),
                            Filters.eq("toUsername", user2)
                    ),
                    Filters.and(
                            Filters.eq("fromUsername", user2),
                            Filters.eq("toUsername", user1)
                    )
            );

            Document doc = messagesCollection.find(query)
                    .sort(Sorts.descending("timestamp"))
                    .first();

            if (doc != null) {
                return documentToMessage(doc);
            }

        } catch (Exception e) {
            System.err.println("Error getting last message: " + e.getMessage());
        }

        return null;
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
}