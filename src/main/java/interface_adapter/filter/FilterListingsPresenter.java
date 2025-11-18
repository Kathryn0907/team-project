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
        SearchListingState state = new SearchListingState();
        state.setSuccess(true);
        state.setErrorMessage(null);
        state.setListings(outputData.getListings());

        searchViewModel.setState(state);
    }

    @Override
    public void presentError(String errorMessage) {
        SearchListingState state = new SearchListingState();
        state.setSuccess(false);
        state.setErrorMessage(errorMessage);
        state.setListings(new ArrayList<>());

        searchViewModel.setState(state);
    }
}
