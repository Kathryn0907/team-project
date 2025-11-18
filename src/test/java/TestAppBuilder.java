import app.AppBuilder;
import Entities.*;
import data_access.InMemoryListingDataAccessObject;
import data_access.InMemoryUserDataAccessObject;

import javax.swing.*;
import java.util.Arrays;

public class TestAppBuilder {
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("TESTING APP BUILDER - LOGGED IN VIEW");
        System.out.println("========================================\n");

        // Create test data BEFORE building the app
        InMemoryUserDataAccessObject userDAO = new InMemoryUserDataAccessObject();
        InMemoryListingDataAccessObject listingDAO = new InMemoryListingDataAccessObject();

        // Create test users
        User testUser = new User("testuser", "password123");
        User host1 = new User("host1", "pass456");
        userDAO.save(testUser);
        userDAO.save(host1);

        // Create test listings
        Listing listing1 = new Listing(
                "Downtown Luxury Apartment",
                host1,
                null,
                Arrays.asList("modern", "downtown", "luxury"),
                Arrays.asList("apartment"),
                "Beautiful modern apartment in downtown",
                200.0,
                "123 King St, Toronto",
                2.0,
                1000.0,
                2,
                2,
                Listing.BuildingType.APARTMENT,
                true
        );

        Listing listing2 = new Listing(
                "Cozy Beach House",
                host1,
                null,
                Arrays.asList("beach", "cozy", "family-friendly"),
                Arrays.asList("house"),
                "Perfect beach getaway",
                350.0,
                "456 Beach Rd, Miami",
                50.0,
                1500.0,
                3,
                2,
                Listing.BuildingType.HOUSE,
                true
        );

        Listing listing3 = new Listing(
                "Mountain Cabin Retreat",
                host1,
                null,
                Arrays.asList("mountain", "nature", "peaceful"),
                Arrays.asList("villa"),
                "Peaceful mountain retreat",
                180.0,
                "789 Mountain Rd, Aspen",
                100.0,
                1200.0,
                2,
                1,
                Listing.BuildingType.VILLA,
                true
        );

        listingDAO.addListing(listing1);
        listingDAO.addListing(listing2);
        listingDAO.addListing(listing3);

        host1.addMyListing(listing1);
        host1.addMyListing(listing2);
        host1.addMyListing(listing3);

        System.out.println("✓ Test data created:");
        System.out.println("  - Users: testuser, host1");
        System.out.println("  - Listings: 3 active listings");
        System.out.println();

        // Build and launch the application
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                System.out.println("Building application...");

                AppBuilder appBuilder = new AppBuilder();

                // Inject test data into the builder's DAOs
                appBuilder.userDataAccessObject.save(testUser);
                appBuilder.userDataAccessObject.save(host1);
                appBuilder.listingDataAccessObject.addListing(listing1);
                appBuilder.listingDataAccessObject.addListing(listing2);
                appBuilder.listingDataAccessObject.addListing(listing3);
                host1.addMyListing(listing1);
                host1.addMyListing(listing2);
                host1.addMyListing(listing3);

                JFrame application = appBuilder
                        .addLoginView()
                        .addSignupView()
                        .addLoggedInView()
                        .addSignupUseCase()
                        .addLoginUseCase()
                        .addSearchListingUseCase()
                        .build();

                application.pack();
                application.setSize(1200, 800);
                application.setLocationRelativeTo(null);
                application.setVisible(true);

                System.out.println("✓ Application window opened");
                System.out.println();
                System.out.println("========================================");
                System.out.println("TEST INSTRUCTIONS:");
                System.out.println("========================================");
                System.out.println("1. You should see the SIGNUP view first");
                System.out.println("2. Click 'Cancel' to go to LOGIN view");
                System.out.println("3. Login with:");
                System.out.println("   Username: testuser");
                System.out.println("   Password: password123");
                System.out.println("4. After login, you should see:");
                System.out.println("   - Filter panel on the LEFT");
                System.out.println("   - Search bar at the TOP");
                System.out.println("   - ALL 3 LISTINGS displayed immediately");
                System.out.println("5. Try searching for 'beach' or 'mountain'");
                System.out.println("6. Try using the filter panel");
                System.out.println();
                System.out.println("✓ Close window to exit");
                System.out.println("========================================");
            }
        });
    }
}