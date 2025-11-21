package use_case.cancel_account;
import Entities.Listing;
import Entities.User;

import java.util.ArrayList;

public interface CancelAccountDataAccessInterface {


    boolean existByUsername(String username);


    /**
     * Cancel the account by username.
     * Note: The Listings belongs to this user is also deleted (or deactivated)
     * @param username username
     */
    void cancelAccount(String username);

    User getUser(String username);

    void addUser(User user);

    ArrayList<Listing> getAllActiveListings();

    void addListing(Listing listing);
}