package interface_adapter.edit_listing;

import Entities.Listing;
import use_case.edit_listing.EditListingInputBoundary;
import use_case.edit_listing.EditListingInputData;

public class EditListingController {

    private final EditListingInputBoundary interactor;

    public EditListingController(EditListingInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void saveEdits(Listing listing) {
        EditListingInputData data = new EditListingInputData(listing);
        interactor.saveEdits(data);
    }
}