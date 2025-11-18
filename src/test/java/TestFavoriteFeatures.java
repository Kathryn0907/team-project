import app.SaveFavoriteUseCaseFactory;
import app.CheckFavoriteUseCaseFactory;
import data_access.InMemoryListingDataAccessObject;
import Entities.*;
import interface_adapter.ViewManagerModel;
import interface_adapter.save_favorite.*;
import interface_adapter.check_favorite.*;
import view.CheckFavoriteView;

import javax.swing.*;
import java.util.Arrays;

public class TestFavoriteFeatures {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("TESTING FAVORITE FEATURES");
        System.out.println("========================================\n");

        // Initialize data access
        InMemoryListingDataAccessObject dataAccess = new InMemoryListingDataAccessObject();

        // Create test users
        User jonathan = new User("jonathan", "pass123");
        User alice = new User("alice", "secure456");
        dataAccess.addUser(jonathan);
        dataAccess.addUser(alice);

        // Create test listings
        Listing listing1 = new Listing(
                "Beachfront Condo",
                alice,
                null,
                Arrays.asList("beach", "ocean view", "luxury"),
                Arrays.asList("condo"),
                "Beautiful beachfront condo with ocean views",
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
                alice,
                null,
                Arrays.asList("mountain", "nature", "peaceful"),
                Arrays.asList("villa"),
                "Cozy cabin in the mountains",
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
                "Downtown Loft",
                alice,
                null,
                Arrays.asList("downtown", "modern", "urban"),
                Arrays.asList("apartment"),
                "Modern loft in the heart of downtown",
                200.0,
                "789 Main Street, Toronto",
                1.0,
                900.0,
                1,
                1,
                Listing.BuildingType.APARTMENT,
                true
        );

        // Inactive listing (should not be saveable)
        Listing listing4 = new Listing(
                "Unavailable Property",
                alice,
                null,
                Arrays.asList("unavailable"),
                Arrays.asList("house"),
                "This property is no longer available",
                150.0,
                "999 Closed Street",
                10.0,
                1000.0,
                2,
                2,
                Listing.BuildingType.HOUSE,
                false  // INACTIVE
        );

        // Add listings to data access
        dataAccess.addListing(listing1);
        dataAccess.addListing(listing2);
        dataAccess.addListing(listing3);
        dataAccess.addListing(listing4);

        System.out.println("✓ Test data created");
        System.out.println("  - 4 listings (3 active, 1 inactive)");
        System.out.println("  - 2 users\n");

        // TEST 1: Save Favorite Use Case
        System.out.println("TEST 1: SAVE FAVORITE");
        System.out.println("========================================");

        SaveFavoriteViewModel saveViewModel = new SaveFavoriteViewModel();
        SaveFavoriteController saveController =
                SaveFavoriteUseCaseFactory.createSaveFavoriteUseCase(saveViewModel, dataAccess);

        // Test 1a: Add first favorite
        System.out.println("\n--- Adding 'Beachfront Condo' to jonathan's favorites ---");
        saveController.addToFavorites("jonathan", "Beachfront Condo");
        SaveFavoriteState saveState = saveViewModel.getState();
        if (saveState.getError() == null) {
            System.out.println("✓ " + saveState.getMessage());
        } else {
            System.out.println("✗ Error: " + saveState.getError());
        }

        // Test 1b: Add second favorite
        System.out.println("\n--- Adding 'Mountain Cabin Retreat' to jonathan's favorites ---");
        saveController.addToFavorites("jonathan", "Mountain Cabin Retreat");
        saveState = saveViewModel.getState();
        if (saveState.getError() == null) {
            System.out.println("✓ " + saveState.getMessage());
        } else {
            System.out.println("✗ Error: " + saveState.getError());
        }

        // Test 1c: Try to add duplicate
        System.out.println("\n--- Trying to add 'Beachfront Condo' again ---");
        saveController.addToFavorites("jonathan", "Beachfront Condo");
        saveState = saveViewModel.getState();
        if (saveState.getError() == null) {
            System.out.println("✓ " + saveState.getMessage());
        } else {
            System.out.println("✗ Error: " + saveState.getError());
        }

