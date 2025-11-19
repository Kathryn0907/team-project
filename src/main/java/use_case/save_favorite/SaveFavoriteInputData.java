package use_case.save_favorite;

/**
 * Input Data for the Save Favorite Use Case.
 */
public class SaveFavoriteInputData {
    private final String username;
    private final String listingName;

    public SaveFavoriteInputData(String username, String listingName) {
        this.username = username;
        this.listingName = listingName;
    }

    public String getUsername() {
        return username;
    }

    public String getListingName() {
        return listingName;
    }
}