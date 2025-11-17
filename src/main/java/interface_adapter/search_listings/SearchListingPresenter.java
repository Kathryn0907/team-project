package interface_adapter.search_listings;

import use_case.search_listings.SearchListingOutputBoundary;
import use_case.search_listings.SearchListingOutputData;
import java.util.ArrayList;

public class SearchListingPresenter implements SearchListingOutputBoundary {
    private SearchListingViewModel viewModel;

    public SearchListingPresenter(SearchListingViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(SearchListingOutputData outputData) {
        SearchListingState state = new SearchListingState();
        state.setListings(outputData.getListings());
        state.setSuccess(true);
        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(SearchListingOutputData outputData) {
        SearchListingState state = new SearchListingState();
        state.setErrorMessage(outputData.getErrorMessage());
        state.setListings(outputData.getListings());  // Now we have the fallback listings!
        state.setSuccess(false);
        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }
}