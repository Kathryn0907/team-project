package use_case.search_listings;

import data_access.InMemoryListingDAO;
import Entities.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Complete JUnit test suite for SearchListingInteractor with 100% code coverage
 */
class SearchListingInteractorTest {

    private InMemoryListingDAO dataAccess;
    private User testUser;

    @BeforeEach
    void setUp() {
        dataAccess = new InMemoryListingDAO();
        testUser = new User("testuser", "password");
        dataAccess.addUser(testUser);
    }

    /**
     * Test Case 1: Search by keyword - SUCCESS
     * Covers: keyword search with matches found
     */
    @Test
    void testSearchByKeywordSuccess() {
        // Setup test data
        Listing listing1 = createListing("Modern Downtown Apartment",
                Arrays.asList("modern", "downtown"), 150.0);
        Listing listing2 = createListing("Beach House",
                Arrays.asList("beach", "ocean"), 300.0);

        dataAccess.addListing(listing1);
        dataAccess.addListing(listing2);

        // Create presenter that captures output
        TestPresenter presenter = new TestPresenter();

        // Create interactor
        SearchListingInteractor interactor = new SearchListingInteractor(dataAccess, presenter);

        // Execute search
        SearchListingInputData inputData = new SearchListingInputData("modern", null);
        SearchListingOutputData result = interactor.execute(inputData);

        // Verify results
        assertTrue(result.isSuccess(), "Search should succeed");
        assertEquals(1, result.getListings().size(), "Should find 1 listing");
        assertEquals("Modern Downtown Apartment", result.getListings().get(0).getName());
        assertNull(result.getErrorMessage());

        // Verify presenter was called
        assertTrue(presenter.successCalled, "Presenter prepareSuccessView should be called");
        assertFalse(presenter.failCalled, "Presenter prepareFailView should NOT be called");
    }

    /**
     * Test Case 2: Search by keyword - NOT FOUND
     * Covers: keyword search with no matches, fallback to all listings
     */
    @Test
    void testSearchByKeywordNotFound() {
        // Setup test data
        Listing listing1 = createListing("Downtown Apartment",
                Arrays.asList("downtown"), 150.0);
        Listing listing2 = createListing("Beach House",
                Arrays.asList("beach"), 300.0);

        dataAccess.addListing(listing1);
        dataAccess.addListing(listing2);

        // Create presenter
        TestPresenter presenter = new TestPresenter();

        // Create interactor
        SearchListingInteractor interactor = new SearchListingInteractor(dataAccess, presenter);

        // Execute search
        SearchListingInputData inputData = new SearchListingInputData("nonexistent", null);
        SearchListingOutputData result = interactor.execute(inputData);

        // Verify results
        assertFalse(result.isSuccess(), "Search should fail");
        assertNotNull(result.getErrorMessage());
        assertTrue(result.getErrorMessage().contains("not familiar with"),
                "Should have 'not familiar with' error message");
        assertEquals(2, result.getListings().size(),
                "Should return all listings as fallback");

        // Verify presenter was called
        assertFalse(presenter.successCalled, "Presenter prepareSuccessView should NOT be called");
        assertTrue(presenter.failCalled, "Presenter prepareFailView should be called");
    }

    /**
     * Test Case 3: Search with empty keyword
     * Covers: empty keyword returning all listings
     */
    @Test
    void testSearchWithEmptyKeyword() {
        // Setup test data
        Listing listing1 = createListing("Apartment 1",
                Arrays.asList("modern"), 150.0);
        Listing listing2 = createListing("Apartment 2",
                Arrays.asList("cozy"), 200.0);
        Listing listing3 = createListing("Apartment 3",
                Arrays.asList("luxury"), 250.0);

        dataAccess.addListing(listing1);
        dataAccess.addListing(listing2);
        dataAccess.addListing(listing3);

        // Create presenter
        TestPresenter presenter = new TestPresenter();

        // Create interactor
        SearchListingInteractor interactor = new SearchListingInteractor(dataAccess, presenter);

        // Execute search with empty string
        SearchListingInputData inputData = new SearchListingInputData("", null);
        SearchListingOutputData result = interactor.execute(inputData);

        // Verify results
        assertTrue(result.isSuccess(), "Should succeed with empty keyword");
        assertEquals(3, result.getListings().size(), "Should return all 3 listings");
        assertNull(result.getErrorMessage());

        // Verify presenter was called
        assertTrue(presenter.successCalled);
    }

