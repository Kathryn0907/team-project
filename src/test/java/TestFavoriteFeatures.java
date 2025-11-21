import app.AppBuilder;
import Entities.*;
import javax.swing.*;
import java.util.Arrays;

/**
 * Complete Integration Test for Favorites Feature
 * Tests Use Case 9 (Save Favorite) and Use Case 14 (Check Favorite)
 *
 * Run this to verify everything works together!
 */
public class TestFavoriteFeatures {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("COMPLETE FAVORITES INTEGRATION TEST");
        System.out.println("========================================\n");

        // Create the application builder
        AppBuilder appBuilder = new AppBuilder();

        // Add test data BEFORE building
        setupTestData(appBuilder);

        // Build the complete application
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                System.out.println("Building application with favorites...\n");

                JFrame application = appBuilder
                        .addLoginView()
                        .addSignupView()
                        .addLoggedInView()
                        .addCheckFavoriteView()        // Add favorites view
                        .addSignupUseCase()
                        .addLoginUseCase()
                        .addSearchListingUseCase()
                        .addSaveFavoriteUseCase()      // Add save favorite use case
                        .addCheckFavoriteUseCase()     // Add check favorite use case
                        .rebuildLoggedInView()         // Rebuild with all controllers
                        .build();

                application.pack();
                application.setSize(1200, 800);
                application.setLocationRelativeTo(null);
                application.setVisible(true);

