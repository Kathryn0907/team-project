package use_case.search_listings;

public interface SearchListingInputBoundary {
    SearchListingOutputData execute(use_case.search_listings.SearchListingInputData inputData);
}