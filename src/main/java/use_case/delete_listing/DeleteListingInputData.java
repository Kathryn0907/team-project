package use_case.delete_listing;

import org.bson.types.ObjectId;

public class DeleteListingInputData {
    private final ObjectId listingId;
    private final ObjectId ownerId;

    public DeleteListingInputData(ObjectId listingId, ObjectId ownerId) {
        this.listingId = listingId;
        this.ownerId = ownerId;
    }

    public ObjectId getListingId() { return listingId; }
    public ObjectId getOwnerId() { return ownerId; }
}