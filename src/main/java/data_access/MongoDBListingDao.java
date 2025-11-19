package data_access;

import Entities.Listing;
import Entities.User;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;


public class MongoDBListingDao {

    private final MongoCollection<Document> collection;

    public MongoDBListingDao() {
        MongoClient client = MongoConfig.getClient();
        MongoDatabase db = client.getDatabase("CSC207_group_project_2025");
        this.collection = db.getCollection("Listings");
    }

    /**
     * Save the listing to MongoDB will assign this listing an ObjectId.
     * @param listing The listing.
     */
    public void saveListing(Listing listing) {

        Document listingDocument = new Document()
                .append("id", listing.getId())
                .append("name", listing.getName())
                .append("owner", listing.getOwner().getId())
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
                .append("buildingType", listing.getBuildingType().name()) // May have error.
                .append("active", listing.isActive());

        collection.insertOne(listingDocument);

    }


    /**
     * Find the listing by Id.
     * @param listingId The Id of the listing.
     * @return Null if didn't find the listing.
     */
    public Listing findListing(ObjectId listingId) {

        Document doc = collection.find(Filters.eq("id", listingId)).first();

        if (doc != null) {
            Listing listing = new Listing();

            listing.setId(listingId);
            listing.setName(doc.getString("name"));

            ObjectId ownerId = doc.getObjectId("owner");
            User user = new MongoDBUserDAO().findUser(ownerId);
            listing.setOwner(user);

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

            return listing;
        }
        else {return null;}

    }




}
