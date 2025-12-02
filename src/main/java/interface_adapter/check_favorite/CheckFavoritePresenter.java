package interface_adapter.check_favorite;

import interface_adapter.ViewManagerModel;
import use_case.check_favorite.CheckFavoriteOutputBoundary;
import use_case.check_favorite.CheckFavoriteOutputData;

import java.util.Collections;

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

        state.setFavouriteListings(outputData.getFavouriteListings());
        state.setError(null);

        checkFavoriteViewModel.setState(state);
        checkFavoriteViewModel.firePropertyChange();

        viewManagerModel.setState(CheckFavoriteViewModel.VIEW_NAME);
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String error) {
        CheckFavoriteState state = checkFavoriteViewModel.getState();

        state.setError(error);
        state.setFavouriteListings(Collections.emptyList());

        checkFavoriteViewModel.setState(state);
        checkFavoriteViewModel.firePropertyChange();

        viewManagerModel.setState(CheckFavoriteViewModel.VIEW_NAME);
        viewManagerModel.firePropertyChange();
    }
}
