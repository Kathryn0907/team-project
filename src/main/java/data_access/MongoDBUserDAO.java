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

    public MongoDBUserDAO() {
        MongoClient client = MongoConfig.getClient();
        MongoDatabase db = client.getDatabase("CSC207_group_project_2025");
        this.collection = db.getCollection("Users");
    }


    public void saveUser(User user) {

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

    public User findUser(ObjectId id) {

        Document doc = collection.find(Filters.eq("id", id)).first();

        return null;

    }
}
