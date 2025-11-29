package use_case.edit_listing;

import Entities.Listing;

public interface EditListingDataAccessInterface {
    void update(Listing listing);
}