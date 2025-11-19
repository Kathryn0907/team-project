package interface_adapter.filter;

import interface_adapter.search_listings.SearchListingState;
import interface_adapter.search_listings.SearchListingViewModel;
import use_case.filter.FilterListingsOutputBoundary;
import use_case.filter.FilterListingsOutputData;

import java.util.ArrayList;

public class FilterListingsPresenter implements FilterListingsOutputBoundary {

    private final SearchListingViewModel searchViewModel;

    public FilterListingsPresenter(SearchListingViewModel searchViewModel) {
        this.searchViewModel = searchViewModel;
    }

    @Override
    public void present(FilterListingsOutputData outputData) {
        // Get the EXISTING state instead of creating a new one
        SearchListingState state = searchViewModel.getState();

        state.setSuccess(true);
        state.setErrorMessage(null);
        state.setListings(outputData.getListings());

        searchViewModel.setState(state);
        searchViewModel.firePropertyChanged();  // Notify the view
    }

    @Override
    public void presentError(String errorMessage) {
        // Get the EXISTING state instead of creating a new one
        SearchListingState state = searchViewModel.getState();

        state.setSuccess(false);
        state.setErrorMessage(errorMessage);
        state.setListings(new ArrayList<>());

        searchViewModel.setState(state);
        searchViewModel.firePropertyChanged();  // Notify the view
    }
}