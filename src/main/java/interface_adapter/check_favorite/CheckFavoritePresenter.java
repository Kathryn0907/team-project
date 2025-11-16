package interface_adapter.check_favorite;

import interface_adapter.ViewManagerModel;
import use_case.check_favorite.CheckFavoriteOutputBoundary;
import use_case.check_favorite.CheckFavoriteOutputData;

public class CheckFavoritePresenter implements CheckFavoriteOutputBoundary {

    private final CheckFavoriteViewModel checkFavoriteViewModel;
    private final ViewManagerModel viewManagerModel;

    public CheckFavoritePresenter(ViewManagerModel viewManagerModel,
                                  CheckFavoriteViewModel checkFavoriteViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.checkFavoriteViewModel = checkFavoriteViewModel;
    }

    @Override
    public void prepareSuccessView(CheckFavoriteOutputData outputData) {
        CheckFavoriteState state = checkFavoriteViewModel.getState();

        state.setFavouriteListingNames(outputData.getFavouriteListingNames());
        state.setError(null);

        checkFavoriteViewModel.firePropertyChange();

        // Switch to the "Favourite Listings" view
        viewManagerModel.setState(checkFavoriteViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String error) {
        CheckFavoriteState state = checkFavoriteViewModel.getState();

        state.setError(error);
        state.setFavouriteListingNames(java.util.Collections.emptyList());

        checkFavoriteViewModel.firePropertyChange();
    }
}
