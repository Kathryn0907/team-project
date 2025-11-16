package use_case.create_listing;

import Entities.Listing;

/**
 * The Create Listing Interactor.
 */
public class CreateListingInteractor implements CreateListingInputBoundary {
    private final CreateListingDataAccessInterface listingDataAccessObject;
    private final CreateListingOutputBoundary listingPresenter;
    private final Listing listing;

    public CreateListingInteractor(CreateListingDataAccessInterface listingDataAccessObject,
                                   CreateListingOutputBoundary listingPresenter,
                                   Listing listing) {
        this.listingDataAccessObject = listingDataAccessObject;
        this.listingPresenter = listingPresenter;
        this.listing = this.listing;
    }
    @Override
    public void execute(CreateListingInputData createListingInputData) {
        if ("".equals(createListingInputData.getName())) {
            listingPresenter.prepareFailView("Name cannot be empty");
        }
        else if ("".equals(createListingInputData.getDescription())) {
            listingPresenter.prepareFailView("Description cannot be empty");
        }
        else {
            final Listing listing = new Listing(createListingInputData.getName(),
                    createListingInputData.getTags(),
                    createListingInputData.getDescription());
            listingDataAccessObject.save(listing);

            final CreateListingOutputData createListingOutputData = new CreateListingOutputData(listing.getName());
            listingPresenter.prepareSuccessView(createListingOutputData);
        }
    }
    @Override
    public void swtichToProfileView() {
        listingPresenter.switchToProfileView();
    }

}
