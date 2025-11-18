package interface_adapter.filter;

import Entities.Listing;
import use_case.filter.FilterListingsInputBoundary;
import use_case.filter.FilterListingsInputData;

public class FilterListingsController {

    private final FilterListingsInputBoundary interactor;

    public FilterListingsController(FilterListingsInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void applyFilter(String userAddress,
                            Double maxDistanceKm,
                            Double minPrice,
                            Double maxPrice,
                            Double minArea,
                            Double maxArea,
                            Integer minBedrooms,
                            Integer minBathrooms,
                            Listing.BuildingType buildingType) {

        FilterListingsInputData inputData = new FilterListingsInputData(
                userAddress,
                maxDistanceKm,
                minPrice,
                maxPrice,
                minArea,
                maxArea,
                minBedrooms,
                minBathrooms,
                buildingType
        );

        interactor.execute(inputData);
    }
}
