package interface_adapter.save_favorite;

public class SaveFavoriteState {

    private String listingName;
    private boolean alreadyInFavourites;
    private String message;
    private String error;

    public SaveFavoriteState() {}

    public String getListingName() {
        return listingName;
    }

    public void setListingName(String listingName) {
        this.listingName = listingName;
    }

    public boolean isAlreadyInFavourites() {
        return alreadyInFavourites;
    }

    public void setAlreadyInFavourites(boolean alreadyInFavourites) {
        this.alreadyInFavourites = alreadyInFavourites;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
