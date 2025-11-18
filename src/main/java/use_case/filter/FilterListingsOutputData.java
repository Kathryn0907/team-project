package use_case.filter;

import Entities.Listing;
import java.util.ArrayList;

public class FilterListingsOutputData {

    private final ArrayList<Listing> listings;

    public FilterListingsOutputData(ArrayList<Listing> listings) {
        // Defensive copy to avoid external mutation
        this.listings = listings != null
                ? new ArrayList<>(listings)
                : new ArrayList<>();
    }

    /**
     * Returns all listings that match the filter criteria.
     */
    public ArrayList<Listing> getListings() {
        return listings;
    }
}
