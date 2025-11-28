package use_case.delete_listing;

import org.bson.types.ObjectId;

public class DeleteListingOutputData {

    private final ObjectId listingId;

    public DeleteListingOutputData(ObjectId listingId) {
        this.listingId = listingId;
    }

    public ObjectId getListingId() {
        return listingId;
    }
}