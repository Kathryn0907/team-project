package use_case.save_favorite

public interface SaveFavoriteDataAccessInterface {
    User getUser(String username);
    Listing getListingByName(String listingName);
    void saveUser(User user);   // to persist updated favorites
}