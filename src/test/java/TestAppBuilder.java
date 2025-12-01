import app.AppBuilder;
import Entities.*;
import data_access.MongoDBUserDAO;
import data_access.MongoDBListingDAO;
import data_access.MongoDBMessageDAO;

import javax.swing.*;
import java.util.Arrays;

/**
 * Complete Test for AppBuilder with all integrated features:
 * - Favorites
 * - MongoDB-backed Messaging with WebSocket
 * - Imagga Tag Extraction
 */
public class TestAppBuilder {
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("TESTING COMPLETE APP INTEGRATION");
        System.out.println("========================================\n");

        // Check MongoDB connection first
        System.out.println("Checking MongoDB connection...");
        MongoDBUserDAO mongoUserDAO = null;
        MongoDBListingDAO mongoListingDAO = null;
        MongoDBMessageDAO mongoMessageDAO = null;

        try {
            mongoUserDAO = new MongoDBUserDAO();
            mongoListingDAO = new MongoDBListingDAO();
            mongoMessageDAO = new MongoDBMessageDAO();
            System.out.println("✅ MongoDB connection successful\n");
        } catch (Exception e) {
            System.err.println("❌ MongoDB connection failed: " + e.getMessage());
            System.err.println("⚠️  Continuing with in-memory data only...\n");
        }

        // Create test data
        System.out.println("Setting up test data...\n");

        AppBuilder appBuilder = new AppBuilder();

        // Create test users
        User testUser1 = new User("alice", "password123");
        User testUser2 = new User("bob", "password456");
        User testUser3 = new User("charlie", "password789");

        // Save to both in-memory and MongoDB
        appBuilder.userDataAccessObject.save(testUser1);
        appBuilder.userDataAccessObject.save(testUser2);
        appBuilder.userDataAccessObject.save(testUser3);

        if (mongoUserDAO != null) {
            try {
                // Check if users already exist in MongoDB
                if (mongoUserDAO.findUserByUsername("alice") == null) {
                    mongoUserDAO.saveUser(testUser1);
                    System.out.println("✓ Saved alice to MongoDB");
                } else {
                    System.out.println("✓ alice already exists in MongoDB");
                }

                if (mongoUserDAO.findUserByUsername("bob") == null) {
                    mongoUserDAO.saveUser(testUser2);
                    System.out.println("✓ Saved bob to MongoDB");
                } else {
                    System.out.println("✓ bob already exists in MongoDB");
                }

                if (mongoUserDAO.findUserByUsername("charlie") == null) {
                    mongoUserDAO.saveUser(testUser3);
                    System.out.println("✓ Saved charlie to MongoDB");
                } else {
                    System.out.println("✓ charlie already exists in MongoDB");
                }
            } catch (Exception e) {
                System.err.println("⚠️  Could not save users to MongoDB: " + e.getMessage());
            }
        }

        System.out.println("\n✓ Created test users:");
        System.out.println("  - alice (password: password123)");
        System.out.println("  - bob (password: password456)");
        System.out.println("  - charlie (password: password789)");

        // Create diverse test listings with image URLs for Imagga
        Listing listing1 = new Listing(
                "Beachfront Paradise Villa",
                testUser2,
                "https://images.unsplash.com/photo-1564013799919-ab600027ffc6",  // House image
                Arrays.asList("beach", "ocean view", "luxury", "pool"),
                Arrays.asList("villa"),
                "Stunning beachfront villa with private pool and ocean views",
                450.0,
                "123 Ocean Drive, Miami Beach, FL",
                5.0,
                3500.0,
                4,
                3,
                Listing.BuildingType.VILLA,
                true
        );

        Listing listing2 = new Listing(
                "Modern Downtown Loft",
                testUser2,
                "https://images.unsplash.com/photo-1522708323590-d24dbb6b0267",  // Apartment image
                Arrays.asList("downtown", "modern", "urban", "nightlife"),
                Arrays.asList("apartment"),
                "Sleek modern loft in the heart of downtown with city views",
                250.0,
                "456 Main Street, Toronto, ON",
                1.0,
                1200.0,
                2,
                2,
                Listing.BuildingType.APARTMENT,
                true
        );