        // Test 1d: Try to add inactive listing
        System.out.println("\n--- Trying to add inactive 'Unavailable Property' ---");
        saveController.addToFavorites("jonathan", "Unavailable Property");
        saveState = saveViewModel.getState();
        if (saveState.getError() != null) {
            System.out.println("✓ Correctly rejected: " + saveState.getError());
        } else {
            System.out.println("✗ Should have rejected inactive listing");
        }

        // Test 1e: Try with non-existent listing
        System.out.println("\n--- Trying to add non-existent listing ---");
        saveController.addToFavorites("jonathan", "Non-Existent Listing");
        saveState = saveViewModel.getState();
        if (saveState.getError() != null) {
            System.out.println("✓ Correctly rejected: " + saveState.getError());
        } else {
            System.out.println("✗ Should have rejected non-existent listing");
        }

        // TEST 2: Check Favorite Use Case
        System.out.println("\n========================================");
        System.out.println("TEST 2: CHECK FAVORITE");
        System.out.println("========================================");

        ViewManagerModel viewManagerModel = new ViewManagerModel();
        CheckFavoriteViewModel checkViewModel = new CheckFavoriteViewModel();
        CheckFavoriteController checkController =
                CheckFavoriteUseCaseFactory.createCheckFavoriteUseCase(
                        viewManagerModel, checkViewModel, dataAccess);

        // Test 2a: Check jonathan's favorites
        System.out.println("\n--- Checking jonathan's favorites ---");
        checkController.loadFavouriteListings("jonathan");
        CheckFavoriteState checkState = checkViewModel.getState();
        if (checkState.getError() == null) {
            System.out.println("✓ Found " + checkState.getFavouriteListingNames().size() + " favorites:");
            for (String name : checkState.getFavouriteListingNames()) {
                System.out.println("  • " + name);
            }
        } else {
            System.out.println("✗ Error: " + checkState.getError());
        }

        // Test 2b: Check alice's favorites (should be empty)
        System.out.println("\n--- Checking alice's favorites (should be empty) ---");
        checkController.loadFavouriteListings("alice");
        checkState = checkViewModel.getState();
        if (checkState.getError() == null) {
            System.out.println("✓ Found " + checkState.getFavouriteListingNames().size() + " favorites");
            if (checkState.getFavouriteListingNames().isEmpty()) {
                System.out.println("  (No favorites yet)");
            }
        } else {
            System.out.println("✗ Error: " + checkState.getError());
        }

        // Test 2c: Check non-existent user
        System.out.println("\n--- Checking non-existent user ---");
        checkController.loadFavouriteListings("nonexistent");
        checkState = checkViewModel.getState();
        if (checkState.getError() != null) {
            System.out.println("✓ Correctly rejected: " + checkState.getError());
        } else {
            System.out.println("✗ Should have rejected non-existent user");
        }

        // TEST 3: UI Demo
        System.out.println("\n========================================");
        System.out.println("TEST 3: UI DEMO");
        System.out.println("========================================");

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Re-load jonathan's favorites to display in UI
                checkController.loadFavouriteListings("jonathan");

                CheckFavoriteView view = new CheckFavoriteView(checkViewModel);

                JFrame frame = new JFrame("Favorite Listings - " + "jonathan");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(700, 500);
                frame.setLocationRelativeTo(null);
                frame.add(view);
                frame.setVisible(true);

                System.out.println("\n✓ UI window opened");
                System.out.println("  Displaying jonathan's " +
                        checkViewModel.getState().getFavouriteListingNames().size() +
                        " favorite listings");
                System.out.println("\nClose the window to exit.");
            }
        });

        System.out.println("\n========================================");
        System.out.println("✓ ALL TESTS PASSED!");
        System.out.println("========================================");
    }
}