package data_access;

import Entities.Comment;
import Entities.Listing;
import Entities.User;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;
import use_case.cancel_account.CancelAccountDataAccessInterface;
import use_case.login.LoginUserDataAccessInterface;
import use_case.logout.LogoutUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;

import java.util.ArrayList;

public class MongoDBUserDAO implements SignupUserDataAccessInterface,
        LoginUserDataAccessInterface,
        LogoutUserDataAccessInterface, CancelAccountDataAccessInterface {

    private final MongoCollection<Document> usersCollection;
    private MongoDBExtractToCache data;

    public MongoDBUserDAO() {
        this.data = new MongoDBExtractToCache();
        this.usersCollection = data.getDatabase().getCollection("Users");
    }


    /**
     * Save or update the user in database.
     * To update, make sure the id is correct.
     * @param user the user.
     */
    public void saveUser(User user) {

        // Check if the User is already in database. If yes, delete that User
        // so this will renew the data.
        if (usersCollection.find(Filters.eq("id", user.getId())).first() != null) {
            usersCollection.deleteOne(Filters.eq("id", user.getId()));
        } else if (usersCollection.find(Filters.eq("username", user.getUsername())).first() != null) {
            usersCollection.deleteOne(Filters.eq("username", user.getUsername()));
        }

        ArrayList<ObjectId> myListingsIds = new ArrayList<>();
        for (Listing listing : user.getMyListings()) {
            myListingsIds.add(listing.getId());
        }

        ArrayList<ObjectId> favouriteListingsIds = new ArrayList<>();
        for (Listing listing : user.getFavourite()) {
            favouriteListingsIds.add(listing.getId());
        }

        ArrayList<ObjectId> myCommentsIds = new ArrayList<>();
        for (Comment comment : user.getMyComments()) {
            myCommentsIds.add(comment.getIdForDB());
        }


        Document userDocument = new Document()
                .append("id", user.getId())
                .append("username", user.getUsername())
                .append("password", user.getPassword())
                .append("myListings", myListingsIds)
                .append("favouriteListings", favouriteListingsIds)
                .append("myComments", myCommentsIds);

        usersCollection.insertOne(userDocument);

    }


    public void deleteUser(User user) {
        ObjectId userId = user.getId();
        if (usersCollection.find(Filters.eq("id", userId)).first() != null) {
            usersCollection.deleteOne(Filters.eq("id", userId));
        } else {
            System.out.println("Listing with id: " + userId + " and name: "
                    + user.getUsername() + " not found in Database");
        }
    }


    /**
     * Find the user in local cache. You can use refresh method if you just
     * save something but didn't find it.
     * @param userId The id of the user.
     * @return null if not found.
     */
    public User findUserById(ObjectId userId) {
        return data.findUserById(userId);
    }


    /**
     * Find the user in local cache. You can use refresh method if you just
     * save something but didn't find it.
     * @param username username of the user.
     * @return null if not found.
     */
    public User findUserByUsername(String username) {
        for (User user : getAllUsers()) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        } return null;
    }


    /**
     * Refresh the local cache.
     */
    public void refreshData() {
        this.data = new MongoDBExtractToCache();
    }


    /**
     * Get all users in local cache. You can use refresh method if you just
     * save something but didn't find it.
     * @return A list of users.
     */
    public ArrayList<User> getAllUsers() {
        return data.getUsersCache();
    }


    //-------------------- Override Methods ----------------------

    @Override
    public boolean existsByName(String username) {
        return usersCollection.find(Filters.eq("username", username)).first() != null;
    }


    @Override
    public void cancelAccount(String username) {
        MongoDBListingDAO mongoDBListingDAO = new MongoDBListingDAO();
        MongoDBCommentDAO mongoDBCommentDAO = new MongoDBCommentDAO();
        User user = findUserByUsername(username);
        for (Listing listing : user.getMyListings()) {
            for (Comment comment : listing.getComments()) {
                comment.setListing(null);
                mongoDBCommentDAO.saveComment(comment);
            }
            mongoDBListingDAO.deleteListing(listing);
        }
        usersCollection.deleteOne(Filters.eq("username", username));
    }


    @Override
    public void save(User user) {saveUser(user);}


    @Override
    public User getUser(String username) {
        Document userDocument = usersCollection.find(Filters.eq("username",username)).first();
        if (userDocument != null) {
            ObjectId userId = userDocument.getObjectId("id");
            return findUserById(userId);
        } else {
            for (User user : getAllUsers()) {
                if (user.getUsername().equals(username)) {
                    return user;
                }
            }
        }
        return null;
    }





    //----------------------------------------------------------------------
    // Will not use these methods. These are for tests of interactor, and they should
    // run in InMemoryDAOs.
    private final String msg = "You are using MongoDB to run the interactor unit test.\nIf you want to test " +
            "the DB please avoid using these methods since they belongs to use case DAIS \nthat should not have" +
            "these functions.";
    @Override
    public void setCurrentUsername(String name) {
        System.out.println(msg);
//        throw new RuntimeException("method name: setCurrentUsername");
    }
    @Override
    public String getCurrentUsername() {
        System.out.println(msg);
//        throw new RuntimeException("method name: getCurrentUsername");
        return null;
    }
    @Override
    public void addListing(Listing listing) {
        System.out.println(msg);
//        throw new RuntimeException("method name: addListing");
    }
    @Override
    public void addUser(User user) {
        System.out.println(msg);
//        throw new RuntimeException("method name: addUser");
    }
    @Override
    public ArrayList<Listing> getAllActiveListings() {
        System.out.println(msg);
//        throw new RuntimeException("method name: getAllActiveListings");
        return null;
    }
}
