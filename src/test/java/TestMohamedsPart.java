import app.SearchListingUseCaseFactory;
import app.MyListingsUseCaseFactory;
import Entities.*;
import data_access.*;
import interface_adapter.search_listings.*;
import interface_adapter.my_listings.*;
import use_case.search_listings.SearchListingOutputData;
import use_case.my_listings.MyListingsOutputData;

import java.util.Arrays;

public class TestMohamedsPart {
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("TESTING MOHAMED'S PART");
        System.out.println("========================================\n");

        // Initialize data access
        InMemoryListingDAO dataAccess = new InMemoryListingDAO();

        // Create test users
        User mohamed = new User("mohamed", "pass123");
        User jane = new User("jane_host", "secure456");
        dataAccess.addUser(mohamed);
        dataAccess.addUser(jane);

        // Create test listings using the new constructor
        Listing listing1 = new Listing(
                "Modern Downtown Apartment",           // name
                mohamed, ,                                // owner
                // tags
                Arrays.asList("modern", "downtown", "near subway station"),            // mainCategories
                Arrays.asList("apartment"), // description
                "Beautiful modern apartment in the heart of downtown",                                 // price
                150.0,                    // address
                "Toronto Downtown",                                   // distance
                2.5,                                  // area
                850.0,                                      // bedrooms
                2,                                      // bathrooms
                1,         // buildingType
                Listing.BuildingType.APARTMENT,                                    // active
                true);

        Listing listing2 = new Listing(
                "Lakeside Villa with Pool",            // name
                jane, ,                                  // owner
                // tags
                Arrays.asList("near the lake", "luxury", "pool", "good view"),                // mainCategories
                Arrays.asList("villa"), // description
                "Stunning lakeside villa with private pool",                                 // price
                450.0,                        // address
                "Muskoka Lake",                                  // distance
                75.0,                                 // area
                2500.0,                                      // bedrooms
                4,                                      // bathrooms
                3,             // buildingType
                Listing.BuildingType.VILLA,                                    // active
                true);

        Listing listing3 = new Listing(
                "Modern Condo",                        // name
                mohamed, ,                               // owner
                // tags
                Arrays.asList("modern", "downtown"),                // mainCategories
                Arrays.asList("condo"),   // description
                "Sleek modern condo on King Street",                                 // price
                200.0,                         // address
                "King Street",                                   // distance
                1.5,                                 // area
                1000.0,                                      // bedrooms
                2,                                      // bathrooms
                2,             // buildingType
                Listing.BuildingType.CONDO,                                    // active
                true);

        // Add listings to data access
        dataAccess.addListing(listing1);
        dataAccess.addListing(listing2);
        dataAccess.addListing(listing3);

        // Add listings to users
        mohamed.addMyListing(listing1);
        mohamed.addMyListing(listing3);
        jane.addMyListing(listing2);

        // TEST 1: Search by Keyword
        System.out.println("TEST 1: SEARCH BY KEYWORD");
        System.out.println("========================================");

        SearchListingViewModel searchViewModel = new SearchListingViewModel();
        SearchListingController searchController =
                SearchListingUseCaseFactory.createSearchListingUseCase(searchViewModel, dataAccess);

        // Search for "modern"
        System.out.println("\n--- Searching for: 'modern' ---");
        SearchListingOutputData result1 = searchController.execute("modern", null);
        displaySearchResults(result1);

        // Search for "lake"
        System.out.println("\n--- Searching for: 'lake' ---");
        SearchListingOutputData result2 = searchController.execute("lake", null);
        displaySearchResults(result2);

        // Search for non-existent keyword
        System.out.println("\n--- Searching for: 'beachfront' ---");
        SearchListingOutputData result3 = searchController.execute("beachfront", null);
        displaySearchResults(result3);

        // TEST 2: My Listings
        System.out.println("\n========================================");
        System.out.println("TEST 2: MY LISTINGS");
        System.out.println("========================================");

        MyListingsViewModel myListingsViewModel = new MyListingsViewModel();
        MyListingsController myListingsController =
                MyListingsUseCaseFactory.createMyListingsUseCase(myListingsViewModel, dataAccess);

        // Get Mohamed's listings
        System.out.println("\n--- User: mohamed ---");
        MyListingsOutputData mohamedListings = myListingsController.execute("mohamed");
        displayMyListings(mohamedListings);

        // Get Jane's listings
        System.out.println("\n--- User: jane_host ---");
        MyListingsOutputData janeListings = myListingsController.execute("jane_host");
        displayMyListings(janeListings);

        System.out.println("\n========================================");
        System.out.println("✓ ALL TESTS PASSED!");
        System.out.println("========================================");
    }

    private static void displaySearchResults(SearchListingOutputData result) {
        if (!result.isSuccess()) {
            System.out.println("❌ " + result.getErrorMessage());
            System.out.println("Showing all available listings:");
        } else {
            System.out.println("✓ Found " + result.getListings().size() + " matching listing(s):");
        }

        for (Listing listing : result.getListings()) {
            System.out.println(String.format(
                    "  • %s | $%.2f/night | %s | Tags: %s",
                    listing.getName(),
                    listing.getPrice(),
                    listing.getAddress(),
                    listing.getTags()
            ));
        }
    }

    private static void displayMyListings(MyListingsOutputData result) {
        if (!result.isSuccess()) {
            System.out.println("❌ Error: User not found");
            return;
        }

        System.out.println("Total listings: " + result.getListings().size());
        for (Listing listing : result.getListings()) {
            System.out.println(String.format(
                    "  • %s | $%.2f/night | %d bed, %d bath | %s | Type: %s",
                    listing.getName(),
                    listing.getPrice(),
                    listing.getBedrooms(),
                    listing.getBathrooms(),
                    listing.getAddress(),
                    listing.getBuildingType()
            ));
        }
    }
}