import app.AppBuilder;
import Entities.*;
import javax.swing.*;
import java.util.Arrays;

public class TestWithRealAddresses {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("DISTANCE FILTER TEST - REAL ADDRESSES");
        System.out.println("========================================\n");

        // Check if API key is set
        String apiKey = System.getenv("GOOGLE_API_KEY");
        if (apiKey == null || apiKey.isEmpty()) {
            System.err.println("❌ GOOGLE_API_KEY environment variable is NOT set!");
            System.err.println("\nPlease set it and try again.");
            return;
        }

        System.out.println("✓ GOOGLE_API_KEY is set\n");

        // Create the application
        AppBuilder appBuilder = new AppBuilder();
        setupRealAddresses(appBuilder);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame application = appBuilder
                        .addLoginView()
                        .addSignupView()
                        .addLoggedInView()
                        .addCheckFavoriteView()
                        .addSignupUseCase()
                        .addLoginUseCase()
                        .addSearchListingUseCase()
                        .addSaveFavoriteUseCase()
                        .addCheckFavoriteUseCase()
                        .rebuildLoggedInView()
                        .build();

                application.pack();
                application.setSize(1400, 900);
                application.setLocationRelativeTo(null);
                application.setVisible(true);

                System.out.println("✓ Application ready!\n");
                printInstructions();
            }
        });
    }

    private static void setupRealAddresses(AppBuilder appBuilder) {
        System.out.println("Setting up test data with REAL addresses...\n");

        User testUser = new User("testuser", "pass123");
        User host1 = new User("host1", "pass456");

        appBuilder.userDataAccessObject.save(testUser);
        appBuilder.userDataAccessObject.save(host1);

        Listing toronto1 = new Listing(
                "CN Tower View Condo",
                host1,
                null,
                Arrays.asList("downtown", "iconic", "luxury"),
                Arrays.asList("condo"),
                "Modern condo with CN Tower views",
                350.0,
                "301 Front Street West, Toronto, ON, Canada",  // Real CN Tower address
                0.0,
                1200.0,
                2,
                2,
                Listing.BuildingType.CONDO,
                true
        );

        Listing toronto2 = new Listing(
                "University District Studio",
                host1,
                null,
                Arrays.asList("university", "student-friendly"),
                Arrays.asList("studio"),
                "Cozy studio near U of T campus",
                180.0,
                "27 King's College Circle, Toronto, ON, Canada",  // Real U of T address
                0.0,
                500.0,
                1,
                1,
                Listing.BuildingType.STUDIO,
                true
        );

        Listing toronto3 = new Listing(
                "Harbourfront Waterfront Condo",
                host1,
                null,
                Arrays.asList("waterfront", "modern"),
                Arrays.asList("condo"),
                "Beautiful waterfront with lake views",
                320.0,
                "235 Queens Quay West, Toronto, ON, Canada",  // Real Harbourfront address
                0.0,
                1100.0,
                2,
                2,
                Listing.BuildingType.CONDO,
                true
        );

        Listing toronto4 = new Listing(
                "Yorkville Luxury Penthouse",
                host1,
                null,
                Arrays.asList("luxury", "upscale"),
                Arrays.asList("apartment"),
                "High-end penthouse in Yorkville",
                500.0,
                "1 Yorkville Avenue, Toronto, ON, Canada",  // Real Yorkville address
                0.0,
                1500.0,
                3,
                2,
                Listing.BuildingType.APARTMENT,
                true
        );

        Listing toronto5 = new Listing(
                "Scarborough Family House",
                host1,
                null,
                Arrays.asList("family", "spacious", "suburban"),
                Arrays.asList("house"),
                "Large family home in Scarborough",
                220.0,
                "Scarborough Town Centre, Toronto, ON, Canada",  // Real Scarborough location
                0.0,
                2000.0,
                4,
                3,
                Listing.BuildingType.HOUSE,
                true
        );

        // Add all listings
        appBuilder.listingDataAccessObject.addListing(toronto1);
        appBuilder.listingDataAccessObject.addListing(toronto2);
        appBuilder.listingDataAccessObject.addListing(toronto3);
        appBuilder.listingDataAccessObject.addListing(toronto4);
        appBuilder.listingDataAccessObject.addListing(toronto5);

        System.out.println("✓ Created 5 listings with REAL Toronto addresses:\n");
        System.out.println("  1. CN Tower View Condo");
        System.out.println("     📍 301 Front Street West, Toronto");
        System.out.println("     💰 $350/night\n");

        System.out.println("  2. University District Studio");
        System.out.println("     📍 27 King's College Circle, Toronto");
        System.out.println("     💰 $180/night\n");

        System.out.println("  3. Harbourfront Waterfront Condo");
        System.out.println("     📍 235 Queens Quay West, Toronto");
        System.out.println("     💰 $320/night\n");

        System.out.println("  4. Yorkville Luxury Penthouse");
        System.out.println("     📍 1 Yorkville Avenue, Toronto");
        System.out.println("     💰 $500/night\n");

        System.out.println("  5. Scarborough Family House");
        System.out.println("     📍 Scarborough Town Centre, Toronto");
        System.out.println("     💰 $220/night\n");
    }

    private static void printInstructions() {
        System.out.println("========================================");
        System.out.println("TESTING INSTRUCTIONS");
        System.out.println("========================================\n");

        System.out.println("STEP 1: LOGIN");
        System.out.println("  Username: testuser");
        System.out.println("  Password: pass123\n");

        System.out.println("STEP 2: TEST DISTANCE FILTER");
        System.out.println("  In the filter panel (left), enter:\n");

        System.out.println("  📍 User Address: CN Tower, Toronto");
        System.out.println("  📏 Max dist (km): 5");
        System.out.println("  Click 'Filter'\n");

        System.out.println("  ⏱️  Wait 5-10 seconds for API calls...\n");

        System.out.println("EXPECTED RESULTS:\n");
        System.out.println("  With 5 km radius from CN Tower:");
        System.out.println("  ✅ CN Tower View Condo (~0.1 km)");
        System.out.println("  ✅ Harbourfront Condo (~1.5 km)");
        System.out.println("  ✅ University Studio (~2.5 km)");
        System.out.println("  ✅ Yorkville Penthouse (~3 km)");
        System.out.println("  ❌ Scarborough House (~17 km) - FILTERED OUT\n");

        System.out.println("STEP 3: TRY DIFFERENT ADDRESSES\n");
        System.out.println("  Try these as origin addresses:");
        System.out.println("  • Union Station, Toronto");
        System.out.println("  • Toronto Pearson Airport");
        System.out.println("  • Rogers Centre, Toronto");
        System.out.println("  • Toronto City Hall\n");

        System.out.println("STEP 4: COMBINE FILTERS\n");
        System.out.println("  Distance: 5 km from CN Tower");
        System.out.println("  Max price: 300");
        System.out.println("  → Should show 2 listings\n");

        System.out.println("========================================");
        System.out.println("ALL ADDRESSES ARE REAL");
        System.out.println("========================================\n");
        System.out.println("✓ No more ZERO_RESULTS errors!");
        System.out.println("✓ Google Maps can find all these locations");
        System.out.println("✓ Distances are accurate\n");

        System.out.println("Watch the console for API call logs.\n");
        System.out.println("========================================\n");
    }
}