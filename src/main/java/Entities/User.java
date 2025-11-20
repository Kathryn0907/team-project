package Entities;

import org.bson.types.ObjectId;

import java.util.ArrayList;

public class User {

    // User class include getter for each attribute. Adder and remover for every list of listing
    // and changer for password.

    private ObjectId id = ObjectId.get();
    private final String username;
    private String password;
    private ArrayList<Listing> myListings =  new ArrayList<>();
    private ArrayList<Listing> favourite = new ArrayList<>();
    private ArrayList<Comment> myComments =   new ArrayList<>();

    /**
     * Creates a new user with the given non-empty name and non-empty password.
     * @param userName the username
     * @param password the password
     * @throws IllegalArgumentException if the password or name are empty
     */

    public User(String userName, String password) {
        if (userName == null || password == null) {
            throw new IllegalArgumentException("Username or password cannot be empty");
        }
        this.username = userName;
        this.password = password;
    }

    public ObjectId getId() {return id;}

    public void setId(ObjectId id) {this.id = id;}

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
        if(listing == null){
            throw new IllegalArgumentException("Listing cannot be null");
        }
        if(!myListings.contains(listing)){
            myListings.add(listing);
        }
    }

    public void removeMyListing(Listing listing){
        myListings.remove(listing);

        myListings.removeIf(myListing -> myListing.getId().equals(listing.getId()) );
    }

    public ArrayList<Listing> getMyListings() {
        return myListings;
    }

    public void addFavourite(Listing listing){
        if(listing == null){
            throw new IllegalArgumentException("Listing cannot be null");
        }
        favourite.add(listing);
    }

    public void removeFavourite(Listing listing){
        favourite.remove(listing);

        favourite.removeIf(favouriteListing -> favouriteListing.getId().equals(listing.getId()));
    }

    public ArrayList<Listing> getFavourite() {
        return favourite;
    }

    public void addComment(Comment comment){
        myComments.add(comment);
    }

    public void removeComment(Comment comment){

        myComments.remove(comment);

        myComments.removeIf(myComments -> myComments.getIdForDB().equals(comment.getIdForDB()));
    }

    public ArrayList<Comment> getMyComments() {
        return myComments;
    }
}