        Listing listing3 = new Listing(
                "Mountain Cabin Retreat",
                testUser3,
                "https://images.unsplash.com/photo-1613490493576-7fde63acd811",  // Villa/cabin image
                Arrays.asList("mountain", "nature", "peaceful", "hiking"),
                Arrays.asList("villa"),
                "Cozy mountain cabin surrounded by nature and hiking trails",
                180.0,
                "789 Mountain Road, Aspen, CO",
                100.0,
                1800.0,
                3,
                2,
                Listing.BuildingType.VILLA,
                true
        );

        Listing listing4 = new Listing(
                "Lakeside Family House",
                testUser2,
                "https://images.unsplash.com/photo-1568605114967-8130f3a36994",  // House image
                Arrays.asList("lake", "family-friendly", "dock", "spacious"),
                Arrays.asList("house"),
                "Beautiful family house on the lake with private dock",
                320.0,
                "111 Lake View Drive, Muskoka, ON",
                75.0,
                2500.0,
                4,
                3,
                Listing.BuildingType.HOUSE,
                true
        );

        Listing listing5 = new Listing(
                "Cozy Studio Downtown",
                testUser3,
                "https://images.unsplash.com/photo-1502672260266-1c1ef2d93688",  // Studio image
                Arrays.asList("studio", "cozy", "affordable", "transit"),
                Arrays.asList("apartment"),
                "Perfect studio apartment near transit and amenities",
                120.0,
                "222 Transit Way, Toronto, ON",
                3.0,
                500.0,
                1,
                1,
                Listing.BuildingType.STUDIO,
                true
        );

        // Add listings to in-memory storage
        appBuilder.listingDataAccessObject.addListing(listing1);
        appBuilder.listingDataAccessObject.addListing(listing2);
        appBuilder.listingDataAccessObject.addListing(listing3);
        appBuilder.listingDataAccessObject.addListing(listing4);
        appBuilder.listingDataAccessObject.addListing(listing5);

        // Add to MongoDB if available
        if (mongoListingDAO != null) {
            try {
                mongoListingDAO.saveListing(listing1);
                mongoListingDAO.saveListing(listing2);
                mongoListingDAO.saveListing(listing3);
                mongoListingDAO.saveListing(listing4);
                mongoListingDAO.saveListing(listing5);
                System.out.println("\n✓ Saved 5 listings to MongoDB");
            } catch (Exception e) {
                System.err.println("⚠️  Could not save listings to MongoDB: " + e.getMessage());
            }
        }

        System.out.println("\n✓ Created 5 test listings with image URLs:");
        System.out.println("  1. Beachfront Paradise Villa ($450/night)");
        System.out.println("  2. Modern Downtown Loft ($250/night)");
        System.out.println("  3. Mountain Cabin Retreat ($180/night)");
        System.out.println("  4. Lakeside Family House ($320/night)");
        System.out.println("  5. Cozy Studio Downtown ($120/night)");

        // Add sample messages to MongoDB if available
        if (mongoMessageDAO != null) {
            try {
                System.out.println("\n✓ Creating sample conversation between alice and bob...");

                // Check if messages already exist
                java.util.ArrayList<Entities.Message> existingMessages =
                        mongoMessageDAO.getConversation("alice", "bob");

                if (existingMessages.isEmpty()) {
                    Message msg1 = new Message(
                            java.util.UUID.randomUUID().toString(),
                            "alice",
                            "bob",
                            listing1.getId().toHexString(),
                            "Hi Bob! I'm interested in your Beachfront Paradise Villa. Is it available?"
                    );
                    mongoMessageDAO.saveMessage(msg1);

                    Message msg2 = new Message(
                            java.util.UUID.randomUUID().toString(),
                            "bob",
                            "alice",
                            listing1.getId().toHexString(),
                            "Hi Alice! Yes, it's available. Would you like to schedule a viewing?"
                    );
                    msg2.setTimestamp(java.time.Instant.now().plusSeconds(60));
                    mongoMessageDAO.saveMessage(msg2);

                    Message msg3 = new Message(
                            java.util.UUID.randomUUID().toString(),
                            "alice",
                            "bob",
                            listing1.getId().toHexString(),
                            "That would be great! How about tomorrow afternoon?"
                    );
                    msg3.setTimestamp(java.time.Instant.now().plusSeconds(120));
                    mongoMessageDAO.saveMessage(msg3);

                    System.out.println("  ✓ Added 3 sample messages");
                } else {
                    System.out.println("  ✓ Sample conversation already exists (" +
                            existingMessages.size() + " messages)");
                }
            } catch (Exception e) {
                System.err.println("⚠️  Could not create sample messages: " + e.getMessage());
            }
        }

