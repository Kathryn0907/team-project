package Entities;

import java.time.Instant;

/**
 * Message entity for user-to-user messaging
 */
public class Message {
    private String id;
    private String fromUsername;
    private String toUsername;
    private String listingId;  // Optional: which listing they're discussing
    private String content;
    private Instant timestamp;
    private boolean isRead;

    public Message(String id, String fromUsername, String toUsername,
                   String listingId, String content) {
        this.id = id;
        this.fromUsername = fromUsername;
        this.toUsername = toUsername;
        this.listingId = listingId;
        this.content = content;
        this.timestamp = Instant.now();
        this.isRead = false;
    }

    // Getters
    public String getId() { return id; }
    public String getFromUsername() { return fromUsername; }
    public String getToUsername() { return toUsername; }
    public String getListingId() { return listingId; }
    public String getContent() { return content; }
    public Instant getTimestamp() { return timestamp; }
    public boolean isRead() { return isRead; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setRead(boolean read) { isRead = read; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }

    @Override
    public String toString() {
        return String.format("[%s] %s -> %s: %s",
                timestamp, fromUsername, toUsername, content);
    }
}