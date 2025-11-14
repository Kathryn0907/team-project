package use_case.create_listing;

import Entities.Listing;

public interface CreateListingDataAccessInterface {

    /**
     * Saves the listing
     * @param listing the listing to save
     */

    void save(Listing listing);
}
