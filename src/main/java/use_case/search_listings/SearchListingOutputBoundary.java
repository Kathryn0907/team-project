package use_case.search_listings;

public interface SearchListingOutputBoundary {
    void prepareSuccessView(SearchListingOutputData outputData);
    void prepareFailView(SearchListingOutputData outputData);  // Changed: now takes outputData
}