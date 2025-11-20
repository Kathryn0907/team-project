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

import java.util.ArrayList;

public class MongoDBUserDAO {

    private final MongoCollection<Document> usersCollection;
    private MongoDBExtractToCache data;

    public MongoDBUserDAO() {
        MongoClient client = MongoConfig.getClient();
        MongoDatabase db = client.getDatabase("CSC207_group_project_2025");
        this.usersCollection = db.getCollection("Users");
        this.data = new MongoDBExtractToCache();
    }


    /**
     * Save or update the user in database.
     * To update, make sure the Id is correct.
     * @param user the user.
     */
    public void saveUser(User user) {

        // Check if the User is already in database. If yes, delete that User
        // so this will renew the data.
        if (usersCollection.find(Filters.eq("id", user.getId())).first() != null) {
            usersCollection.deleteOne(Filters.eq("id", user.getId()));
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
     * Find the comment in local cache. You can use refresh method if you just
     * save something but didn't find it.
     * @param userId The id of the user.
     * @return null if not found.
     */
    public User findUserById(ObjectId userId) {
        return data.findUserById(userId);
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
}
