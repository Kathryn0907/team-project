package interface_adapter.delete_listing;

import org.bson.types.ObjectId;
import use_case.delete_listing.DeleteListingInputBoundary;
import use_case.delete_listing.DeleteListingInputData;

public class DeleteListingController {

    private final DeleteListingInputBoundary interactor;

    public DeleteListingController(DeleteListingInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(ObjectId listingId, ObjectId ownerId) {
        interactor.execute(new DeleteListingInputData(listingId, ownerId));
    }
}
