package app;

import javax.swing.*;

/**
 * Main application entry point with Favorites functionality
 * Updated by Jonathan (Use Case 9 & 14)
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("AIRBNB LISTING BROWSER");
        System.out.println("========================================\n");

        System.out.println("Initializing application...");

        AppBuilder appBuilder = new AppBuilder();

        // Start WebSocket server first
        appBuilder.startWebSocketServer();

        // Build the application
        JFrame application = appBuilder
                // 1. Basic views
                .addSignupView()
                .addLoginView()
                .addLoggedInView()

                // 2. Use cases that create controllers the views need
                .addSearchListingUseCase()
                .addSaveFavoriteUseCase()      // sets saveController
                .addCheckFavoriteUseCase()     // sets checkController

                // 3. Views that depend on those controllers / viewmodels
                .addCheckFavoriteView()
                .addProfileView()
                .addCreateListingView()
                .addCreateListingUseCase()
                .addListingDetailViewAndCommentUseCase()
                .addDeleteListingUseCase()
                .addEditListingUseCase()
                .addMessagingUseCase()

                // 4. Hook up login/signup use cases (they just need the viewmodels)
                .addSignupUseCase()
                .addLoginUseCase()

                // 5. Now that search/save/check controllers exist, rebuild LoggedInView with them
                .rebuildLoggedInView()

                // 6. Finish
                .build();


        application.pack();
        application.setSize(1200, 800);
        application.setLocationRelativeTo(null);
        application.setVisible(true);

        System.out.println("\n========================================");
        System.out.println("APPLICATION STARTED");
        System.out.println("========================================");
        System.out.println("\nFeatures available:");
        System.out.println("  ✓ Search and filter listings");
        System.out.println("  ✓ Save favorite listings");
        System.out.println("  ✓ Real-time messaging (MongoDB + WebSocket)");
        System.out.println("  ✓ Imagga AI tag extraction");
        System.out.println("\nQuick start:");
        System.out.println("  1. Sign up or log in");
        System.out.println("  2. Browse listings");
        System.out.println("  3. Click ✉️ Messages to chat");
        System.out.println("  4. Click ❤ to save favorites");
        System.out.println("  5. View listing details");
        System.out.println("\n========================================\n");
    }
}