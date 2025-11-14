package entity;

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
}
