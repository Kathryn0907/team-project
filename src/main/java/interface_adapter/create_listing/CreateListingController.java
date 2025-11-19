package interface_adapter.create_listing;

import Entities.Listing;
import Entities.User;
import use_case.create_listing.CreateListingInputBoundary;
import use_case.create_listing.CreateListingInputData;

import java.util.List;

public class CreateListingController {
    private final CreateListingInputBoundary createListingUseCaseInteractor;

    public CreateListingController(CreateListingInputBoundary createListingUseCaseInteractor) {
        this.createListingUseCaseInteractor = createListingUseCaseInteractor;
    }
    /**
     * Executes the Create Listing Use Case
     *
     * @param name           is the name of the listing
     * @param owner
     * @param photoPath      is the path of the photo given
     * @param tags
     * @param mainCategories
     * @param description    is the description of the listing
     * @param price
     * @param address
     * @param distance
     * @param area
     * @param bedrooms
     * @param bathrooms
     * @param buildingType
     * @param active
     */
    public void execute(String name,
                        User owner,
                        String photoPath,
                        List<String> tags,
                        List<String> mainCategories,
                        String description,
                        double price,
                        String address,
                        double distance,
                        double area,
                        int bedrooms,
                        int bathrooms,
                        Listing.BuildingType buildingType,  // NEW
                        boolean active) {
        final CreateListingInputData createListingInputData = new CreateListingInputData(name,
                owner, photoPath, tags, mainCategories, description, price, address, distance, area, bedrooms,
                bathrooms, buildingType, active);
        createListingUseCaseInteractor.execute(createListingInputData);

    }
    public void switchToProfileView() {
        createListingUseCaseInteractor.swtichToProfileView();
    }
}