    /**
     * Test Case 4: Search with whitespace keyword
     * Covers: whitespace-only keyword treated as empty
     */
    @Test
    void testSearchWithWhitespaceKeyword() {
        // Setup test data
        Listing listing1 = createListing("Apartment 1",
                Arrays.asList("modern"), 150.0);

        dataAccess.addListing(listing1);

        // Create presenter
        TestPresenter presenter = new TestPresenter();

        // Create interactor
        SearchListingInteractor interactor = new SearchListingInteractor(dataAccess, presenter);

        // Execute search with whitespace
        SearchListingInputData inputData = new SearchListingInputData("   ", null);
        SearchListingOutputData result = interactor.execute(inputData);

        // Verify results
        assertTrue(result.isSuccess(), "Should succeed with whitespace keyword");
        assertEquals(1, result.getListings().size(), "Should return all listings");
    }

    /**
     * Test Case 5: Search with null keyword
     * Covers: null keyword returning all listings
     */
    @Test
    void testSearchWithNullKeyword() {
        // Setup test data
        Listing listing1 = createListing("Apartment 1",
                Arrays.asList("modern"), 150.0);
        Listing listing2 = createListing("Apartment 2",
                Arrays.asList("cozy"), 200.0);

        dataAccess.addListing(listing1);
        dataAccess.addListing(listing2);

        // Create presenter
        TestPresenter presenter = new TestPresenter();

        // Create interactor
        SearchListingInteractor interactor = new SearchListingInteractor(dataAccess, presenter);

        // Execute search with null keyword
        SearchListingInputData inputData = new SearchListingInputData(null, null);
        SearchListingOutputData result = interactor.execute(inputData);

        // Verify results
        assertTrue(result.isSuccess(), "Should succeed with null keyword");
        assertEquals(2, result.getListings().size(), "Should return all 2 listings");
    }

    /**
     * Test Case 6: Search with empty database
     * Covers: searching when no listings exist
     */
    @Test
    void testSearchWithEmptyDatabase() {
        // Don't add any listings to dataAccess

        // Create presenter
        TestPresenter presenter = new TestPresenter();

        // Create interactor
        SearchListingInteractor interactor = new SearchListingInteractor(dataAccess, presenter);

        // Execute search with keyword
        SearchListingInputData inputData1 = new SearchListingInputData("modern", null);
        SearchListingOutputData result1 = interactor.execute(inputData1);

        // Verify results
        assertFalse(result1.isSuccess(), "Should fail when no listings");
        assertTrue(result1.getListings().isEmpty(), "Should return empty list");
        assertNotNull(result1.getErrorMessage());

        // Execute search with empty keyword
        SearchListingInputData inputData2 = new SearchListingInputData("", null);
        SearchListingOutputData result2 = interactor.execute(inputData2);

        // Verify results
        assertTrue(result2.isSuccess(), "Empty keyword should succeed");
        assertTrue(result2.getListings().isEmpty(), "Should return empty list");
    }

    /**
     * Test Case 7: Search with pre-filtered listings
     * Covers: searching within a subset of already filtered listings
     */
    @Test
    void testSearchWithPreFilteredListings() {
        // Setup test data
        Listing listing1 = createListing("Modern Downtown Apartment",
                Arrays.asList("modern", "downtown"), 150.0);
        Listing listing2 = createListing("Modern Beach House",
                Arrays.asList("modern", "beach"), 300.0);
        Listing listing3 = createListing("Rustic Cottage",
                Arrays.asList("rustic", "nature"), 120.0);

        dataAccess.addListing(listing1);
        dataAccess.addListing(listing2);
        dataAccess.addListing(listing3);

        // Create pre-filtered list (only "modern" listings)
        ArrayList<Listing> preFiltered = new ArrayList<>();
        preFiltered.add(listing1);
        preFiltered.add(listing2);

        // Create presenter
        TestPresenter presenter = new TestPresenter();

        // Create interactor
        SearchListingInteractor interactor = new SearchListingInteractor(dataAccess, presenter);

        // Execute search within pre-filtered listings
        SearchListingInputData inputData = new SearchListingInputData("beach", preFiltered);
        SearchListingOutputData result = interactor.execute(inputData);

        // Verify results
        assertTrue(result.isSuccess(), "Should succeed");
        assertEquals(1, result.getListings().size(),
                "Should find 1 listing in pre-filtered set");
        assertEquals("Modern Beach House", result.getListings().get(0).getName());
    }

