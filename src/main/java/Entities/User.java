package Entities;

import java.util.ArrayList;

public class User {

    private final String username;
    private String password;
    private ArrayList<Listing> myListings =  new ArrayList<>();
    private ArrayList<Listing> favourite = new ArrayList<>();
    private ArrayList<Comments> myComments =   new ArrayList<>();

    public User(String userName, String password) {
        if (userName == null || password == null) {
            throw new IllegalArgumentException("Username or password cannot be empty");
        }
        this.username = userName;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    public void addMyListing(Listing listing){
        if(!myListings.contains(listing)){
            throw new IllegalArgumentException("Listing not found");
        }
        myListings.add(listing);
    }

    public void removeMyListing(Listing listing){
        myListings.remove(listing);
    }

    public ArrayList<Listing> getMyListings() {
        return myListings;
    }

    public void addFavourite(Listing listing){
        favourite.add(listing);
    }

    public void removeFavourite(Listing listing){
        if(!favourite.contains(listing)){
            throw new IllegalArgumentException("Listing not found");
        }
        myListings.remove(listing);
    }

    public ArrayList<Listing> getFavourite() {
        return favourite;
    }

    public void addComment(Comments comment){
        myComments.add(comment);
    }

    public void removeComment(Comments comment){
        if(!myComments.contains(comment)){
            throw new IllegalArgumentException("Comment not found");
        }
        myComments.remove(comment);
    }

    public ArrayList<Comments> getMyComments() {
        return myComments;
    }
}
