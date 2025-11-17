package use_case.search_listings;

import Entities.Listing;
import java.util.ArrayList;

public class SearchListingOutputData {
    private final ArrayList<Listing> listings;
    private final String errorMessage;
    private final boolean success;

    public SearchListingOutputData(ArrayList<Listing> listings, String errorMessage, boolean success) {
        this.listings = listings;
        this.errorMessage = errorMessage;
        this.success = success;
    }

    public ArrayList<Listing> getListings() {
        return listings;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }
}
