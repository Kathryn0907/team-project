package use_case.create_listing;

import Entities.Listing;

public class CreateListingOutputData {

    private final Listing listing;

    public CreateListingOutputData(Listing listing) {
        this.listing = listing;
    }

    public Listing getListing() {
        return listing;
    }
}
