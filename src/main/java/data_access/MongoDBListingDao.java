package data_access;

import Entities.Listing;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import static com.mongodb.client.model.Filters.eq;

public class MongoDBListingDao {

    private final MongoCollection<Document> collection;

    public MongoDBListingDao() {
        MongoClient client = MongoConfig.getClient();
        MongoDatabase db = client.getDatabase("CSC207_group_project_2025");
        this.collection = db.getCollection("listings");
    }

    /**
     * Save the listing to MongoDB will assign this listing an ObjectId.
     * @param listing The listing.
     */
    public void saveListing(Listing listing) {

        String listingId = new ObjectId().toString();
        listing.setId(listingId);

        Document listingDocument = new Document()
                .append("id", listingId)
                .append("name", listing.getName())
                .append("description", listing.getDescription())
                ;
    }



}
