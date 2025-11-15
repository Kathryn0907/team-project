package interface_adapter.check_favorite;

import java.util.ArrayList;
import java.util.List;

public class CheckFavoriteState {

    private List<String> favouriteListingNames = new ArrayList<>();
    private String error;

    public CheckFavoriteState() {}

    public List<String> getFavouriteListingNames() {
        return favouriteListingNames;
    }

    public void setFavouriteListingNames(List<String> favouriteListingNames) {
        this.favouriteListingNames = favouriteListingNames;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
