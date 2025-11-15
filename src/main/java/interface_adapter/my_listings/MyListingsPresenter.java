package interface_adapter.my_listings;

import use_case.my_listings.MyListingsOutputBoundary;
import use_case.my_listings.MyListingsOutputData;

public class MyListingsPresenter implements MyListingsOutputBoundary {
    private MyListingsViewModel viewModel;

    public MyListingsPresenter(MyListingsViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(MyListingsOutputData outputData) {
        MyListingsState state = new MyListingsState();
        state.setListings(outputData.getListings());
        state.setSuccess(true);
        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        MyListingsState state = new MyListingsState();
        state.setErrorMessage(errorMessage);
        state.setSuccess(false);
        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }
}