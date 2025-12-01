package interface_adapter.check_favorite;

import Entities.Listing;
import java.util.ArrayList;
import java.util.List;

public class CheckFavoriteState {

    private List<Listing> favouriteListings = new ArrayList<>();
    private String error;

    public CheckFavoriteState() {}

    public List<Listing> getFavouriteListings() {
        return favouriteListings;
    }

    public void setFavouriteListings(List<Listing> favouriteListings) {
        this.favouriteListings = favouriteListings;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    // OPTIONAL helper if you still want names:
    public List<String> getFavouriteListingNames() {
        List<String> names = new ArrayList<>();
        for (Listing l : favouriteListings) {
            if (l != null) {
                names.add(l.getName());
            }
        }
        return names;
    }
}
