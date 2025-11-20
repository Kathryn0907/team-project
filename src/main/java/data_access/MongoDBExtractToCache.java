package data_access;

import Entities.Comment;
import Entities.Listing;
import Entities.User;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MongoDBExtractToCache {

    private MongoDatabase database;


    private ArrayList<User> usersCache = new ArrayList<>();
    private ArrayList<Listing> listingCache = new ArrayList<>();
    private ArrayList<Comment> commentsCache = new ArrayList<>();

    private Map<User,List<ObjectId>> userListingsMap = new HashMap<>();
    private Map<User,List<ObjectId>> userFavouriteListingsMap = new HashMap<>();
    private Map<User,List<ObjectId>> userCommentsMap = new HashMap<>();

    private Map<Listing,ObjectId> listingOwnerMap = new HashMap<>();
    private Map<Listing,List<ObjectId>> listingCommentsMap = new HashMap<>();

    private Map<Comment,ObjectId> commentUserMap = new HashMap<>();
    private Map<Comment,ObjectId> commentListingMap = new HashMap<>();


    public MongoDBExtractToCache() {
        MongoClient mongoClient = MongoConfig.getClient();
        database = mongoClient.getDatabase("CSC207_group_project_2025");
    }


    /**
     * This method will load all data to Cache. PLEASE use this method for get the data
     * (don't use the private helpers)
     * After loaded, use getter for usersCache, listingCache and commentCache
     * to get ArrayList of them.
     */
    public void load() {

        MongoCollection<Document> usersCollection = database.getCollection("users");
        MongoCollection<Document> listingsCollection = database.getCollection("listings");
        MongoCollection<Document> commentsCollection = database.getCollection("comments");

        loadAllUsers(usersCollection);
        loadAllUsers(listingsCollection);
        loadAllUsers(commentsCollection);

        addEmbeddingToAll();
    }


    private void addEmbeddingToAll() {

        if (usersCache.isEmpty() && listingCache.isEmpty() && commentsCache.isEmpty()) {
            System.out.println("Cache is empty,if there is data in database, load them.");
        } else {

            for (Map.Entry<User, List<ObjectId>> entry : userListingsMap.entrySet()) {
                User user = entry.getKey();
                List<ObjectId> listingsIds = entry.getValue();

                for  (ObjectId listingId : listingsIds) {
                    Listing listing = findListingById(listingId);
                    if (listing != null) {user.addMyListing(listing);}
                }
            }

            for (Map.Entry<User, List<ObjectId>> entry : userFavouriteListingsMap.entrySet()) {
                User user = entry.getKey();
                List<ObjectId> listingsIds = entry.getValue();

                for  (ObjectId listingId : listingsIds) {
                    Listing listing = findListingById(listingId);
                    if (listing != null){user.addFavourite(listing);}
                }
            }

            for (Map.Entry<User,List<ObjectId>> entry : userCommentsMap.entrySet()) {
                User user = entry.getKey();
                List<ObjectId> commentsIds = entry.getValue();

                for  (ObjectId commentId : commentsIds) {
                    Comment comment = findCommentById(commentId);
                    if (comment != null){user.addComment(comment);}
                }
            }

            for (Map.Entry<Comment,ObjectId> entry : commentUserMap.entrySet()) {
                Comment comment = entry.getKey();
                ObjectId userId = entry.getValue();

                User user = findUserById(userId);
                if (user != null){comment.setUser(user);}
            }

            for (Map.Entry<Comment,ObjectId> entry : commentListingMap.entrySet()) {
                Comment comment = entry.getKey();
                ObjectId listingId = entry.getValue();

                Listing listing = findListingById(listingId);
                if (listing != null) {comment.setListing(listing);}
            }

            for (Map.Entry<Listing,ObjectId> entry : listingOwnerMap.entrySet()) {
                Listing listing = entry.getKey();
                ObjectId ownerId = entry.getValue();

                User user = findUserById(ownerId);
                if (user != null){listing.setOwner(user);}
            }

            for (Map.Entry<Listing,List<ObjectId>> entry : listingCommentsMap.entrySet()) {
                Listing listing = entry.getKey();
                List<ObjectId> commentsIds = entry.getValue();

                for  (ObjectId commentId : commentsIds) {
                    Comment comment = findCommentById(commentId);
                    if (comment != null){listing.addComment(comment);}
                }
            }

        }



    }



    private void loadAllUsers(MongoCollection<Document> usersCollection) {

        FindIterable<Document> documents = usersCollection.find();

        for (Document doc : documents) {

            String username = doc.getString("username");
            String password = doc.getString("password");

            User user = new User(username, password);

            user.setId(doc.getObjectId("_id"));

            // Add user with no embedding.
            usersCache.add(user);

            // Save embedded info.
            userListingsMap.put(user,doc.getList("myListings",ObjectId.class));
            userFavouriteListingsMap.put(user,doc.getList("favouriteListings",ObjectId.class));
            userCommentsMap.put(user,doc.getList("myComments",ObjectId.class));
        }

    }


    private void loadAllListings(MongoCollection<Document> listingsCollection) {

        FindIterable<Document> documents = listingsCollection.find();

        for (Document doc : documents) {

            Listing listing = new Listing();

            listing.setId(doc.getObjectId("_id"));
            listing.setName(doc.getString("name"));
            listing.setOwnerId(doc.getObjectId("owner_id"));
            listing.setPhotoPath(doc.getString("photoPath"));
            listing.setTags(doc.getList("tags", String.class));
            listing.setMainCategories(doc.getList("mainCategories", String.class));
            listing.setDescription(doc.getString("description"));
            listing.setPrice(doc.getDouble("price"));
            listing.setAddress(doc.getString("address"));
            listing.setDistance(doc.getDouble("distance"));
            listing.setArea(doc.getDouble("area"));
            listing.setBedrooms(doc.getInteger("bedrooms"));
            listing.setBathrooms(doc.getInteger("bathrooms"));
            listing.setBuildingType(Listing.BuildingType.valueOf(doc.getString("buildingtype")));
            listing.setActive(doc.getBoolean("active"));

            // Add listings with no embedding
            listingCache.add(listing);

            // Save embedded info.
            listingOwnerMap.put(listing,doc.getObjectId("owner_id"));
            listingCommentsMap.put(listing,doc.getList("comments",ObjectId.class));
        }

    }


    private void loadAllComments(MongoCollection<Document> commentsCollection) {

        FindIterable<Document> documents = commentsCollection.find();

        for (Document doc : documents) {

            String id = doc.getString("id");
            String content = doc.getString("content");

            Comment comment = new Comment();

            comment.setIdForDB(doc.getObjectId("idForDB"));
            comment.setId(id);
            comment.setContent(content);
            comment.setCreatedAt(Instant.ofEpochSecond(doc.getLong("createdAt")));

            // Add comments with no embedding
            commentsCache.add(comment);

            // Save embedded info.
            commentUserMap.put(comment, doc.getObjectId("userId"));
            commentListingMap.put(comment, doc.getObjectId("listingId"));

        }
    }

    public User findUserById(ObjectId id) {
        for (User user : usersCache) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }

    public Listing findListingById(ObjectId id) {
        for (Listing listing : listingCache) {
            if (listing.getId().equals(id)) {
                return listing;
            }
        }
        return null;
    }

    public Comment findCommentById(ObjectId id) {
        for (Comment comment : commentsCache) {
            if (comment.getIdForDB().equals(id)) {
                return comment;
            }
        }
        return null;
    }


    public  ArrayList<User> getUsersCache() {
        return usersCache;
    }

    public  ArrayList<Listing> getListingsCache() {
        return listingCache;
    }

    public  ArrayList<Comment> getCommentsCache() {
        return commentsCache;
    }
}
