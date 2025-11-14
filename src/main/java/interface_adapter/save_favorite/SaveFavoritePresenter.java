package interface_adapter.save_favorite;

import use_case.save_favorite.SaveFavoriteOutputBoundary;
import use_case.save_favorite.SaveFavoriteOutputData;

public class SaveFavoritePresenter implements SaveFavoriteOutputBoundary {

    private final SaveFavoriteViewModel saveFavoriteViewModel;

    public SaveFavoritePresenter(SaveFavoriteViewModel saveFavoriteViewModel) {
        this.saveFavoriteViewModel = saveFavoriteViewModel;
    }

    @Override
    public void prepareSuccessView(SaveFavoriteOutputData outputData) {
        SaveFavoriteState state = saveFavoriteViewModel.getState();

        state.setListingName(outputData.getListingName());
        state.setAlreadyInFavourites(outputData.isAlreadyInFavourites());

        if (outputData.isAlreadyInFavourites()) {
            state.setMessage("Listing \"" + outputData.getListingName()
                    + "\" is already in your favourites.");
        } else {
            state.setMessage("Listing \"" + outputData.getListingName()
                    + "\" was added to favourites.");
        }
        state.setError(null);

        saveFavoriteViewModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String error) {
        SaveFavoriteState state = saveFavoriteViewModel.getState();

        state.setError(error);
        state.setMessage(null);

        saveFavoriteViewModel.firePropertyChange();
    }
}
