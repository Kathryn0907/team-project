package use_case.save_favorite;

import Entities.User;
import Entities.Listing;

/**
 * The Save Favorite Interactor.
 */
public class SaveFavoriteInteractor implements SaveFavoriteInputBoundary {

    private final SaveFavoriteDataAccessInterface dataAccess;
    private final SaveFavoriteOutputBoundary presenter;

    public SaveFavoriteInteractor(SaveFavoriteDataAccessInterface dataAccess,
                                  SaveFavoriteOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(SaveFavoriteInputData inputData) {
        final String username = inputData.getUsername();
        final String listingName = inputData.getListingName();

        // Get the user
        final User user = dataAccess.getUser(username);

        // Get the listing
        final Listing listing = dataAccess.getListingByName(listingName);

        boolean alreadyInFavourites = user.getFavourite().contains(listing);

        // === TOGGLE LOGIC ===
        if (alreadyInFavourites) {
            user.removeFavourite(listing);
        } else {
            user.addFavourite(listing);
        }

        dataAccess.saveUser(user);

        SaveFavoriteOutputData outputData =
                new SaveFavoriteOutputData(listingName, alreadyInFavourites);

        presenter.prepareSuccessView(outputData);
    }
}