        // Build and launch the application
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                System.out.println("\n========================================");
                System.out.println("BUILDING APPLICATION");
                System.out.println("========================================\n");

                // Start WebSocket server first
                appBuilder.startWebSocketServer();

                // Build the complete application with all features
                JFrame application = appBuilder
                        .addLoginView()
                        .addSignupView()
                        .addLoggedInView()
                        .addCheckFavoriteView()
                        .addProfileView()
                        .addSignupUseCase()
                        .addLoginUseCase()
                        .addSearchListingUseCase()
                        .addSaveFavoriteUseCase()
                        .addCheckFavoriteUseCase()
                        .addMessagingUseCase()
                        .rebuildLoggedInView()
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

    private static void printTestInstructions() {
        System.out.println("========================================");
        System.out.println("COMPREHENSIVE TEST INSTRUCTIONS");
        System.out.println("========================================\n");

        System.out.println("📋 SETUP:");
        System.out.println("  1. You should see the SIGNUP view");
        System.out.println("  2. Click 'Go to Login' to switch to LOGIN view\n");

        System.out.println("🔐 LOGIN OPTIONS:");
        System.out.println("  Username: alice | Password: password123");
        System.out.println("  Username: bob   | Password: password456");
        System.out.println("  Username: charlie | Password: password789\n");

        System.out.println("========================================");
        System.out.println("FEATURE 1: SEARCH & BROWSE LISTINGS");
        System.out.println("========================================");
        System.out.println("✓ After login, you'll see:");
        System.out.println("  - Filter panel on the LEFT");
        System.out.println("  - Search bar at the TOP");
        System.out.println("  - ✉️ Messages button (TOP RIGHT)");
        System.out.println("  - ❤ View My Favorites button (TOP RIGHT)");
        System.out.println("  - ALL 5 listings displayed\n");

        System.out.println("Try searching for:");
        System.out.println("  • 'beach' → Finds Beachfront Paradise Villa");
        System.out.println("  • 'mountain' → Finds Mountain Cabin Retreat");
        System.out.println("  • 'downtown' → Finds Modern Loft & Studio");
        System.out.println("  • 'lake' → Finds Lakeside Family House\n");

        System.out.println("Try filtering:");
        System.out.println("  • Max price: $200 → Shows 2 listings");
        System.out.println("  • Building type: VILLA → Shows 2 listings");
        System.out.println("  • Min bedrooms: 3 → Shows 3 listings\n");

        System.out.println("========================================");
        System.out.println("FEATURE 2: FAVORITE LISTINGS");
        System.out.println("========================================");
        System.out.println("✓ Each listing card has '❤ Add to Favorites' button");
        System.out.println("\nTest steps:");
        System.out.println("  1. Click '❤ Add to Favorites' on any listing");
        System.out.println("     → Success dialog appears");
        System.out.println("  2. Add 2-3 listings to favorites");
        System.out.println("  3. Click '❤ View My Favorites' (top right)");
        System.out.println("     → See your favorited listings");
        System.out.println("  4. Try adding same listing again");
        System.out.println("     → 'Already in favourites' message\n");

        System.out.println("========================================");
        System.out.println("FEATURE 3: REAL-TIME MESSAGING");
        System.out.println("========================================");
        System.out.println("✓ MongoDB + WebSocket integration");
        System.out.println("\nTest steps:");
        System.out.println("  1. Click '✉️ Messages' button (top right)");
        System.out.println("     → Opens Conversations view");
        System.out.println("  2. If logged in as 'alice':");
        System.out.println("     → Should see existing conversation with 'bob'");
        System.out.println("     → Shows message preview and unread count");
        System.out.println("  3. Click on conversation");
        System.out.println("     → Opens chat with message history from MongoDB");
        System.out.println("  4. Type and send a message");
        System.out.println("     → Saved to MongoDB + sent via WebSocket");
        System.out.println("  5. Click '← Back to Conversations'");
        System.out.println("     → Returns to conversation list\n");

        System.out.println("🆕 NEW CONVERSATION:");
        System.out.println("  1. Click '➕ New Conversation'");
        System.out.println("  2. Enter username (try 'charlie')");
        System.out.println("     → System validates username in MongoDB");
        System.out.println("  3. If valid, opens new chat");
        System.out.println("  4. Send first message to start conversation\n");

        System.out.println("💡 MULTI-USER TEST:");
        System.out.println("  1. Run TWO instances of the app");
        System.out.println("  2. Login as 'alice' in one, 'bob' in other");
        System.out.println("  3. Send messages between them");
        System.out.println("  4. Messages appear INSTANTLY (WebSocket)");
        System.out.println("  5. Restart apps → Messages persist (MongoDB)\n");

        System.out.println("========================================");
        System.out.println("FEATURE 4: IMAGGA TAG EXTRACTION");
        System.out.println("========================================");
        System.out.println("✓ AI-powered image tagging");
        System.out.println("\nTest steps:");
        System.out.println("  1. Click on any listing card to view details");
        System.out.println("  2. Scroll down to see listing info and photo");
        System.out.println("  3. Click '🏷️ Get Tags from Image' button");
        System.out.println("     → Progress bar appears");
        System.out.println("     → Calls Imagga API (takes 2-5 seconds)");
        System.out.println("  4. Dialog shows extracted tags and categories");
        System.out.println("  5. Tags are saved and displayed on listing\n");

        System.out.println("📸 Images with good results:");
        System.out.println("  • Beachfront Villa → beach, ocean, luxury tags");
        System.out.println("  • Modern Loft → apartment, urban, modern tags");
        System.out.println("  • Mountain Cabin → nature, mountain, peaceful tags\n");

        System.out.println("========================================");
        System.out.println("VERIFICATION CHECKLIST");
        System.out.println("========================================");
        System.out.println("Core Features:");
        System.out.println("  [ ] Can search listings by keyword");
        System.out.println("  [ ] Can filter by price, bedrooms, type");
        System.out.println("  [ ] Can add listings to favorites");
        System.out.println("  [ ] Can view all favorite listings");
        System.out.println("  [ ] Cannot add duplicate favorites\n");

        System.out.println("Messaging Features:");
        System.out.println("  [ ] ✉️ Messages button visible");
        System.out.println("  [ ] Conversations list loads from MongoDB");
        System.out.println("  [ ] Can see existing conversations");
        System.out.println("  [ ] Can start new conversation with valid user");
        System.out.println("  [ ] Cannot start conversation with invalid user");
        System.out.println("  [ ] Messages save to MongoDB");
        System.out.println("  [ ] Messages appear in real-time (WebSocket)");
        System.out.println("  [ ] Message history persists after restart");
        System.out.println("  [ ] Unread message counts display correctly\n");

        System.out.println("Imagga Features:");
        System.out.println("  [ ] 🏷️ Get Tags button visible in listing details");
        System.out.println("  [ ] Progress bar shows during extraction");
        System.out.println("  [ ] Tags extract successfully from images");
        System.out.println("  [ ] Tags display on listing after extraction");
        System.out.println("  [ ] Categories are assigned correctly\n");

        System.out.println("========================================");
        System.out.println("KNOWN ISSUES & TIPS");
        System.out.println("========================================");
        System.out.println("💡 If MongoDB connection fails:");
        System.out.println("  → App continues with in-memory data");
        System.out.println("  → Messaging won't persist");
        System.out.println("  → Check MONGODB_URI environment variable\n");

        System.out.println("💡 If WebSocket fails:");
        System.out.println("  → Check if port 8887 is available");
        System.out.println("  → Messages save to DB but no real-time delivery");
        System.out.println("  → Restart application\n");

        System.out.println("💡 If Imagga API fails:");
        System.out.println("  → Check API credentials in config.properties");
        System.out.println("  → Verify image URL is accessible");
        System.out.println("  → Check API rate limits\n");

        System.out.println("========================================");
        System.out.println("🚀 READY TO TEST!");
        System.out.println("========================================");
        System.out.println("\nFollow the steps above to test all features.");
        System.out.println("Close the window when finished testing.\n");
        System.out.println("For issues, check console output for errors.\n");
    }
}