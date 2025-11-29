package use_case.delete_listing;

import Entities.Listing;
import Entities.User;
import use_case.delete_listing.DeleteListingDataAccessInterface;

public class DeleteListingInteractor implements DeleteListingInputBoundary {

    private final DeleteListingDataAccessInterface listingDAO;
    private final DeleteListingOutputBoundary presenter;

    public DeleteListingInteractor(DeleteListingDataAccessInterface listingDAO,
                                   DeleteListingOutputBoundary presenter) {
        this.listingDAO = listingDAO;
        this.presenter = presenter;
    }

    @Override
    public void execute(DeleteListingInputData inputData) {

        Listing listing = listingDAO.findListingById(inputData.getListingId());

        if (listing == null) {
            presenter.prepareFailView("Listing not found.");
            return;
        }

        if (!listing.getOwnerId().equals(inputData.getOwnerId())) {
            presenter.prepareFailView("Unauthorized: You can only delete your own listings.");
            return;
        }

        listingDAO.deleteListing(listing);
        listingDAO.removeListingFromUser(inputData.getOwnerId(), listing);

        presenter.prepareSuccessView(new DeleteListingOutputData(listing.getId()));
    }
}