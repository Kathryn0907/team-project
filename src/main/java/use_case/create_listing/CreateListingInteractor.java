package use_case.create_listing;

import Entities.Listing;
import Entities.User;

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

        if (createListingInputData.getName() == null || createListingInputData.getName().trim().isEmpty()) {
            listingPresenter.prepareFailView("Listing Name cannot be empty");
            return;
        }
        if (createListingInputData.getDescription() == null || createListingInputData.getDescription().trim().isEmpty()) {
            listingPresenter.prepareFailView("Property description cannot be empty");
            return;
        }
        if (createListingInputData.getPrice() < 0) {
            listingPresenter.prepareFailView("Price cannot be negative");
            return;
        }
        if (createListingInputData.getArea() < 0) {
            listingPresenter.prepareFailView("Area cannot be negative");
            return;
        }
        if (createListingInputData.getBedrooms() < 0 || createListingInputData.getBathrooms() < 0) {
            listingPresenter.prepareFailView("Bedroom and bathroom counts cannot be negative");
            return;
        }

        Listing listing = new Listing(
                createListingInputData.getName(),
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

        listing.setOwnerId(createListingInputData.getOwner().getId());
        listingDataAccessObject.save(listing);
        listingDataAccessObject.addListingToUser(createListingInputData.getOwner(), listing);
        listingPresenter.prepareSuccessView(new CreateListingOutputData(listing));
    }
    @Override
    public void switchToProfileView() {
        listingPresenter.switchToProfileView();
    }
}
