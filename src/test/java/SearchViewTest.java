import app.SearchListingUseCaseFactory;
import data_access.InMemoryListingDAO;
import Entities.*;
import interface_adapter.search_listings.*;
import view.SearchView;

import javax.swing.*;

public class SearchViewTest {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("SEARCH VIEW UI TEST");
        System.out.println("========================================\n");

        // Initialize data access
        InMemoryListingDAO dataAccess = new InMemoryListingDAO();

        // Create test users
        User user1 = new User("mohamed", "pass123");
        User user2 = new User("jane", "secure456");
        dataAccess.addUser(user1);
        dataAccess.addUser(user2);

        // Create test listings with proper constructor
        Listing listing1 = new Listing(
                "Modern Downtown Apartment",
                user1,
                null,  // tags (will add separately)
                null,  // mainCategories
                "Beautiful modern apartment in downtown Toronto with subway access",
                150.0,
                "123 King Street, Toronto",
                2.5,   // distance
                850.0,
                2,
                1,
                Listing.BuildingType.APARTMENT,
                true
        );
        listing1.addTag("modern");
        listing1.addTag("downtown");
        listing1.addTag("near subway station");

        Listing listing2 = new Listing(
                "Lakeside Villa with Pool",
                user2,
                null,
                null,
                "Stunning luxury villa on Muskoka Lake with private pool",
                450.0,
                "456 Lake Road, Muskoka",
                75.0,
                2500.0,
                4,
                3,
                Listing.BuildingType.VILLA,
                true
        );
        listing2.addTag("near the lake");
        listing2.addTag("luxury");
        listing2.addTag("pool");
        listing2.addTag("good view");

        Listing listing3 = new Listing(
                "Modern Condo in Entertainment District",
                user1,
                null,
                null,
                "Stylish condo in the heart of entertainment district",
                200.0,
                "789 Queen Street West, Toronto",
                1.5,
                1000.0,
                2,
                2,
                Listing.BuildingType.CONDO,
                true
        );
        listing3.addTag("modern");
        listing3.addTag("downtown");
        listing3.addTag("near restaurants");

        Listing listing4 = new Listing(
                "Rustic Cottage in the Woods",
                user2,
                null,
                null,
                "Peaceful cottage surrounded by nature",
                120.0,
                "100 Forest Lane, Algonquin Park",
                150.0,
                1200.0,
                3,
                2,
                Listing.BuildingType.VILLA,  // Using VILLA as closest to cottage
                true
        );
        listing4.addTag("rural");
        listing4.addTag("nature");
        listing4.addTag("peaceful");

        // Add listings to data access
        dataAccess.addListing(listing1);
        dataAccess.addListing(listing2);
        dataAccess.addListing(listing3);
        dataAccess.addListing(listing4);

        user1.addMyListing(listing1);
        user1.addMyListing(listing3);
        user2.addMyListing(listing2);
        user2.addMyListing(listing4);

        System.out.println("✓ Test data created");
        System.out.println("  - 4 listings loaded");
        System.out.println("  - 2 users created\n");

        // Create view components
        SearchListingViewModel viewModel = new SearchListingViewModel();
        SearchListingController controller =
                SearchListingUseCaseFactory.createSearchListingUseCase(viewModel, dataAccess);

        SearchView searchView = new SearchView(viewModel, controller);

        System.out.println("✓ Search view created\n");

        // Create and show window
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("Airbnb Listing Browser - Search Test");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(900, 700);
                frame.setLocationRelativeTo(null);  // Center on screen
                frame.add(searchView);
                frame.setVisible(true);

                System.out.println("========================================");
                System.out.println("UI WINDOW OPENED");
                System.out.println("========================================");
                System.out.println("\nTry searching for:");
                System.out.println("  - 'modern' (should find 2 listings)");
                System.out.println("  - 'lake' (should find 1 listing)");
                System.out.println("  - 'downtown' (should find 2 listings)");
                System.out.println("  - 'beachfront' (should show error + all listings)");
                System.out.println("\nClose the window to exit.");
            }
        });
    }
}
