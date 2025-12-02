import app.AppBuilder;
import Entities.*;
import javax.swing.*;
import java.util.Arrays;

/**
 * Integration Test for Favorites Feature
 * Works with your current AppBuilder (Mongo + InMemory)
 */
public class TestFavoriteFeatures {

    public static void main(String[] args) {

        System.out.println("========================================");
        System.out.println("FAVORITES FEATURE INTEGRATION TEST");
        System.out.println("========================================");

        AppBuilder app = new AppBuilder();

        // BEFORE building: insert test data into IN-MEMORY DAOs
        setupTestData(app);

        SwingUtilities.invokeLater(() -> {

            JFrame frame = app
                    .addSignupView()
                    .addLoginView()
                    .addLoggedInView()

                    // Use cases MUST be added before adding FavoritesView
                    .addSignupUseCase()
                    .addLoginUseCase()
                    .addSearchListingUseCase()
                    .addSaveFavoriteUseCase()
                    .addCheckFavoriteUseCase()

                    // Controllers now exist → rebuild LoggedInView to inject them
                    .rebuildLoggedInView()

                    // Now that controllers exist → safe to add FavoritesView
                    .addCheckFavoriteView()

                    .build();

            frame.pack();
            frame.setSize(1200, 800);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            System.out.println("\n✓ Application started.\nFollow the test steps in console.\n");
            printTestInstructions();
        });
    }

    // -------------------------------------
    // Insert In-Memory Test Data
    // -------------------------------------
    private static void setupTestData(AppBuilder app) {

        System.out.println("\nSetting up test data...");

        // Test users
        User testUser = new User("jonathan", "pass123");
        User host1 = new User("alice", "secure456");

        app.userDataAccessObject.save(testUser);
        app.userDataAccessObject.save(host1);

        // Test listings
        Listing l1 = new Listing(
                "Beachfront Paradise Condo", host1, null,
                Arrays.asList("beach","ocean","luxury"), Arrays.asList("condo"),
                "Beautiful beachfront condo", 250.0,
                "123 Beach Drive", 5.0, 1200.0,
                2, 2, Listing.BuildingType.CONDO, true
        );

        Listing l2 = new Listing(
                "Mountain Cabin Retreat", host1, null,
                Arrays.asList("mountain","nature"), Arrays.asList("villa"),
                "Cozy mountain cabin", 180.0,
                "456 Mountain Road", 50.0, 1500.0,
                3, 2, Listing.BuildingType.VILLA, true
        );

        Listing l3 = new Listing(
                "Modern Downtown Loft", host1, null,
                Arrays.asList("modern","urban"), Arrays.asList("apartment"),
                "Downtown loft", 200.0,
                "789 Main Street", 1.0, 900.0,
                1, 1, Listing.BuildingType.APARTMENT, true
        );

        app.listingDataAccessObject.addListing(l1);
        app.listingDataAccessObject.addListing(l2);
        app.listingDataAccessObject.addListing(l3);

        System.out.println("✓ Test data created");
    }

    // -------------------------------------
    // Instructions for tester
    // -------------------------------------
    private static void printTestInstructions() {

        System.out.println("\n========================================");
        System.out.println("HOW TO TEST FAVORITES");
        System.out.println("========================================");

        System.out.println("1. CANCEL → Go to Login screen");
        System.out.println("2. Login with:");
        System.out.println("   Username: jonathan");
        System.out.println("   Password: pass123\n");

        System.out.println("3. Add favorites:");
        System.out.println("   - Beachfront Paradise Condo");
        System.out.println("   - Mountain Cabin Retreat\n");

        System.out.println("4. Click '❤ View My Favorites'");
        System.out.println("   EXPECTED:");
        System.out.println("   - 2 listings displayed");
        System.out.println("   - Buttons show Remove / View Details\n");

        System.out.println("========================================");
    }
}
