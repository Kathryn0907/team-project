package use_case.create_listing;

import Entities.Listing;
import Entities.User;

public interface CreateListingDataAccessInterface {

    /**
     * Saves the listing
     * @param listing the listing to save
     */

    void save(Listing listing);

    void addListingToUser(User user, Listing listing);
}