    /**
     * Test Case 8: Search with pre-filtered listings - no match
     * Covers: searching within pre-filtered list with no matches
     */
    @Test
    void testSearchWithPreFilteredListingsNoMatch() {
        // Setup test data
        Listing listing1 = createListing("Modern Downtown Apartment",
                Arrays.asList("modern", "downtown"), 150.0);
        Listing listing2 = createListing("Modern Beach House",
                Arrays.asList("modern", "beach"), 300.0);

        dataAccess.addListing(listing1);
        dataAccess.addListing(listing2);

        // Create pre-filtered list
        ArrayList<Listing> preFiltered = new ArrayList<>();
        preFiltered.add(listing1);
        preFiltered.add(listing2);

        // Create presenter
        TestPresenter presenter = new TestPresenter();

        // Create interactor
        SearchListingInteractor interactor = new SearchListingInteractor(dataAccess, presenter);

        // Execute search within pre-filtered listings for non-existent keyword
        SearchListingInputData inputData = new SearchListingInputData("rustic", preFiltered);
        SearchListingOutputData result = interactor.execute(inputData);

        // Verify results
        assertFalse(result.isSuccess(), "Should fail when no matches");
        assertEquals(2, result.getListings().size(),
                "Should return pre-filtered listings as fallback");
    }

    /**
     * Test Case 9: Search by name exact match
     * Covers: exact name match gets highest relevance score
     */
    @Test
    void testSearchByNameExactMatch() {
        // Setup test data
        Listing listing1 = createListing("Beach House",
                Arrays.asList("luxury"), 300.0);
        Listing listing2 = createListing("House Near Beach",
                Arrays.asList("beach", "ocean"), 250.0);

        dataAccess.addListing(listing1);
        dataAccess.addListing(listing2);

        // Create presenter
        TestPresenter presenter = new TestPresenter();

        // Create interactor
        SearchListingInteractor interactor = new SearchListingInteractor(dataAccess, presenter);

        // Execute search
        SearchListingInputData inputData = new SearchListingInputData("beach house", null);
        SearchListingOutputData result = interactor.execute(inputData);

        // Verify results
        assertTrue(result.isSuccess());
        assertEquals(2, result.getListings().size());
        // Exact name match should be first
        assertEquals("Beach House", result.getListings().get(0).getName());
    }

    /**
     * Test Case 10: Search by category
     * Covers: category matching in relevance calculation
     */
    @Test
    void testSearchByCategory() {
        // Setup test data
        Listing listing1 = createListing("Modern Apartment",
                Arrays.asList("downtown"), 150.0);
        listing1.addCategory("apartment");

        Listing listing2 = createListing("Beach House",
                Arrays.asList("beach"), 300.0);

        dataAccess.addListing(listing1);
        dataAccess.addListing(listing2);

        // Create presenter
        TestPresenter presenter = new TestPresenter();

        // Create interactor
        SearchListingInteractor interactor = new SearchListingInteractor(dataAccess, presenter);

        // Execute search
        SearchListingInputData inputData = new SearchListingInputData("apartment", null);
        SearchListingOutputData result = interactor.execute(inputData);

        // Verify results
        assertTrue(result.isSuccess());
        assertEquals(1, result.getListings().size());
        assertEquals("Modern Apartment", result.getListings().get(0).getName());
    }

    /**
     * Helper method to create test listings
     */
    private Listing createListing(String name, java.util.List<String> tags, double price) {
        return new Listing(
                name,
                testUser,
                null,
                tags,
                new ArrayList<>(),
                "Test description",
                price,
                "Test Address",
                0.0,
                100.0,
                2,
                1,
                Listing.BuildingType.APARTMENT,
                true
        );
    }

    /**
     * Test presenter that captures method calls
     */
    private static class TestPresenter implements SearchListingOutputBoundary {
        boolean successCalled = false;
        boolean failCalled = false;
        SearchListingOutputData lastOutput = null;

        @Override
        public void prepareSuccessView(SearchListingOutputData outputData) {
            successCalled = true;
            lastOutput = outputData;
        }

