package use_case.search_listings;

import Entities.Listing;
import java.util.ArrayList;

public class SearchListingInteractor implements SearchListingInputBoundary {

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

        // Base set to search: pre-filtered list if provided, otherwise all active listings
        ArrayList<Listing> listingsToSearch =
                inputData.getPreFilteredListings() != null
                        ? inputData.getPreFilteredListings()
                        : dataAccess.getAllActiveListings();

        // Case 1: empty / null keyword → just return the base list
        if (keyword == null || keyword.trim().isEmpty()) {
            SearchListingOutputData outputData =
                    new SearchListingOutputData(listingsToSearch, null, true);
            presenter.prepareSuccessView(outputData);
            return outputData;
        }

        // Normalize keyword and perform relevance-based search
        String normalizedKeyword = keyword.trim().toLowerCase();

        ArrayList<ListingMatch> matches = new ArrayList<>();
        for (Listing listing : listingsToSearch) {
            int relevanceScore = calculateRelevance(listing, normalizedKeyword);
            if (relevanceScore > 0) {
                matches.add(new ListingMatch(listing, relevanceScore));
            }
        }

        // Sort by relevance (descending)
        matches.sort((a, b) -> Integer.compare(b.relevanceScore, a.relevanceScore));

        // Extract the sorted Listing objects
        ArrayList<Listing> results = new ArrayList<>();
        for (ListingMatch match : matches) {
            results.add(match.listing);
        }

        // No results → fail view with fallback (original list)
        if (results.isEmpty()) {
            String message = "Sorry, not familiar with \"" + keyword + "\". Please use another keyword.";
            SearchListingOutputData outputData =
                    new SearchListingOutputData(listingsToSearch, message, false);
            presenter.prepareFailView(outputData);
            return outputData;
        }

        // Success
        SearchListingOutputData outputData =
                new SearchListingOutputData(results, null, true);
        presenter.prepareSuccessView(outputData);
        return outputData;
    }

    /**
     * Token-based relevance:
     * - Split keyword into tokens: "beach house" → ["beach", "house"]
     * - Each token can match: name, categories, tags, address
     * - Exact name match gets a big bonus so it floats to the top.
     */
    private int calculateRelevance(Listing listing, String normalizedKeyword) {
        int score = 0;

        String[] tokens = normalizedKeyword.split("\\s+");

        String name = listing.getName() != null
                ? listing.getName().toLowerCase()
                : "";
        String address = listing.getAddress() != null
                ? listing.getAddress().toLowerCase()
                : "";

        for (String token : tokens) {
            boolean tokenMatched = false;

            // Name matching
            if (name.contains(token)) {
                score += 30;
                tokenMatched = true;
            }

            // Main categories
            if (!listing.getMainCategories().isEmpty()) {
                for (String category : listing.getMainCategories()) {
                    if (category != null && category.toLowerCase().contains(token)) {
                        score += 20;
                        tokenMatched = true;
                        break; // no need to check more categories for this token
                    }
                }
            }

            // Tags (only if not already matched by categories)
            if (!tokenMatched && !listing.getTags().isEmpty()) {
                for (String tag : listing.getTags()) {
                    if (tag.toLowerCase().contains(token)) {
                        score += 15;
                        tokenMatched = true;
                        break;
                    }
                }
            }

            // Address (can stack with name/categories/tags)
            if (address.contains(token)) {
                score += 10;
            }
        }

        // Exact whole-name match bonus (e.g., "beach house" == "Beach House")
        if (!name.isEmpty() && name.equals(normalizedKeyword)) {
            score += 100;
        }

        return score;
    }

    /**
     * Helper class to hold a listing with its computed relevance score.
     */
    private static class ListingMatch {
        final Listing listing;
        final int relevanceScore;

        ListingMatch(Listing listing, int relevanceScore) {
            this.listing = listing;
            this.relevanceScore = relevanceScore;
        }
    }
}
