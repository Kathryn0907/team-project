package use_case.edit_listing;

import Entities.Listing;

public class EditListingOutputData {
    private final Listing updatedListing;

    public EditListingOutputData(Listing updatedListing) {
        this.updatedListing = updatedListing;
    }

    public Listing getUpdatedListing() {
        return updatedListing;
    }
}