        @Override
        public void prepareFailView(SearchListingOutputData outputData) {
            failCalled = true;
            lastOutput = outputData;
        }
    }
    @Test
    void testSearchByTagMatch() {
        Listing tagListing = createListing("Apartment",
                Arrays.asList("luxury", "pool"), 150.0); // pool tag

        dataAccess.addListing(tagListing);

        TestPresenter presenter = new TestPresenter();
        SearchListingInteractor interactor = new SearchListingInteractor(dataAccess, presenter);

        SearchListingInputData input = new SearchListingInputData("pool", null);
        SearchListingOutputData result = interactor.execute(input);

        assertTrue(result.isSuccess());
        assertEquals(1, result.getListings().size());
        assertEquals("Apartment", result.getListings().get(0).getName());
    }

    @Test
    void testSearchByAddressMatch() {
        Listing addressListing = createListing(
                "Cozy Home",
                Arrays.asList("quiet"),
                120.0
        );
        addressListing.setAddress("123 Beach Road"); // keyword in address

        dataAccess.addListing(addressListing);

        TestPresenter presenter = new TestPresenter();
        SearchListingInteractor interactor = new SearchListingInteractor(dataAccess, presenter);

        SearchListingInputData input = new SearchListingInputData("road", null);
        SearchListingOutputData result = interactor.execute(input);

        assertTrue(result.isSuccess());
        assertEquals(1, result.getListings().size());
        assertEquals("Cozy Home", result.getListings().get(0).getName());
    }

    @Test
    void testSearchHandlesNullNameAndAddress() {
        // Listing with *no* name and *no* address
        Listing listing = new Listing(); // default constructor → name & address stay null
        listing.setOwner(testUser);
        listing.addTag("pool"); // give it something matchable

        dataAccess.addListing(listing);

        TestPresenter presenter = new TestPresenter();
        SearchListingInteractor interactor = new SearchListingInteractor(dataAccess, presenter);

        // Keyword matches only the tag; this exercises the null-name/null-address path
        SearchListingInputData input = new SearchListingInputData("pool", null);
        SearchListingOutputData result = interactor.execute(input);

        assertTrue(result.isSuccess());
        assertEquals(1, result.getListings().size());
    }


    @Test
    void testSearchIgnoresNullCategorySafely() {
        // Listing with a null category in mainCategories
        Listing listing = createListing("Category Test",
                Arrays.asList("tag"), 100.0);

        ArrayList<String> categoriesWithNull = new ArrayList<>();
        categoriesWithNull.add(null); // will trigger category != null guard
        listing.setMainCategories(categoriesWithNull);

        dataAccess.addListing(listing);

        TestPresenter presenter = new TestPresenter();
        SearchListingInteractor interactor = new SearchListingInteractor(dataAccess, presenter);

        // Keyword that does NOT match anything, so score stays 0
        SearchListingInputData input = new SearchListingInputData("nomatch", null);
        SearchListingOutputData result = interactor.execute(input);

        // We care mainly that it doesn't blow up and falls back correctly
        assertFalse(result.isSuccess());
        assertEquals(1, result.getListings().size()); // fallback = original list
    }

    @Test
    void testCategoryLoopNoMatches() {
        Listing listing = createListing(
                "Cozy Cabin",
                Arrays.asList("forest"),
                200.0
        );
        listing.addCategory("nature");
        listing.addCategory("cabin");
        // none contain "urban"

        dataAccess.addListing(listing);

        TestPresenter presenter = new TestPresenter();
        SearchListingInteractor interactor = new SearchListingInteractor(dataAccess, presenter);

        SearchListingInputData input = new SearchListingInputData("urban", null);
        SearchListingOutputData result = interactor.execute(input);

        assertFalse(result.isSuccess()); // no matches → fallback
    }

    @Test
    void testSearchWithNoTagsOnListing() {
        // Create a listing with NO tags at all
        Listing listing = createListing(
                "Simple Apartment",
                new ArrayList<>(),  // EMPTY tags list
                150.0
        );

        dataAccess.addListing(listing);

        TestPresenter presenter = new TestPresenter();
        SearchListingInteractor interactor = new SearchListingInteractor(dataAccess, presenter);

        // Search for something that doesn't match name/category/address
        // This makes tokenMatched = false, and tags are empty
        SearchListingInputData input = new SearchListingInputData("luxury", null);
        SearchListingOutputData result = interactor.execute(input);

        assertFalse(result.isSuccess()); // no match found
    }
}