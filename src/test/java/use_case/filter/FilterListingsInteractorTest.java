package use_case.filter;

import Entities.Listing;
import Entities.Listing.BuildingType;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class FilterListingsInteractorTest {

    private static class InMemoryListingDataAccess implements FilterListingsDataAccessInterface {

        private final List<Listing> listings;

        InMemoryListingDataAccess(List<Listing> listings) {
            this.listings = listings;
        }

        @Override
        public List<Listing> getAllActiveListings() {
            return listings;
        }
    }

    private static class FakeDistanceService implements DistanceService {

        @Override
        public double calculateDistanceKm(String originAddress, String destinationAddress) {
            if ("NearAddress".equals(destinationAddress)) {
                return 3.0;
            } else if ("FarAddress".equals(destinationAddress)) {
                return 10.0;
            } else if ("VeryFarAddress".equals(destinationAddress)) {
                return 15.0;
            }
            return 0.0;
        }
    }

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
                null,
                null,
                null,
                null,
                "desc",
                price,
                address,
                0.0,
                area,
                bedrooms,
                bathrooms,
                buildingType,
                active
        );
    }

    @Test
    public void testFiltersByPriceBedroomsAndBuildingType() {
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

        FilterListingsInputData input = new FilterListingsInputData(
                null,
                null,
                50.0,
                200.0,
                null,
                null,
                2,
                null,
                BuildingType.CONDO
        );

        interactor.execute(input);

        assertNull("No error expected", presenter.lastError);
        assertNotNull("Output expected", presenter.lastOutput);

        ArrayList<Listing> results = presenter.lastOutput.getListings();
        assertEquals(1, results.size());
        assertEquals("CheapCondo", results.get(0).getName());
    }

    @Test
    public void testFiltersByDistanceUsingDistanceService() {
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

        FilterListingsInputData input = new FilterListingsInputData(
                "UserAddress",
                5.0,
                null, null,
                null, null,
                null, null,
                null
        );

        interactor.execute(input);

        assertNull(presenter.lastError);
        assertNotNull(presenter.lastOutput);
        ArrayList<Listing> results = presenter.lastOutput.getListings();

        assertEquals(1, results.size());
        assertEquals("NearListing", results.get(0).getName());

        assertEquals(3.0, results.get(0).getDistance(), 0.0001);
    }

    @Test
    public void testInvalidPriceRangeTriggersError() {
        Listing l1 = createListing("Any", 100.0, "Addr", 50.0,
                2, 1, BuildingType.CONDO, true);
        InMemoryListingDataAccess dao =
                new InMemoryListingDataAccess(Arrays.asList(l1));
        FakeDistanceService distanceService = new FakeDistanceService();
        RecordingPresenter presenter = new RecordingPresenter();

        FilterListingsInteractor interactor =
                new FilterListingsInteractor(dao, distanceService, presenter);

        FilterListingsInputData input = new FilterListingsInputData(
                null, null,
                200.0, 100.0,
                null, null,
                null, null,
                null
        );

        interactor.execute(input);

        assertNull("No output should be produced on error", presenter.lastOutput);
        assertEquals("Min price cannot be greater than max price.", presenter.lastError);
    }

    @Test
    public void testFiltersByBathroomsOnly() {
        Listing l1 = createListing("OneBath", 100.0, "Addr1",
                50.0, 2, 1, BuildingType.CONDO, true);
        Listing l2 = createListing("TwoBath", 150.0, "Addr2",
                60.0, 3, 2, BuildingType.HOUSE, true);
        Listing l3 = createListing("ThreeBath", 200.0, "Addr3",
                70.0, 4, 3, BuildingType.TOWNHOUSE, true);

        InMemoryListingDataAccess dao = new InMemoryListingDataAccess(
                Arrays.asList(l1, l2, l3));
        FakeDistanceService distanceService = new FakeDistanceService();
        RecordingPresenter presenter = new RecordingPresenter();

        FilterListingsInteractor interactor =
                new FilterListingsInteractor(dao, distanceService, presenter);

        FilterListingsInputData input = new FilterListingsInputData(
                null, null, null, null, null, null, null,
                2,
                null
        );

        interactor.execute(input);

        assertNull(presenter.lastError);
        assertNotNull(presenter.lastOutput);
        ArrayList<Listing> results = presenter.lastOutput.getListings();
        assertEquals(2, results.size());
        assertTrue(results.stream().anyMatch(l -> l.getName().equals("TwoBath")));
        assertTrue(results.stream().anyMatch(l -> l.getName().equals("ThreeBath")));
    }

    @Test
    public void testFiltersByAreaRange() {
        Listing l1 = createListing("Small", 100.0, "Addr1",
                40.0, 2, 1, BuildingType.CONDO, true);
        Listing l2 = createListing("Medium", 150.0, "Addr2",
                60.0, 2, 1, BuildingType.CONDO, true);
        Listing l3 = createListing("Large", 200.0, "Addr3",
                100.0, 3, 2, BuildingType.HOUSE, true);

        InMemoryListingDataAccess dao = new InMemoryListingDataAccess(
                Arrays.asList(l1, l2, l3));
        FakeDistanceService distanceService = new FakeDistanceService();
        RecordingPresenter presenter = new RecordingPresenter();

        FilterListingsInteractor interactor =
                new FilterListingsInteractor(dao, distanceService, presenter);

        FilterListingsInputData input = new FilterListingsInputData(
                null, null, null, null,
                50.0,
                80.0,
                null, null, null
        );

        interactor.execute(input);

        assertNull(presenter.lastError);
        assertNotNull(presenter.lastOutput);
        ArrayList<Listing> results = presenter.lastOutput.getListings();
        assertEquals(1, results.size());
        assertEquals("Medium", results.get(0).getName());
    }

    @Test
    public void testInvalidAreaRangeTriggersError() {
        Listing l1 = createListing("Any", 100.0, "Addr", 50.0,
                2, 1, BuildingType.CONDO, true);
        InMemoryListingDataAccess dao =
                new InMemoryListingDataAccess(Arrays.asList(l1));
        FakeDistanceService distanceService = new FakeDistanceService();
        RecordingPresenter presenter = new RecordingPresenter();

        FilterListingsInteractor interactor =
                new FilterListingsInteractor(dao, distanceService, presenter);

        FilterListingsInputData input = new FilterListingsInputData(
                null, null, null, null,
                100.0, 50.0,
                null, null, null
        );

        interactor.execute(input);

        assertNull("No output should be produced on error", presenter.lastOutput);
        assertEquals("Min area cannot be greater than max area.", presenter.lastError);
    }

    @Test
    public void testFiltersByBuildingTypeOnly() {
        Listing l1 = createListing("Condo1", 100.0, "Addr1",
                50.0, 2, 1, BuildingType.CONDO, true);
        Listing l2 = createListing("House1", 150.0, "Addr2",
                60.0, 3, 2, BuildingType.HOUSE, true);
        Listing l3 = createListing("Townhouse1", 120.0, "Addr3",
                55.0, 2, 1, BuildingType.TOWNHOUSE, true);

        InMemoryListingDataAccess dao = new InMemoryListingDataAccess(
                Arrays.asList(l1, l2, l3));
        FakeDistanceService distanceService = new FakeDistanceService();
        RecordingPresenter presenter = new RecordingPresenter();

        FilterListingsInteractor interactor =
                new FilterListingsInteractor(dao, distanceService, presenter);

        FilterListingsInputData input = new FilterListingsInputData(
                null, null, null, null, null, null, null, null,
                BuildingType.HOUSE
        );

        interactor.execute(input);

        assertNull(presenter.lastError);
        assertNotNull(presenter.lastOutput);
        ArrayList<Listing> results = presenter.lastOutput.getListings();
        assertEquals(1, results.size());
        assertEquals("House1", results.get(0).getName());
        assertEquals(BuildingType.HOUSE, results.get(0).getBuildingType());
    }

    @Test
    public void testWithNullAddress_SkipsDistanceCalculation() {
        Listing l1 = createListing("Listing1", 100.0, "Addr1",
                50.0, 2, 1, BuildingType.CONDO, true);
        Listing l2 = createListing("Listing2", 150.0, "Addr2",
                60.0, 3, 2, BuildingType.HOUSE, true);

        InMemoryListingDataAccess dao = new InMemoryListingDataAccess(
                Arrays.asList(l1, l2));
        FakeDistanceService distanceService = new FakeDistanceService();
        RecordingPresenter presenter = new RecordingPresenter();

        FilterListingsInteractor interactor =
                new FilterListingsInteractor(dao, distanceService, presenter);

        FilterListingsInputData input = new FilterListingsInputData(
                null,
                null,
                50.0, 200.0,
                null, null, null, null, null
        );

        interactor.execute(input);

        assertNull(presenter.lastError);
        assertNotNull(presenter.lastOutput);
        ArrayList<Listing> results = presenter.lastOutput.getListings();
        assertEquals(2, results.size());
        assertEquals(0.0, results.get(0).getDistance(), 0.0001);
    }

    @Test
    public void testWithEmptyAddress_SkipsDistanceCalculation() {
        Listing l1 = createListing("Listing1", 100.0, "Addr1",
                50.0, 2, 1, BuildingType.CONDO, true);

        InMemoryListingDataAccess dao = new InMemoryListingDataAccess(
                Arrays.asList(l1));
        FakeDistanceService distanceService = new FakeDistanceService();
        RecordingPresenter presenter = new RecordingPresenter();

        FilterListingsInteractor interactor =
                new FilterListingsInteractor(dao, distanceService, presenter);

        FilterListingsInputData input = new FilterListingsInputData(
                "   ",
                5.0,
                null, null, null, null, null, null, null
        );

        interactor.execute(input);

        assertNull(presenter.lastError);
        assertNotNull(presenter.lastOutput);
        ArrayList<Listing> results = presenter.lastOutput.getListings();
        assertEquals(1, results.size());
    }

    @Test
    public void testListingWithNullAddress_IsSkippedWhenDistanceFilterActive() {
        Listing l1 = createListing("HasAddress", 100.0, "NearAddress",
                50.0, 2, 1, BuildingType.CONDO, true);
        Listing l2 = createListing("NoAddress", 150.0, null,
                60.0, 3, 2, BuildingType.HOUSE, true);

        InMemoryListingDataAccess dao = new InMemoryListingDataAccess(
                Arrays.asList(l1, l2));
        FakeDistanceService distanceService = new FakeDistanceService();
        RecordingPresenter presenter = new RecordingPresenter();

        FilterListingsInteractor interactor =
                new FilterListingsInteractor(dao, distanceService, presenter);

        FilterListingsInputData input = new FilterListingsInputData(
                "UserAddress",
                10.0,
                null, null, null, null, null, null, null
        );

        interactor.execute(input);

        assertNull(presenter.lastError);
        assertNotNull(presenter.lastOutput);
        ArrayList<Listing> results = presenter.lastOutput.getListings();
        assertEquals(1, results.size());
        assertEquals("HasAddress", results.get(0).getName());
    }

    @Test
    public void testWithEmptyListingsDatabase() {
        InMemoryListingDataAccess dao = new InMemoryListingDataAccess(
                Collections.emptyList());
        FakeDistanceService distanceService = new FakeDistanceService();
        RecordingPresenter presenter = new RecordingPresenter();

        FilterListingsInteractor interactor =
                new FilterListingsInteractor(dao, distanceService, presenter);

        FilterListingsInputData input = new FilterListingsInputData(
                null, null, 100.0, 200.0, null, null, 2, null, null
        );

        interactor.execute(input);

        assertNull(presenter.lastError);
        assertNotNull(presenter.lastOutput);
        ArrayList<Listing> results = presenter.lastOutput.getListings();
        assertEquals(0, results.size());
    }

    @Test
    public void testWithNoMatchingListings() {
        Listing l1 = createListing("Cheap", 100.0, "Addr1",
                50.0, 2, 1, BuildingType.CONDO, true);
        Listing l2 = createListing("AlsoCheap", 150.0, "Addr2",
                60.0, 3, 2, BuildingType.HOUSE, true);

        InMemoryListingDataAccess dao = new InMemoryListingDataAccess(
                Arrays.asList(l1, l2));
        FakeDistanceService distanceService = new FakeDistanceService();
        RecordingPresenter presenter = new RecordingPresenter();

        FilterListingsInteractor interactor =
                new FilterListingsInteractor(dao, distanceService, presenter);

        FilterListingsInputData input = new FilterListingsInputData(
                null, null,
                5000.0, 10000.0,
                null, null, null, null, null
        );

        interactor.execute(input);

        assertNull(presenter.lastError);
        assertNotNull(presenter.lastOutput);
        ArrayList<Listing> results = presenter.lastOutput.getListings();
        assertEquals(0, results.size());
    }

    @Test
    public void testWithAllListingsMatching_NoFiltersApplied() {
        Listing l1 = createListing("Listing1", 100.0, "Addr1",
                50.0, 2, 1, BuildingType.CONDO, true);
        Listing l2 = createListing("Listing2", 150.0, "Addr2",
                60.0, 3, 2, BuildingType.HOUSE, true);
        Listing l3 = createListing("Listing3", 200.0, "Addr3",
                70.0, 4, 3, BuildingType.TOWNHOUSE, true);

        InMemoryListingDataAccess dao = new InMemoryListingDataAccess(
                Arrays.asList(l1, l2, l3));
        FakeDistanceService distanceService = new FakeDistanceService();
        RecordingPresenter presenter = new RecordingPresenter();

        FilterListingsInteractor interactor =
                new FilterListingsInteractor(dao, distanceService, presenter);

        FilterListingsInputData input = new FilterListingsInputData(
                null, null, null, null, null, null, null, null, null
        );

        interactor.execute(input);

        assertNull(presenter.lastError);
        assertNotNull(presenter.lastOutput);
        ArrayList<Listing> results = presenter.lastOutput.getListings();
        assertEquals(3, results.size());
    }

    @Test
    public void testWithAllFiltersCombined() {
        Listing l1 = createListing("Perfect", 150.0, "NearAddress",
                60.0, 3, 2, BuildingType.HOUSE, true);
        Listing l2 = createListing("WrongPrice", 50.0, "NearAddress",
                60.0, 3, 2, BuildingType.HOUSE, true);
        Listing l3 = createListing("WrongDistance", 150.0, "FarAddress",
                60.0, 3, 2, BuildingType.HOUSE, true);
        Listing l4 = createListing("WrongType", 150.0, "NearAddress",
                60.0, 3, 2, BuildingType.CONDO, true);

        InMemoryListingDataAccess dao = new InMemoryListingDataAccess(
                Arrays.asList(l1, l2, l3, l4));
        FakeDistanceService distanceService = new FakeDistanceService();
        RecordingPresenter presenter = new RecordingPresenter();

        FilterListingsInteractor interactor =
                new FilterListingsInteractor(dao, distanceService, presenter);

        FilterListingsInputData input = new FilterListingsInputData(
                "UserAddress",
                5.0,
                100.0, 200.0,
                50.0, 70.0,
                3,
                2,
                BuildingType.HOUSE
        );

        interactor.execute(input);

        assertNull(presenter.lastError);
        assertNotNull(presenter.lastOutput);
        ArrayList<Listing> results = presenter.lastOutput.getListings();
        assertEquals(1, results.size());
        assertEquals("Perfect", results.get(0).getName());
    }

    @Test
    public void testDistanceExcludesListingsBeyondRadius() {
        Listing l1 = createListing("Near", 100.0, "NearAddress",
                50.0, 2, 1, BuildingType.CONDO, true);
        Listing l2 = createListing("Far", 100.0, "VeryFarAddress",
                50.0, 2, 1, BuildingType.CONDO, true);

        InMemoryListingDataAccess dao = new InMemoryListingDataAccess(
                Arrays.asList(l1, l2));
        FakeDistanceService distanceService = new FakeDistanceService();
        RecordingPresenter presenter = new RecordingPresenter();

        FilterListingsInteractor interactor =
                new FilterListingsInteractor(dao, distanceService, presenter);

        FilterListingsInputData input = new FilterListingsInputData(
                "UserAddress", 10.0,
                null, null, null, null, null, null, null
        );

        interactor.execute(input);

        assertNull(presenter.lastError);
        assertNotNull(presenter.lastOutput);
        ArrayList<Listing> results = presenter.lastOutput.getListings();
        assertEquals(1, results.size());
        assertEquals("Near", results.get(0).getName());
    }

    @Test
    public void testPriceFilterWithOnlyMinPrice() {
        Listing l1 = createListing("Cheap", 100.0, "Addr1",
                50.0, 2, 1, BuildingType.CONDO, true);
        Listing l2 = createListing("Expensive", 300.0, "Addr2",
                60.0, 3, 2, BuildingType.HOUSE, true);

        InMemoryListingDataAccess dao = new InMemoryListingDataAccess(
                Arrays.asList(l1, l2));
        FakeDistanceService distanceService = new FakeDistanceService();
        RecordingPresenter presenter = new RecordingPresenter();

        FilterListingsInteractor interactor =
                new FilterListingsInteractor(dao, distanceService, presenter);

        FilterListingsInputData input = new FilterListingsInputData(
                null, null,
                200.0,
                null,
                null, null, null, null, null
        );

        interactor.execute(input);

        assertNull(presenter.lastError);
        assertNotNull(presenter.lastOutput);
        ArrayList<Listing> results = presenter.lastOutput.getListings();
        assertEquals(1, results.size());
        assertEquals("Expensive", results.get(0).getName());
    }

    @Test
    public void testPriceFilterWithOnlyMaxPrice() {
        Listing l1 = createListing("Cheap", 100.0, "Addr1",
                50.0, 2, 1, BuildingType.CONDO, true);
        Listing l2 = createListing("Expensive", 300.0, "Addr2",
                60.0, 3, 2, BuildingType.HOUSE, true);

        InMemoryListingDataAccess dao = new InMemoryListingDataAccess(
                Arrays.asList(l1, l2));
        FakeDistanceService distanceService = new FakeDistanceService();
        RecordingPresenter presenter = new RecordingPresenter();

        FilterListingsInteractor interactor =
                new FilterListingsInteractor(dao, distanceService, presenter);

        FilterListingsInputData input = new FilterListingsInputData(
                null, null,
                null,
                200.0,
                null, null, null, null, null
        );

        interactor.execute(input);

        assertNull(presenter.lastError);
        assertNotNull(presenter.lastOutput);
        ArrayList<Listing> results = presenter.lastOutput.getListings();
        assertEquals(1, results.size());
        assertEquals("Cheap", results.get(0).getName());
    }

    @Test
    public void testAreaFilterWithOnlyMinArea() {
        Listing l1 = createListing("Small", 100.0, "Addr1",
                40.0, 2, 1, BuildingType.CONDO, true);
        Listing l2 = createListing("Large", 200.0, "Addr2",
                100.0, 3, 2, BuildingType.HOUSE, true);

        InMemoryListingDataAccess dao = new InMemoryListingDataAccess(
                Arrays.asList(l1, l2));
        FakeDistanceService distanceService = new FakeDistanceService();
        RecordingPresenter presenter = new RecordingPresenter();

        FilterListingsInteractor interactor =
                new FilterListingsInteractor(dao, distanceService, presenter);

        FilterListingsInputData input = new FilterListingsInputData(
                null, null, null, null,
                80.0,
                null,
                null, null, null
        );

        interactor.execute(input);

        assertNull(presenter.lastError);
        assertNotNull(presenter.lastOutput);
        ArrayList<Listing> results = presenter.lastOutput.getListings();
        assertEquals(1, results.size());
        assertEquals("Large", results.get(0).getName());
    }

    @Test
    public void testAreaFilterWithOnlyMaxArea() {
        Listing l1 = createListing("Small", 100.0, "Addr1",
                40.0, 2, 1, BuildingType.CONDO, true);
        Listing l2 = createListing("Large", 200.0, "Addr2",
                100.0, 3, 2, BuildingType.HOUSE, true);

        InMemoryListingDataAccess dao = new InMemoryListingDataAccess(
                Arrays.asList(l1, l2));
        FakeDistanceService distanceService = new FakeDistanceService();
        RecordingPresenter presenter = new RecordingPresenter();

        FilterListingsInteractor interactor =
                new FilterListingsInteractor(dao, distanceService, presenter);

        FilterListingsInputData input = new FilterListingsInputData(
                null, null, null, null,
                null,
                50.0,
                null, null, null
        );

        interactor.execute(input);

        assertNull(presenter.lastError);
        assertNotNull(presenter.lastOutput);
        ArrayList<Listing> results = presenter.lastOutput.getListings();
        assertEquals(1, results.size());
        assertEquals("Small", results.get(0).getName());
    }

    @Test
    public void testInactiveListingsAreFilteredByDAO() {
        Listing l1 = createListing("Active1", 100.0, "Addr1",
                50.0, 2, 1, BuildingType.CONDO, true);
        Listing l2 = createListing("Inactive", 150.0, "Addr2",
                60.0, 3, 2, BuildingType.HOUSE, false);
        Listing l3 = createListing("Active2", 120.0, "Addr3",
                55.0, 2, 1, BuildingType.TOWNHOUSE, true);

        List<Listing> activeOnly = new ArrayList<>();
        activeOnly.add(l1);
        activeOnly.add(l3);

        InMemoryListingDataAccess dao = new InMemoryListingDataAccess(activeOnly);
        FakeDistanceService distanceService = new FakeDistanceService();
        RecordingPresenter presenter = new RecordingPresenter();

        FilterListingsInteractor interactor =
                new FilterListingsInteractor(dao, distanceService, presenter);

        FilterListingsInputData input = new FilterListingsInputData(
                null, null, null, null, null, null, null, null, null
        );

        interactor.execute(input);

        assertNull(presenter.lastError);
        assertNotNull(presenter.lastOutput);
        ArrayList<Listing> results = presenter.lastOutput.getListings();
        assertEquals(2, results.size());
        assertFalse(results.stream().anyMatch(l -> l.getName().equals("Inactive")));
    }

    @Test
    public void testBedroomsExactlyEqualsMinimum() {
        Listing l1 = createListing("Exactly2Bed", 100.0, "Addr1",
                50.0, 2, 1, BuildingType.CONDO, true);
        Listing l2 = createListing("Only1Bed", 150.0, "Addr2",
                60.0, 1, 1, BuildingType.CONDO, true);

        InMemoryListingDataAccess dao = new InMemoryListingDataAccess(
                Arrays.asList(l1, l2));
        FakeDistanceService distanceService = new FakeDistanceService();
        RecordingPresenter presenter = new RecordingPresenter();

        FilterListingsInteractor interactor =
                new FilterListingsInteractor(dao, distanceService, presenter);

        FilterListingsInputData input = new FilterListingsInputData(
                null, null, null, null, null, null,
                2,
                null, null
        );

        interactor.execute(input);

        assertNull(presenter.lastError);
        assertNotNull(presenter.lastOutput);
        ArrayList<Listing> results = presenter.lastOutput.getListings();
        assertEquals(1, results.size());
        assertEquals("Exactly2Bed", results.get(0).getName());
    }

    @Test
    public void testBathroomsExactlyEqualsMinimum() {
        Listing l1 = createListing("Exactly2Bath", 100.0, "Addr1",
                50.0, 3, 2, BuildingType.CONDO, true);
        Listing l2 = createListing("Only1Bath", 150.0, "Addr2",
                60.0, 3, 1, BuildingType.CONDO, true);

        InMemoryListingDataAccess dao = new InMemoryListingDataAccess(
                Arrays.asList(l1, l2));
        FakeDistanceService distanceService = new FakeDistanceService();
        RecordingPresenter presenter = new RecordingPresenter();

        FilterListingsInteractor interactor =
                new FilterListingsInteractor(dao, distanceService, presenter);

        FilterListingsInputData input = new FilterListingsInputData(
                null, null, null, null, null, null, null,
                2,
                null
        );

        interactor.execute(input);

        assertNull(presenter.lastError);
        assertNotNull(presenter.lastOutput);
        ArrayList<Listing> results = presenter.lastOutput.getListings();
        assertEquals(1, results.size());
        assertEquals("Exactly2Bath", results.get(0).getName());
    }

    @Test
    public void testDistanceExactlyEqualsMaximum() {
        Listing l1 = createListing("ExactlyAtLimit", 100.0, "NearAddress",
                50.0, 2, 1, BuildingType.CONDO, true);
        Listing l2 = createListing("BeyondLimit", 150.0, "FarAddress",
                60.0, 3, 2, BuildingType.HOUSE, true);

        InMemoryListingDataAccess dao = new InMemoryListingDataAccess(
                Arrays.asList(l1, l2));
        FakeDistanceService distanceService = new FakeDistanceService();
        RecordingPresenter presenter = new RecordingPresenter();

        FilterListingsInteractor interactor =
                new FilterListingsInteractor(dao, distanceService, presenter);

        FilterListingsInputData input = new FilterListingsInputData(
                "UserAddress",
                3.0,
                null, null, null, null, null, null, null
        );

        interactor.execute(input);

        assertNull(presenter.lastError);
        assertNotNull(presenter.lastOutput);
        ArrayList<Listing> results = presenter.lastOutput.getListings();
        assertEquals(1, results.size());
        assertEquals("ExactlyAtLimit", results.get(0).getName());
        assertEquals(3.0, results.get(0).getDistance(), 0.0001);
    }

    @Test
    public void testPriceExactlyEqualsMinimum() {
        Listing l1 = createListing("ExactlyMin", 100.0, "Addr1",
                50.0, 2, 1, BuildingType.CONDO, true);
        Listing l2 = createListing("BelowMin", 99.0, "Addr2",
                60.0, 3, 2, BuildingType.HOUSE, true);

        InMemoryListingDataAccess dao = new InMemoryListingDataAccess(
                Arrays.asList(l1, l2));
        FakeDistanceService distanceService = new FakeDistanceService();
        RecordingPresenter presenter = new RecordingPresenter();

        FilterListingsInteractor interactor =
                new FilterListingsInteractor(dao, distanceService, presenter);

        FilterListingsInputData input = new FilterListingsInputData(
                null, null,
                100.0,
                200.0,
                null, null, null, null, null
        );

        interactor.execute(input);

        assertNull(presenter.lastError);
        assertNotNull(presenter.lastOutput);
        ArrayList<Listing> results = presenter.lastOutput.getListings();
        assertEquals(1, results.size());
        assertEquals("ExactlyMin", results.get(0).getName());
    }

    @Test
    public void testPriceExactlyEqualsMaximum() {
        Listing l1 = createListing("ExactlyMax", 200.0, "Addr1",
                50.0, 2, 1, BuildingType.CONDO, true);
        Listing l2 = createListing("AboveMax", 201.0, "Addr2",
                60.0, 3, 2, BuildingType.HOUSE, true);

        InMemoryListingDataAccess dao = new InMemoryListingDataAccess(
                Arrays.asList(l1, l2));
        FakeDistanceService distanceService = new FakeDistanceService();
        RecordingPresenter presenter = new RecordingPresenter();

        FilterListingsInteractor interactor =
                new FilterListingsInteractor(dao, distanceService, presenter);

        FilterListingsInputData input = new FilterListingsInputData(
                null, null,
                100.0,
                200.0,
                null, null, null, null, null
        );

        interactor.execute(input);

        assertNull(presenter.lastError);
        assertNotNull(presenter.lastOutput);
        ArrayList<Listing> results = presenter.lastOutput.getListings();
        assertEquals(1, results.size());
        assertEquals("ExactlyMax", results.get(0).getName());
    }

    @Test
    public void testAreaExactlyEqualsMinimum() {
        Listing l1 = createListing("ExactlyMinArea", 100.0, "Addr1",
                50.0, 2, 1, BuildingType.CONDO, true);
        Listing l2 = createListing("BelowMinArea", 100.0, "Addr2",
                49.0, 3, 2, BuildingType.HOUSE, true);

        InMemoryListingDataAccess dao = new InMemoryListingDataAccess(
                Arrays.asList(l1, l2));
        FakeDistanceService distanceService = new FakeDistanceService();
        RecordingPresenter presenter = new RecordingPresenter();

        FilterListingsInteractor interactor =
                new FilterListingsInteractor(dao, distanceService, presenter);

        FilterListingsInputData input = new FilterListingsInputData(
                null, null, null, null,
                50.0,
                100.0,
                null, null, null
        );

        interactor.execute(input);

        assertNull(presenter.lastError);
        assertNotNull(presenter.lastOutput);
        ArrayList<Listing> results = presenter.lastOutput.getListings();
        assertEquals(1, results.size());
        assertEquals("ExactlyMinArea", results.get(0).getName());
    }

    @Test
    public void testAreaExactlyEqualsMaximum() {
        Listing l1 = createListing("ExactlyMaxArea", 100.0, "Addr1",
                100.0, 2, 1, BuildingType.CONDO, true);
        Listing l2 = createListing("AboveMaxArea", 100.0, "Addr2",
                101.0, 3, 2, BuildingType.HOUSE, true);

        InMemoryListingDataAccess dao = new InMemoryListingDataAccess(
                Arrays.asList(l1, l2));
        FakeDistanceService distanceService = new FakeDistanceService();
        RecordingPresenter presenter = new RecordingPresenter();

        FilterListingsInteractor interactor =
                new FilterListingsInteractor(dao, distanceService, presenter);

        FilterListingsInputData input = new FilterListingsInputData(
                null, null, null, null,
                50.0,
                100.0,
                null, null, null
        );

        interactor.execute(input);

        assertNull(presenter.lastError);
        assertNotNull(presenter.lastOutput);
        ArrayList<Listing> results = presenter.lastOutput.getListings();
        assertEquals(1, results.size());
        assertEquals("ExactlyMaxArea", results.get(0).getName());
    }

    @Test
    public void testSkipsInactiveListing() {
        Listing l1 = createListing("Active", 100.0, "Addr1",
                50.0, 2, 1, BuildingType.CONDO, true);
        Listing l2 = createListing("Inactive", 150.0, "Addr2",
                60.0, 3, 2, BuildingType.HOUSE, false);

        InMemoryListingDataAccess dao = new InMemoryListingDataAccess(
                Arrays.asList(l1, l2));
        FakeDistanceService distanceService = new FakeDistanceService();
        RecordingPresenter presenter = new RecordingPresenter();

        FilterListingsInteractor interactor =
                new FilterListingsInteractor(dao, distanceService, presenter);

        FilterListingsInputData input = new FilterListingsInputData(
                null, null, null, null, null, null, null, null, null
        );

        interactor.execute(input);

        assertNull(presenter.lastError);
        assertNotNull(presenter.lastOutput);
        ArrayList<Listing> results = presenter.lastOutput.getListings();
        assertEquals(1, results.size());
        assertEquals("Active", results.get(0).getName());
    }

    @Test
    public void testUserAddressProvidedButMaxDistanceIsNull() {
        Listing l1 = createListing("Listing1", 100.0, "NearAddress",
                50.0, 2, 1, BuildingType.CONDO, true);
        Listing l2 = createListing("Listing2", 150.0, "FarAddress",
                60.0, 3, 2, BuildingType.HOUSE, true);

        InMemoryListingDataAccess dao = new InMemoryListingDataAccess(
                Arrays.asList(l1, l2));
        FakeDistanceService distanceService = new FakeDistanceService();
        RecordingPresenter presenter = new RecordingPresenter();

        FilterListingsInteractor interactor =
                new FilterListingsInteractor(dao, distanceService, presenter);

        FilterListingsInputData input = new FilterListingsInputData(
                "UserAddress",
                null,
                null, null, null, null, null, null, null
        );

        interactor.execute(input);

        assertNull(presenter.lastError);
        assertNotNull(presenter.lastOutput);
        ArrayList<Listing> results = presenter.lastOutput.getListings();
        assertEquals(2, results.size());
        assertEquals(0.0, results.get(0).getDistance(), 0.0001);
        assertEquals(0.0, results.get(1).getDistance(), 0.0001);
    }

    @Test
    public void testSkipsListingWithEmptyWhitespaceAddress_WhenDistanceFilterActive() {
        Listing l1 = createListing("HasAddress", 100.0, "NearAddress",
                50.0, 2, 1, BuildingType.CONDO, true);
        Listing l2 = createListing("EmptyAddress", 150.0, "   ",
                60.0, 3, 2, BuildingType.HOUSE, true);

        InMemoryListingDataAccess dao = new InMemoryListingDataAccess(
                Arrays.asList(l1, l2));
        FakeDistanceService distanceService = new FakeDistanceService();
        RecordingPresenter presenter = new RecordingPresenter();

        FilterListingsInteractor interactor =
                new FilterListingsInteractor(dao, distanceService, presenter);

        FilterListingsInputData input = new FilterListingsInputData(
                "UserAddress",
                10.0,
                null, null, null, null, null, null, null
        );

        interactor.execute(input);

        assertNull(presenter.lastError);
        assertNotNull(presenter.lastOutput);
        ArrayList<Listing> results = presenter.lastOutput.getListings();
        assertEquals(1, results.size());
        assertEquals("HasAddress", results.get(0).getName());
    }
}