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
import java.util.List;

public class MongoDBUserDAO {

    private final MongoCollection<Document> collection;
    private MongoDBExtractToCache data;

    public MongoDBUserDAO() {
        MongoClient client = MongoConfig.getClient();
        MongoDatabase db = client.getDatabase("CSC207_group_project_2025");
        this.collection = db.getCollection("Users");
        this.data = new MongoDBExtractToCache();
    }


    public void saveUser(User user) {

        // Check if the User is already in database. If yes, delete that User
        // so this will renew the data.
        if (collection.find(Filters.eq("id", user.getId())).first() == null) {
            collection.deleteOne(Filters.eq("id", user.getId()));
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

        collection.insertOne(userDocument);

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
}
