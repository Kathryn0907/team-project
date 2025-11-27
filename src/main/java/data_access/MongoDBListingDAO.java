package data_access;

import Entities.Comment;
import Entities.Listing;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;
import use_case.create_listing.CreateListingDataAccessInterface;
import use_case.filter.FilterListingsDataAccessInterface;
import use_case.search_listings.SearchListingDataAccessInterface;

import java.util.ArrayList;
import java.util.List;


public class MongoDBListingDAO implements CreateListingDataAccessInterface,
        FilterListingsDataAccessInterface,
        SearchListingDataAccessInterface {

    private final MongoCollection<Document> listingsCollection;
    private MongoDBExtractToCache data;

    public MongoDBListingDAO() {
        this.data = new MongoDBExtractToCache();
        this.listingsCollection = data.getDatabase().getCollection("Listings");
    }

    /**
     * Save or update the listing in Database.
     * To update, make sure the id is correct.
     * @param listing The listing.
     */
    public void saveListing(Listing listing) {

        // Check if the Listing is already in database. If yes, delete that Listing
        // so this will renew the data.
        if (listingsCollection.find(Filters.eq("id", listing.getId())).first() != null ) {
            listingsCollection.deleteOne(Filters.eq("id", listing.getId()));
        } else if (listingsCollection.find(Filters.eq("name", listing.getName())).first() != null) {
            listingsCollection.deleteOne(Filters.eq("name", listing.getName()));
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

        listingsCollection.insertOne(listingDocument);

    }

    /**
    @Override
    // this is temporary to satisfy the interface requirement, will move to saveListing later after verifying everything works
    public void save(Listing listing) {
        saveListing(listing);
    }
    **/

    public void deleteListing(Listing listing) {
        ObjectId listingId = listing.getId();
        if (listingsCollection.find(Filters.eq("id", listingId)).first() != null) {
            listingsCollection.deleteOne(Filters.eq("id", listingId));
        } else {
            System.out.println("Listing with id: " + listingId + " and name: "
                    + listing.getName() + " not found in Database");
        }
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

    /**
     * Refresh the local cache.
     */
    public void refreshData() {
        this.data = new MongoDBExtractToCache();
    }

    /**
     * Get all listings in local cache. You can use refresh method if you just
     * save something but didn't find it.
     * @return A list of listings.
     */
    public ArrayList<Listing> getAllListings() {
        return data.getListingsCache();
    }


    //-------------------- Override Methods ----------------------

    @Override
    public void save(Listing listing) {
        saveListing(listing);
    }

    @Override
    public ArrayList<Listing> getAllActiveListings() {
        ArrayList<Listing> listings = new ArrayList<>();
        for (Listing listing : getAllListings()) {
            if (listing.isActive()) {
                listings.add(listing);
            }
        }
        return listings;
    }
}
