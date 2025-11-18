package use_case.filter;

import Entities.Listing;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements the Filter Listings use case.
 */
public class FilterListingsInteractor implements FilterListingsInputBoundary {

    private final FilterListingsDataAccessInterface listingDataAccess;
    private final DistanceService distanceService;
    private final FilterListingsOutputBoundary presenter;

    public FilterListingsInteractor(FilterListingsDataAccessInterface listingDataAccess,
                                    DistanceService distanceService,
                                    FilterListingsOutputBoundary presenter) {
        this.listingDataAccess = listingDataAccess;
        this.distanceService = distanceService;
        this.presenter = presenter;
    }

    @Override
    public void execute(FilterListingsInputData inputData) {

        // ------- Basic validation -------
        if (inputData.getMinPrice() != null &&
                inputData.getMaxPrice() != null &&
                inputData.getMinPrice() > inputData.getMaxPrice()) {

            presenter.presentError("Min price cannot be greater than max price.");
            return;
        }

        if (inputData.getMinArea() != null &&
                inputData.getMaxArea() != null &&
                inputData.getMinArea() > inputData.getMaxArea()) {

            presenter.presentError("Min area cannot be greater than max area.");
            return;
        }

        // ------- Get all candidate listings -------
        List<Listing> allActiveListings = listingDataAccess.getAllActiveListings();
        ArrayList<Listing> results = new ArrayList<>();

        for (Listing listing : allActiveListings) {

            // Defensive: skip inactive listings
            if (!listing.isActive()) {
                continue;
            }

            // ------- Building type filter -------
            if (inputData.getBuildingType() != null &&
                    listing.getBuildingType() != inputData.getBuildingType()) {
                continue;
            }

            // ------- Price filter -------
            double price = listing.getPrice();
            if (inputData.getMinPrice() != null &&
                    price < inputData.getMinPrice()) {
                continue;
            }
            if (inputData.getMaxPrice() != null &&
                    price > inputData.getMaxPrice()) {
                continue;
            }

            // ------- Area filter -------
            double area = listing.getArea();
            if (inputData.getMinArea() != null &&
                    area < inputData.getMinArea()) {
                continue;
            }
            if (inputData.getMaxArea() != null &&
                    area > inputData.getMaxArea()) {
                continue;
            }

            // ------- Bedrooms filter -------
            if (inputData.getMinBedrooms() != null &&
                    listing.getBedrooms() < inputData.getMinBedrooms()) {
                continue;
            }

            // ------- Bathrooms filter -------
            if (inputData.getMinBathrooms() != null &&
                    listing.getBathrooms() < inputData.getMinBathrooms()) {
                continue;
            }

            // ------- Distance filter -------
            if (inputData.getUserAddress() != null &&
                    !inputData.getUserAddress().trim().isEmpty() &&
                    inputData.getMaxDistanceKm() != null) {

                String listingAddress = listing.getAddress();
                if (listingAddress == null || listingAddress.trim().isEmpty()) {
                    // can't compute distance, skip if user requested distance filtering
                    continue;
                }

                double distanceKm = distanceService.calculateDistanceKm(
                        inputData.getUserAddress(),
                        listingAddress
                );

                // optional: store computed distance back into the entity
                listing.setDistance(distanceKm);

                if (distanceKm > inputData.getMaxDistanceKm()) {
                    continue;
                }
            }

            // If we reach here, listing passed all filters
            results.add(listing);
        }

        FilterListingsOutputData outputData = new FilterListingsOutputData(results);
        presenter.present(outputData);
    }
}