                System.out.println("✓ Application window opened\n");
                printTestInstructions();
            }
        });
    }

    private static void setupTestData(AppBuilder appBuilder) {
        System.out.println("Setting up test data...\n");

        // Create test users
        User testUser = new User("jonathan", "pass123");
        User host1 = new User("alice", "secure456");

        appBuilder.userDataAccessObject.save(testUser);
        appBuilder.userDataAccessObject.save(host1);
        System.out.println("✓ Created users: jonathan, alice");

        // Create test listings
        Listing listing1 = new Listing(
                "Beachfront Paradise Condo",
                host1,
                null,
                Arrays.asList("beach", "ocean view", "luxury", "swimming pool"),
                Arrays.asList("condo"),
                "Stunning beachfront condo with direct ocean views and private pool access",
                250.0,
                "123 Beach Drive, Miami",
                5.0,
                1200.0,
                2,
                2,
                Listing.BuildingType.CONDO,
                true
        );

        Listing listing2 = new Listing(
                "Mountain Cabin Retreat",
                host1,
                null,
                Arrays.asList("mountain", "nature", "peaceful", "hiking"),
                Arrays.asList("villa"),
                "Cozy mountain cabin perfect for nature lovers and hiking enthusiasts",
                180.0,
                "456 Mountain Road, Aspen",
                50.0,
                1500.0,
                3,
                2,
                Listing.BuildingType.VILLA,
                true
        );

        Listing listing3 = new Listing(
                "Modern Downtown Loft",
                host1,
                null,
                Arrays.asList("downtown", "modern", "urban", "nightlife"),
                Arrays.asList("apartment"),
                "Sleek modern loft in the heart of downtown with amazing city views",
                200.0,
                "789 Main Street, Toronto",
                1.0,
                900.0,
                1,
                1,
                Listing.BuildingType.APARTMENT,
                true
        );

        Listing listing4 = new Listing(
                "Lakeside Family House",
                host1,
                null,
                Arrays.asList("lake", "family-friendly", "spacious", "dock"),
                Arrays.asList("house"),
                "Beautiful family house on the lake with private dock",
                320.0,
                "111 Lake View Drive, Muskoka",
                75.0,
                2200.0,
                4,
                3,
                Listing.BuildingType.HOUSE,
                true
        );

        Listing listing5 = new Listing(
                "Cozy Studio Apartment",
                host1,
                null,
                Arrays.asList("studio", "cozy", "affordable", "transit"),
                Arrays.asList("apartment"),
                "Perfect starter apartment near transit and amenities",
                120.0,
                "222 Transit Way, Toronto",
                3.0,
                500.0,
                1,
                1,
                Listing.BuildingType.STUDIO,
                true
        );

        // Add all listings
        appBuilder.listingDataAccessObject.addListing(listing1);
        appBuilder.listingDataAccessObject.addListing(listing2);
        appBuilder.listingDataAccessObject.addListing(listing3);
        appBuilder.listingDataAccessObject.addListing(listing4);
        appBuilder.listingDataAccessObject.addListing(listing5);

        System.out.println("✓ Created 5 test listings\n");
        System.out.println("Listings available:");
        System.out.println("  1. Beachfront Paradise Condo ($250/night)");
        System.out.println("  2. Mountain Cabin Retreat ($180/night)");
        System.out.println("  3. Modern Downtown Loft ($200/night)");
        System.out.println("  4. Lakeside Family House ($320/night)");
        System.out.println("  5. Cozy Studio Apartment ($120/night)\n");
    }

    private static void printTestInstructions() {
        System.out.println("========================================");
        System.out.println("TEST INSTRUCTIONS");
        System.out.println("========================================\n");

        System.out.println("STEP 1: LOGIN");
        System.out.println("  - Click 'Cancel' to go to login screen");
        System.out.println("  - Login with:");
        System.out.println("    Username: jonathan");
        System.out.println("    Password: pass123\n");

        System.out.println("STEP 2: VIEW LISTINGS");
        System.out.println("  - You should see 5 listings displayed");
        System.out.println("  - Each listing has an '❤ Add to Favorites' button");
        System.out.println("  - Notice the '❤ View My Favorites' button (top right)\n");

        System.out.println("STEP 3: ADD FAVORITES");
        System.out.println("  ✓ Click '❤ Add to Favorites' on 'Beachfront Paradise Condo'");
        System.out.println("    → You should see a success dialog");
        System.out.println("  ✓ Click '❤ Add to Favorites' on 'Mountain Cabin Retreat'");
        System.out.println("    → Another success dialog");
        System.out.println("  ✓ Try clicking the same listing again");
        System.out.println("    → Should say 'already in favourites'\n");

        System.out.println("STEP 4: VIEW FAVORITES");
        System.out.println("  ✓ Click '❤ View My Favorites' (top right)");
        System.out.println("    → Screen changes to Favorites view");
        System.out.println("    → You should see your 2 favorited listings");
        System.out.println("    → Title shows count: 'My Favorite Listings (2)'\n");

        System.out.println("STEP 5: TEST SEARCH + FAVORITES");
        System.out.println("  - (You need to manually navigate back to logged in view)");
        System.out.println("  - Search for 'lake'");
        System.out.println("  - Add 'Lakeside Family House' to favorites");
        System.out.println("  - View favorites again");
        System.out.println("  - Should now see 3 listings\n");

        System.out.println("========================================");
        System.out.println("WHAT TO VERIFY");
        System.out.println("========================================\n");

        System.out.println("✓ Each listing card has 'Add to Favorites' button");
        System.out.println("✓ Clicking button shows success message");
        System.out.println("✓ Can't add same listing twice");
        System.out.println("✓ 'View My Favorites' button navigates correctly");
        System.out.println("✓ Favorites view shows all favorited listings");
        System.out.println("✓ Favorites count is accurate");
        System.out.println("✓ Can favorite listings after searching\n");

        System.out.println("========================================");
        System.out.println("EXPECTED BEHAVIOR");
        System.out.println("========================================\n");

        System.out.println("USE CASE 9 (Save Favorite):");
        System.out.println("  ✅ User can add any active listing to favorites");
        System.out.println("  ✅ System prevents duplicate favorites");
        System.out.println("  ✅ System validates user and listing exist");
        System.out.println("  ✅ User gets immediate feedback\n");

        System.out.println("USE CASE 14 (Check Favorite):");
        System.out.println("  ✅ User can view all their favorites");
        System.out.println("  ✅ System shows only active listings");
        System.out.println("  ✅ System displays count of favorites");
        System.out.println("  ✅ Empty state handled gracefully\n");

        System.out.println("========================================\n");
        System.out.println("🚀 Ready to test! Follow the steps above.\n");
        System.out.println("Close the window when finished testing.\n");
        System.out.println("========================================\n");
    }
}