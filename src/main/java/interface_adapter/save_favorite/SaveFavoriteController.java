package interface_adapter.save_favorite;

import use_case.save_favorite.SaveFavoriteInputBoundary;
import use_case.save_favorite.SaveFavoriteInputData;

public class SaveFavoriteController {

    private final SaveFavoriteInputBoundary saveFavoriteUseCaseInteractor;

    public SaveFavoriteController(SaveFavoriteInputBoundary saveFavoriteUseCaseInteractor) {
        this.saveFavoriteUseCaseInteractor = saveFavoriteUseCaseInteractor;
    }

    /**
     * Called by the View when the user clicks "Add to Favorites".
     */
    public void addToFavorites(String username, String listingName) {
        SaveFavoriteInputData inputData =
                new SaveFavoriteInputData(username, listingName);
        saveFavoriteUseCaseInteractor.execute(inputData);
    }
}
