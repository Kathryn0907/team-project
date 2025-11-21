package use_case.check_favorite;

import Entities.User;
import Entities.Listing;

import java.util.ArrayList;
import java.util.List;

/**
 * The Check Favorite Interactor.
 */
public class CheckFavoriteInteractor implements CheckFavoriteInputBoundary {

    private final CheckFavoriteDataAccessInterface dataAccess;
    private final CheckFavoriteOutputBoundary presenter;

    public CheckFavoriteInteractor(CheckFavoriteDataAccessInterface dataAccess,
                                   CheckFavoriteOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(CheckFavoriteInputData inputData) {
        final String username = inputData.getUsername();

        // Get the user from the user DAO
        final User user = dataAccess.getUser(username);
        if (user == null) {
            presenter.prepareFailView("User not found: " + username);
            return;
        }

        // Get the user's favorite listings (may be null / empty)
        final ArrayList<Listing> favouriteListings = user.getFavourite();

        // Extract listing names (only active listings)
        final List<String> favouriteListingNames = new ArrayList<>();
        if (favouriteListings != null) {
            for (Listing listing : favouriteListings) {
                if (listing != null && listing.isActive()) {
                    favouriteListingNames.add(listing.getName());
                }
            }
        }

        // Prepare output data
        final CheckFavoriteOutputData outputData =
                new CheckFavoriteOutputData(favouriteListingNames);

        presenter.prepareSuccessView(outputData);
    }
}
