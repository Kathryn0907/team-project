package interface_adapter.listing_detail;

import Entities.Listing;
import Entities.User;

public class ListingDetailState {
    private Listing currentListing;
    private User currentUser;
    private String errorMessage;

    public Listing getCurrentListing() {
        return currentListing;
    }

    public void setCurrentListing(Listing currentListing) {
        this.currentListing = currentListing;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
