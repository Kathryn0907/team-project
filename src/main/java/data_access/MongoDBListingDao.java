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


public class MongoDBListingDao {

    private final MongoCollection<Document> collection;
    private MongoDBExtractToCache data;

    public MongoDBListingDao() {
        MongoClient client = MongoConfig.getClient();
        MongoDatabase db = client.getDatabase("CSC207_group_project_2025");
        this.collection = db.getCollection("Listings");
        this.data = new MongoDBExtractToCache();
    }

    /**
     * Save the listing to MongoDB will assign this listing an ObjectId.
     * @param listing The listing.
     */
    public void saveListing(Listing listing) {

        // Check if the Listing is already in database. If yes, delete that Listing
        // so this will renew the data.
        if (collection.find(Filters.eq("id", listing.getId())).first() != null ) {
            collection.deleteOne(Filters.eq("id", listing.getId()));
        }


        ArrayList<ObjectId> commentsIds = new ArrayList<>();
        for (Comment comment : listing.getComments()) {
            commentsIds.add(comment.getIdForDB());
        }

        Document listingDocument = new Document()
                .append("id", listing.getId())
                .append("name", listing.getName())
                .append("owner_id", listing.getOwner().getId())
                .append("photoPath", listing.getPhotoPath())
                .append("tags", listing.getTags())
                .append("mainCategories", listing.getMainCategories())
                .append("description", listing.getDescription())
                .append("price", listing.getPrice())
                .append("address", listing.getAddress())
                .append("distance", listing.getDistance())
                .append("area", listing.getArea())
                .append("bedrooms", listing.getBedrooms())
                .append("bathrooms", listing.getBathrooms())
                .append("buildingType", listing.getBuildingType().name())
                .append("active", listing.isActive())
                .append("comments", commentsIds);

        collection.insertOne(listingDocument);

    }

    /**
     * Find the listing in local cache. You can use refresh method if you just
     * save something but didn't find it.
     * @param listingId The id of the listing.
     * @return null if not found.
     */
    public Listing findListingById(ObjectId listingId) {
        return data.findListingById(listingId);
    }

    public void refreshData() {
        this.data = new MongoDBExtractToCache();
    }

}
