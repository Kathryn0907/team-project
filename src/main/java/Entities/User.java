package entity;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String id;
    private String username;
    private String password;

    private List<Listing> listings;
    private List<Listing> favorites;
    private List<Comment> comments;

    public User(String id, String username, String password) {}
 }
