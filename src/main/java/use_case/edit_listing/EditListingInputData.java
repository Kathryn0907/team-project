package use_case.edit_listing;

import Entities.Listing;

public class EditListingInputData {
    private final Listing listing;

    public EditListingInputData(Listing listing) {
        this.listing = listing;
    }

    public Listing getListing() {
        return listing;
    }
}