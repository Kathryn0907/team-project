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
        if (user == null) {
            presenter.prepareFailView("User not found: " + username);
            return;
        }

        // Get the listing
        final Listing listing = dataAccess.getListingByName(listingName);
        if (listing == null) {
            presenter.prepareFailView("Listing not found: " + listingName);
            return;
        }

        // Check if listing is not active (unavailable)
        if (!listing.isActive()) {
            presenter.prepareFailView("Listing is unavailable and cannot be saved.");
            return;
        }

        // Check if already in favorites
        boolean alreadyInFavourites = user.getFavourite().contains(listing);

        if (!alreadyInFavourites) {
            // Add to favorites
            user.addFavourite(listing);
            // Save the updated user
            dataAccess.saveUser(user);
        }

        // Prepare output data
        final SaveFavoriteOutputData outputData =
                new SaveFavoriteOutputData(listingName, alreadyInFavourites);

        presenter.prepareSuccessView(outputData);
    }
}