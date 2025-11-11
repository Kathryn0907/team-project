package Entities;

import java.util.ArrayList;

public class User {

    private final String username;
    private String password;
    private ArrayList<Listing> myListings =  new ArrayList<>();
    private ArrayList<Listing> favourite = new ArrayList<>();
    private ArrayList<Comments> myComments =   new ArrayList<>();

    public User(String userName, String password) {
        this.username = userName;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void resetPassword(String newPassword) {
        this.password = newPassword;
    }

    public void addMyListing(Listing listing){
        myListings.add(listing);
    }

    public ArrayList<Listing> getMyListings() {
        return myListings;
    }

    public void addFavourite(Listing listing){
        favourite.add(listing);
    }

    public ArrayList<Listing> getFavourite() {
        return favourite;
    }

    public void addComment(Comments comment){
        myComments.add(comment);
    }

    public ArrayList<Comments> getMyComments() {
        return myComments;
    }
}
