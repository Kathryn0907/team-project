package app;

import interface_adapter.filter.FilterListingsController;
import interface_adapter.filter.FilterListingsPresenter;
import interface_adapter.search_listings.SearchListingViewModel;
import use_case.filter.DistanceService;
import use_case.filter.FilterListingsDataAccessInterface;
import use_case.filter.FilterListingsInputBoundary;
import use_case.filter.FilterListingsInteractor;
import use_case.filter.FilterListingsOutputBoundary;

public class FilterListingsUseCaseFactory {

    private FilterListingsUseCaseFactory() {}

    public static FilterListingsController create(
            SearchListingViewModel searchViewModel,
            FilterListingsDataAccessInterface listingDataAccess,
            DistanceService distanceService) {

        FilterListingsOutputBoundary presenter =
                new FilterListingsPresenter(searchViewModel);

        FilterListingsInputBoundary interactor =
                new FilterListingsInteractor(listingDataAccess, distanceService, presenter);

        return new FilterListingsController(interactor);
    }
}
