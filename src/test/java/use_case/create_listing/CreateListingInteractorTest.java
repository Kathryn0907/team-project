package use_case.create_listing;

import Entities.Listing;
import Entities.User;
import data_access.InMemoryListingDataAccessObject;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class CreateListingInteractorTest {

    @Test
    void failEmptyNameTest() {

        User owner = new User("olivia", "pass");

        CreateListingInputData inputData = new CreateListingInputData(
                "",
                owner,
                "photo.jpg",
                Collections.emptyList(),
                Collections.emptyList(),
                "desc",
                100,
                "address",
                1,
                600,
                1,
                1,
                Listing.BuildingType.HOUSE,
                true
        );

        InMemoryListingDataAccessObject listingRepo = new InMemoryListingDataAccessObject();

        CreateListingOutputBoundary presenter = new CreateListingOutputBoundary() {
            @Override
            public void prepareSuccessView(CreateListingOutputData outputData) {
                fail("Expected failure, but got success.");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("Listing Name cannot be empty", errorMessage);
            }

            @Override
            public void switchToProfileView() {
                fail("Should not switch view in failure test.");
            }
        };

        CreateListingInteractor interactor = new CreateListingInteractor(listingRepo, presenter);
        interactor.execute(inputData);
    }

    @Test
    void failEmptyDescriptionTest() {

        User owner = new User("olivia", "pass");

        CreateListingInputData inputData = new CreateListingInputData(
                "Condo",
                owner,
                "photo.jpg",
                Collections.emptyList(),
                Collections.emptyList(),
                "",
                100,
                "address",
                1,
                600,
                1,
                1,
                Listing.BuildingType.HOUSE,
                true
        );

        InMemoryListingDataAccessObject listingRepo = new InMemoryListingDataAccessObject();

        CreateListingOutputBoundary presenter = new CreateListingOutputBoundary() {
            @Override
            public void prepareSuccessView(CreateListingOutputData outputData) {
                fail("Expected failure, but got success.");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("Property description cannot be empty", errorMessage);
            }

            @Override
            public void switchToProfileView() {
                fail("Should not switch view in failure test.");
            }
        };

        CreateListingInteractor interactor = new CreateListingInteractor(listingRepo, presenter);
        interactor.execute(inputData);
    }

    @Test
    void switchToProfileViewTest() {

        InMemoryListingDataAccessObject listingRepo = new InMemoryListingDataAccessObject();

        CreateListingOutputBoundary presenter = new CreateListingOutputBoundary() {
            @Override
            public void prepareSuccessView(CreateListingOutputData outputData) {
                fail("Not testing success here.");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Not testing failure here.");
            }

            @Override
            public void switchToProfileView() {
                assertTrue(true);
            }
        };

        CreateListingInteractor interactor = new CreateListingInteractor(listingRepo, presenter);
        interactor.swtichToProfileView();
    }
}

