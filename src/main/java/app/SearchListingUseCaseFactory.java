package app;

import data_access.InMemoryListingDAO;
import interface_adapter.search_listings.*;
import use_case.search_listings.*;
import interface_adapter.search_listings.SearchListingController;
import use_case.search_listings.SearchListingInputBoundary;

public class SearchListingUseCaseFactory {

    public static SearchListingController createSearchListingUseCase(
            SearchListingViewModel viewModel,
            InMemoryListingDAO dataAccess) {

        SearchListingOutputBoundary presenter = new SearchListingPresenter(viewModel);
        SearchListingInputBoundary interactor = new SearchListingInteractor(dataAccess, presenter);
        return new SearchListingController(interactor);
    }
}