package use_case.check_favorite;

import java.util.List;

/**
 * Output Data for the Check Favorite Use Case.
 */
public class CheckFavoriteOutputData {
    private final List<String> favouriteListingNames;

    public CheckFavoriteOutputData(List<String> favouriteListingNames) {
        this.favouriteListingNames = favouriteListingNames;
    }

    public List<String> getFavouriteListingNames() {
        return favouriteListingNames;
    }
}