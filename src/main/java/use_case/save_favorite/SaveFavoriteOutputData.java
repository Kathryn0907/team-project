package use_case.save_favorite;

/**
 * Output Data for the Save Favorite Use Case.
 */
public class SaveFavoriteOutputData {
    private final String listingName;
    private final boolean alreadyInFavourites;

    public SaveFavoriteOutputData(String listingName, boolean alreadyInFavourites) {
        this.listingName = listingName;
        this.alreadyInFavourites = alreadyInFavourites;
    }

    public String getListingName() {
        return listingName;
    }

    public boolean isAlreadyInFavourites() {
        return alreadyInFavourites;
    }
}