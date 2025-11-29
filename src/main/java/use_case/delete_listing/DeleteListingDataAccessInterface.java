package use_case.delete_listing;

import Entities.Listing;
import org.bson.types.ObjectId;

public interface DeleteListingDataAccessInterface {

    Listing findListingById(ObjectId listingId);

    void deleteListing(Listing listing);

    void removeListingFromUser(ObjectId ownerId, Listing listing);
}
