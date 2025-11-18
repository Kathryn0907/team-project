package interface_adapter.check_favorite;

import use_case.check_favorite.CheckFavoriteInputBoundary;
import use_case.check_favorite.CheckFavoriteInputData;

public class CheckFavoriteController {

    private final CheckFavoriteInputBoundary checkFavoriteUseCaseInteractor;

    public CheckFavoriteController(CheckFavoriteInputBoundary checkFavoriteUseCaseInteractor) {
        this.checkFavoriteUseCaseInteractor = checkFavoriteUseCaseInteractor;
    }

    /**
     * Called when the user clicks "Favourite Listings" in their profile.
     */
    public void loadFavouriteListings(String username) {
        CheckFavoriteInputData inputData = new CheckFavoriteInputData(username);
        checkFavoriteUseCaseInteractor.execute(inputData);
    }
}
