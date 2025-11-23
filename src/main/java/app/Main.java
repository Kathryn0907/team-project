package app;

import javax.swing.*;

/**
 * Main application entry point with Favorites functionality
 * Updated by Jonathan (Use Case 9 & 14)
 */
public class Main {
    public static void main(String[] args) {
        AppBuilder appBuilder = new AppBuilder();
        JFrame application = appBuilder
                .addLoginView()
                .addSignupView()
                .addLoggedInView()
                .addCheckFavoriteView()
                .addProfileView()
                .addCreateListingView()
                .addSignupUseCase()
                .addLoginUseCase()
                .addSearchListingUseCase()
                .addSaveFavoriteUseCase()      // Add save favorite use case
                .addCheckFavoriteUseCase()     // Add check favorite use case
                .rebuildLoggedInView()         // Rebuild with all controllers
                .addCreateListingUseCase()
                .build();

        application.pack();
        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}