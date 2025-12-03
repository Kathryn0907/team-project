package use_case.create_listing;

import Entities.Listing;
import Entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CreateListingInteractorTest {

    private TestDAO dao;
    private TestPresenter presenter;
    private CreateListingInteractor interactor;
    private User owner;

    @BeforeEach
    void setUp() {
        dao = new TestDAO();
        presenter = new TestPresenter();
        interactor = new CreateListingInteractor(dao, presenter);

        owner = new User("alice", "password");
    }

    private CreateListingInputData validInput() {
        return new CreateListingInputData(
                "My Listing",
                owner,
                "photo123",
                List.of("tag"),
                List.of("cat"),
                "Nice description",
                100.0,
                "123 Main St",
                2.5,
                50.0,
                2,
                1,
                Listing.BuildingType.CONDO,
                true
        );
    }

    @Test
    void failWhenNameNull() {
        CreateListingInputData input = validInput();
        input = new CreateListingInputData(
                null,
                owner,
                "photo",
                input.getTags(),
                input.getMainCategories(),
                input.getDescription(),
                input.getPrice(),
                input.getAddress(),
                input.getDistance(),
                input.getArea(),
                input.getBedrooms(),
                input.getBathrooms(),
                input.getBuildingType(),
                input.isActive()
        );

        interactor.execute(input);

        assertEquals("Listing name cannot be empty", presenter.failMessage);
        assertNull(presenter.successData);
        assertFalse(dao.saveCalled);
        assertFalse(dao.addCalled);
    }

    @Test
    void failWhenDescriptionNull() {
        CreateListingInputData input = validInput();
        input = new CreateListingInputData(
                input.getName(),
                owner,
                "photo",
                input.getTags(),
                input.getMainCategories(),
                null,
                input.getPrice(),
                input.getAddress(),
                input.getDistance(),
                input.getArea(),
                input.getBedrooms(),
                input.getBathrooms(),
                input.getBuildingType(),
                input.isActive()
        );

        interactor.execute(input);

        assertEquals("Property description cannot be empty", presenter.failMessage);
        assertNull(presenter.successData);
        assertFalse(dao.saveCalled);
    }

    @Test
    void failWhenPriceNegative() {
        CreateListingInputData input = validInput();
        input = new CreateListingInputData(
                input.getName(),
                owner,
                "photo",
                input.getTags(),
                input.getMainCategories(),
                input.getDescription(),
                -10,
                input.getAddress(),
                input.getDistance(),
                input.getArea(),
                input.getBedrooms(),
                input.getBathrooms(),
                input.getBuildingType(),
                input.isActive()
        );

        interactor.execute(input);

        assertEquals("Price must be a positive number", presenter.failMessage);
        assertFalse(dao.saveCalled);
    }

    @Test
    void failWhenAreaNegative() {
        CreateListingInputData input = validInput();
        input = new CreateListingInputData(
                input.getName(),
                owner,
                "photo",
                input.getTags(),
                input.getMainCategories(),
                input.getDescription(),
                input.getPrice(),
                input.getAddress(),
                input.getDistance(),
                -1,
                input.getBedrooms(),
                input.getBathrooms(),
                input.getBuildingType(),
                input.isActive()
        );

        interactor.execute(input);

        assertEquals("Area must be a positive number", presenter.failMessage);
        assertFalse(dao.saveCalled);
    }

    @Test
    void failWhenBedroomsNegative() {
        CreateListingInputData input = validInput();
        input = new CreateListingInputData(
                input.getName(),
                owner,
                "photo",
                input.getTags(),
                input.getMainCategories(),
                input.getDescription(),
                input.getPrice(),
                input.getAddress(),
                input.getDistance(),
                input.getArea(),
                -1,
                1,
                input.getBuildingType(),
                input.isActive()
        );

        interactor.execute(input);

        assertEquals("Bedroom and bathroom counts must be a positive number", presenter.failMessage);
        assertFalse(dao.saveCalled);
    }

    @Test
    void failWhenPhotoNull() {
        CreateListingInputData input = validInput();
        input = new CreateListingInputData(
                input.getName(),
                owner,
                null,
                input.getTags(),
                input.getMainCategories(),
                input.getDescription(),
                input.getPrice(),
                input.getAddress(),
                input.getDistance(),
                input.getArea(),
                input.getBedrooms(),
                input.getBathrooms(),
                input.getBuildingType(),
                input.isActive()
        );

        interactor.execute(input);

        assertEquals("Please upload a photo.", presenter.failMessage);
        assertFalse(dao.saveCalled);
    }

    @Test
    void failWhenNameEmpty() {
        CreateListingInputData input = validInput();
        input = new CreateListingInputData(
                "",                 // EMPTY name
                owner,
                "photo",
                input.getTags(),
                input.getMainCategories(),
                input.getDescription(),
                input.getPrice(),
                input.getAddress(),
                input.getDistance(),
                input.getArea(),
                input.getBedrooms(),
                input.getBathrooms(),
                input.getBuildingType(),
                input.isActive()
        );

        interactor.execute(input);

        assertEquals("Listing name cannot be empty", presenter.failMessage);
    }

    @Test
    void failWhenDescriptionEmpty() {
        CreateListingInputData input = validInput();
        input = new CreateListingInputData(
                input.getName(),
                owner,
                "photo",
                input.getTags(),
                input.getMainCategories(),
                "",                 // EMPTY description
                input.getPrice(),
                input.getAddress(),
                input.getDistance(),
                input.getArea(),
                input.getBedrooms(),
                input.getBathrooms(),
                input.getBuildingType(),
                input.isActive()
        );

        interactor.execute(input);

        assertEquals("Property description cannot be empty", presenter.failMessage);
    }

    @Test
    void failWhenPhotoEmpty() {
        CreateListingInputData input = validInput();
        input = new CreateListingInputData(
                input.getName(),
                owner,
                "",  // EMPTY photo
                input.getTags(),
                input.getMainCategories(),
                input.getDescription(),
                input.getPrice(),
                input.getAddress(),
                input.getDistance(),
                input.getArea(),
                input.getBedrooms(),
                input.getBathrooms(),
                input.getBuildingType(),
                input.isActive()
        );

        interactor.execute(input);

        assertEquals("Please upload a photo.", presenter.failMessage);
    }

    @Test
    void failWhenBathroomsNegative() {
        CreateListingInputData input = validInput();
        input = new CreateListingInputData(
                input.getName(),
                owner,
                "photo",
                input.getTags(),
                input.getMainCategories(),
                input.getDescription(),
                input.getPrice(),
                input.getAddress(),
                input.getDistance(),
                input.getArea(),
                input.getBedrooms(),
                -1,        // negative bathrooms
                input.getBuildingType(),
                input.isActive()
        );

        interactor.execute(input);

        assertEquals("Bedroom and bathroom counts must be a positive number", presenter.failMessage);
    }

    @Test
    void successFlow() {
        CreateListingInputData input = validInput();

        interactor.execute(input);

        assertTrue(dao.saveCalled);
        assertTrue(dao.addCalled);

        assertNotNull(dao.savedListing);
        assertEquals("My Listing", dao.savedListing.getName());
        assertEquals(owner.getId(), dao.savedListing.getOwnerId());

        assertNotNull(presenter.successData);
        assertSame(dao.savedListing, presenter.successData.getListing());
    }

    @Test
    void testSwitchToProfileView() {
        interactor.switchToProfileView();
        assertTrue(presenter.switchCalled);
    }


    static class TestDAO implements CreateListingDataAccessInterface {
        boolean saveCalled = false;
        boolean addCalled = false;

        Listing savedListing;
        User savedUser;

        @Override
        public void save(Listing listing) {
            saveCalled = true;
            savedListing = listing;
        }

        @Override
        public void addListingToUser(User user, Listing listing) {
            addCalled = true;
            savedUser = user;
            savedListing = listing;
        }
    }

    static class TestPresenter implements CreateListingOutputBoundary {
        String failMessage = null;
        CreateListingOutputData successData = null;
        boolean switchCalled = false;

        @Override
        public void prepareSuccessView(CreateListingOutputData outputData) {
            this.successData = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            this.failMessage = errorMessage;
        }

        @Override
        public void switchToProfileView() {
            switchCalled = true;
        }
    }
}

