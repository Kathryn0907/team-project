package use_case.search_listings;

import Entities.Listing;
import java.util.ArrayList;

public class SearchListingInputData {
    private final String keyword;
    private final ArrayList<Listing> preFilteredListings;

    public SearchListingInputData(String keyword, ArrayList<Listing> preFilteredListings) {
        this.keyword = keyword;
        this.preFilteredListings = preFilteredListings;
    }

    public String getKeyword() {
        return keyword;
    }

    public ArrayList<Listing> getPreFilteredListings() {
        return preFilteredListings;
    }
}
