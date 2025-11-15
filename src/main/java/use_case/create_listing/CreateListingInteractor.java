package use_case.create_listing;

import Entities.Listing;

/**
 * The Create Listing Interactor.
 */
public class CreateListingInteractor implements CreateListingInputBoundary {
    private final CreateListingDataAccessInterface listingDataAccessObject;
    private final CreateListingOutputBoundary listingPresenter;

    public CreateListingInteractor(CreateListingDataAccessInterface listingDataAccessObject,
                                   CreateListingOutputBoundary listingPresenter) {
        this.listingDataAccessObject = listingDataAccessObject;
        this.listingPresenter = listingPresenter;
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
                    createListingInputData.getOwner(),
                    createListingInputData.getPhotoPath(),
                    createListingInputData.getTags(),
                    createListingInputData.getMainCategories(),
                    createListingInputData.getDescription(),
                    createListingInputData.getPrice(),
                    createListingInputData.getAddress(),
                    createListingInputData.getDistance(),
                    createListingInputData.getArea(),
                    createListingInputData.getBedrooms(),
                    createListingInputData.getBathrooms(),
                    createListingInputData.getBuildingType(),
                    true
                    );
            listingDataAccessObject.save(listing);

            final CreateListingOutputData createListingOutputData = new CreateListingOutputData(listing.getOwner(),
                    listing.getName(), listing.getPhotoPath());
            listingPresenter.prepareSuccessView(createListingOutputData);
        }
    }
    @Override
    public void swtichToProfileView() {
        listingPresenter.switchToProfileView();
    }

}
