package use_case.edit_listing;

import Entities.Listing;

public class EditListingInteractor implements EditListingInputBoundary {

    private final EditListingDataAccessInterface listingDAO;
    private final EditListingOutputBoundary presenter;

    public EditListingInteractor(EditListingDataAccessInterface listingDAO,
                                 EditListingOutputBoundary presenter) {
        this.listingDAO = listingDAO;
        this.presenter = presenter;
    }

    @Override
    public void saveEdits(EditListingInputData data) {
        Listing listing = data.getListing();

        // Simple validation (same as CreateListing rules)
        if (listing.getName() == null || listing.getName().isEmpty()) {
            presenter.prepareFailView("Listing name cannot be empty.");
            return;
        }
        if (listing.getDescription() == null || listing.getDescription().isEmpty()) {
            presenter.prepareFailView("Description cannot be empty.");
            return;
        }

        listingDAO.update(listing);

        presenter.prepareSuccessView(new EditListingOutputData(listing));
    }
}