package interface_adapter.search_listings;

import use_case.search_listings.SearchListingInputBoundary;
import use_case.search_listings.SearchListingInputData;
import use_case.search_listings.SearchListingOutputData;
import Entities.Listing;
import java.util.ArrayList;

public class SearchListingController {
    private final SearchListingInputBoundary interactor;

    public SearchListingController(SearchListingInputBoundary interactor) {
        this.interactor = interactor;
    }

    public SearchListingOutputData execute(String keyword, ArrayList<Listing> preFilteredListings) {
        SearchListingInputData inputData = new SearchListingInputData(keyword, preFilteredListings);
        return interactor.execute(inputData);
    }
}