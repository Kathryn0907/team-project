package use_case.filter;

import Entities.Listing;
import Entities.Listing.BuildingType;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests for FilterListingsInteractor.
 */
public class FilterListingsInteractorTest {

    /**
     * Simple fake DAO that returns a preconfigured list of listings.
     */
    private static class InMemoryListingDataAccess implements FilterListingsDataAccessInterface {

        private final List<Listing> listings;

        InMemoryListingDataAccess(List<Listing> listings) {
            this.listings = listings;
        }

        @Override
        public List<Listing> getAllActiveListings() {
            // In real code you might filter active here; for tests we'll control isActive flag ourselves.
            return listings;
        }
    }

    /**
     * Fake DistanceService that we can program with custom distances.
     */
    private static class FakeDistanceService implements DistanceService {

        @Override
        public double calculateDistanceKm(String originAddress, String destinationAddress) {
            // Simple rule for tests:
            if ("NearAddress".equals(destinationAddress)) {
                return 3.0;  // 3 km away
            } else if ("FarAddress".equals(destinationAddress)) {
                return 10.0; // 10 km away
            }
            return 0.0;
        }
    }

    /**
     * Fake presenter that records the last output or error message.
     */
    private static class RecordingPresenter implements FilterListingsOutputBoundary {

        FilterListingsOutputData lastOutput;
        String lastError;

        @Override
        public void present(FilterListingsOutputData outputData) {
            this.lastOutput = outputData;
        }

        @Override
        public void presentError(String errorMessage) {
            this.lastError = errorMessage;
        }
    }

    private Listing createListing(String name,
                                  double price,
                                  String address,
                                  double area,
                                  int bedrooms,
                                  int bathrooms,
                                  BuildingType buildingType,
                                  boolean active) {

        return new Listing(
                name,
                null,             // owner
                null,             // photoPath
                null,             // tags
                null,             // mainCategories
                "desc",           // description
                price,
                address,
                0.0,              // distance (will be overwritten by interactor)
                area,
                bedrooms,
                bathrooms,
                buildingType,
                active
        );
    }

    @Test
    public void testFiltersByPriceBedroomsAndBuildingType() {
        // Arrange
        Listing l1 = createListing("CheapCondo", 100.0, "Addr1",
                50.0, 2, 1, BuildingType.CONDO, true);
        Listing l2 = createListing("ExpensiveCondo", 300.0, "Addr2",
                60.0, 3, 2, BuildingType.CONDO, true);
        Listing l3 = createListing("CheapHouse", 120.0, "Addr3",
                55.0, 2, 1, BuildingType.HOUSE, true);
        Listing l4 = createListing("InactiveCondo", 90.0, "Addr4",
                40.0, 1, 1, BuildingType.CONDO, false);

        List<Listing> allListings = Arrays.asList(l1, l2, l3, l4);

        InMemoryListingDataAccess dao = new InMemoryListingDataAccess(allListings);
        FakeDistanceService distanceService = new FakeDistanceService();
        RecordingPresenter presenter = new RecordingPresenter();

        FilterListingsInteractor interactor =
                new FilterListingsInteractor(dao, distanceService, presenter);

        // Filter: price between 50 and 200, at least 2 bedrooms, CONDO only
        FilterListingsInputData input = new FilterListingsInputData(
                null,              // userAddress
                null,              // maxDistanceKm
                50.0,              // minPrice
                200.0,             // maxPrice
                null,              // minArea
                null,              // maxArea
                2,                 // minBedrooms
                null,              // minBathrooms
                BuildingType.CONDO // building type
        );

        // Act
        interactor.execute(input);

        // Assert
        assertNull("No error expected", presenter.lastError);
        assertNotNull("Output expected", presenter.lastOutput);

        ArrayList<Listing> results = presenter.lastOutput.getListings();
        assertEquals(1, results.size());
        assertEquals("CheapCondo", results.get(0).getName());
    }

    @Test
    public void testFiltersByDistanceUsingDistanceService() {
        // Arrange
        Listing near = createListing("NearListing", 150.0, "NearAddress",
                50.0, 2, 1, BuildingType.CONDO, true);
        Listing far = createListing("FarListing", 150.0, "FarAddress",
                50.0, 2, 1, BuildingType.CONDO, true);

        List<Listing> allListings = Arrays.asList(near, far);

        InMemoryListingDataAccess dao = new InMemoryListingDataAccess(allListings);
        FakeDistanceService distanceService = new FakeDistanceService();
        RecordingPresenter presenter = new RecordingPresenter();

        FilterListingsInteractor interactor =
                new FilterListingsInteractor(dao, distanceService, presenter);

        // Filter: max distance 5 km from "UserAddress"
        FilterListingsInputData input = new FilterListingsInputData(
                "UserAddress", // userAddress
                5.0,           // maxDistanceKm
                null, null,    // minPrice, maxPrice
                null, null,    // minArea, maxArea
                null, null,    // minBedrooms, minBathrooms
                null           // buildingType
        );

        // Act
        interactor.execute(input);

        // Assert
        assertNull(presenter.lastError);
        assertNotNull(presenter.lastOutput);
        ArrayList<Listing> results = presenter.lastOutput.getListings();

        // Only the "NearAddress" listing (3 km) should remain, not the 10 km one
        assertEquals(1, results.size());
        assertEquals("NearListing", results.get(0).getName());

        // Optional: check that distance got written back
        assertEquals(3.0, results.get(0).getDistance(), 0.0001);
    }

    @Test
    public void testInvalidPriceRangeTriggersError() {
        // Arrange
        Listing l1 = createListing("Any", 100.0, "Addr", 50.0,
                2, 1, BuildingType.CONDO, true);
        InMemoryListingDataAccess dao =
                new InMemoryListingDataAccess(Arrays.asList(l1));
        FakeDistanceService distanceService = new FakeDistanceService();
        RecordingPresenter presenter = new RecordingPresenter();

        FilterListingsInteractor interactor =
                new FilterListingsInteractor(dao, distanceService, presenter);

        // min price > max price
        FilterListingsInputData input = new FilterListingsInputData(
                null, null,
                200.0, 100.0,  // invalid range
                null, null,
                null, null,
                null
        );

        // Act
        interactor.execute(input);

        // Assert
        assertNull("No output should be produced on error", presenter.lastOutput);
        assertEquals("Min price cannot be greater than max price.", presenter.lastError);
    }
}