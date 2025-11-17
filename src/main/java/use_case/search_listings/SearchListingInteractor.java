package use_case.search_listings;

import Entities.Listing;
import java.util.ArrayList;

public class SearchListingInteractor implements use_case.search_listings.SearchListingInputBoundary {
    private final SearchListingDataAccessInterface dataAccess;
    private final SearchListingOutputBoundary presenter;

    public SearchListingInteractor(SearchListingDataAccessInterface dataAccess,
                                   SearchListingOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public SearchListingOutputData execute(SearchListingInputData inputData) {
        String keyword = inputData.getKeyword();

        if (keyword == null || keyword.trim().isEmpty()) {
            SearchListingOutputData outputData = new SearchListingOutputData(new ArrayList<>(), "Please enter a valid keyword", false);
            presenter.prepareFailView(outputData);  // Pass outputData, not just message
            return outputData;
        }

        String normalizedKeyword = keyword.trim().toLowerCase();

        ArrayList<Listing> listingsToSearch = inputData.getPreFilteredListings() != null ?
                inputData.getPreFilteredListings() : dataAccess.getAllActiveListings();

        ArrayList<ListingMatch> matches = new ArrayList<>();
        for (Listing listing : listingsToSearch) {
            int relevanceScore = calculateRelevance(listing, normalizedKeyword);
            if (relevanceScore > 0) {
                matches.add(new ListingMatch(listing, relevanceScore));
            }
        }

        matches.sort((a, b) -> Integer.compare(b.relevanceScore, a.relevanceScore));

        ArrayList<Listing> results = new ArrayList<>();
        for (ListingMatch match : matches) {
            results.add(match.listing);
        }

        if (results.isEmpty()) {
            String message = "Sorry, not familiar with \"" + keyword + "\". Please use another keyword.";
            SearchListingOutputData outputData = new SearchListingOutputData(listingsToSearch, message, false);
            presenter.prepareFailView(outputData);  // Pass outputData, not just message
            return outputData;
        }

        SearchListingOutputData outputData = new SearchListingOutputData(results, null, true);
        presenter.prepareSuccessView(outputData);
        return outputData;
    }

    private int calculateRelevance(Listing listing, String keyword) {
        int score = 0;

        if (listing.getName().toLowerCase().equals(keyword)) {
            score += 100;
        } else if (listing.getName().toLowerCase().contains(keyword)) {
            score += 50;
        }

        for (String category : listing.getMainCategories()) {
            if (category.toLowerCase().contains(keyword)) {
                score += 30;
            }
        }

        for (String tag : listing.getTags()) {
            if (tag.toLowerCase().contains(keyword)) {
                score += 20;
            }
        }

        if (listing.getAddress().toLowerCase().contains(keyword)) {
            score += 15;
        }

        return score;
    }

    private static class ListingMatch {
        final Listing listing;
        final int relevanceScore;

        ListingMatch(Listing listing, int relevanceScore) {
            this.listing = listing;
            this.relevanceScore = relevanceScore;
        }
    }
}