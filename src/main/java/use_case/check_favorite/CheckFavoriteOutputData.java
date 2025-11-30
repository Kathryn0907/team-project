package use_case.check_favorite;

import Entities.Listing;
import java.util.List;

public class CheckFavoriteOutputData {
    private final List<Listing> favouriteListings;

    public CheckFavoriteOutputData(List<Listing> favouriteListings) {
        this.favouriteListings = favouriteListings;
    }

    public List<Listing> getFavouriteListings() {
        return favouriteListings;
    }
}
