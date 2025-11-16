package Entities;

import java.time.Instant;

public class Comment {
    private String id;
    private String content;
    private User user;
    private Listing listing;
    private Instant createdAt;

    public Comment(String id, String content, User user, Listing listing, Instant createdAt){
        this.id = id;
        this.content = content;
        this.user = user;
        this.listing = listing;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public User getUser() {
        return user;
    }

    public Listing getListing() {
        return listing;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
