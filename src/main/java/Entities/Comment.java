package Entities;

import org.bson.types.ObjectId;

import java.time.Instant;

public class Comment {
    private String id;
    private String content;
    private User user;
    private Listing listing;
    private Instant createdAt;
    private ObjectId idForDB = ObjectId.get();

    // Add an empty comment constructor.
    public Comment() {}

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

    public void setId(String id) {
        this.id = id;
    }

    public ObjectId getIdForDB(){return idForDB;}

    public void setIdForDB(ObjectId id){
        idForDB = id;}


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Listing getListing() {
        return listing;
    }

    public void setListing(Listing listing) {
        this.listing = listing;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
