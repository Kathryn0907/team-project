package view;

import Entities.Listing;

import interface_adapter.ViewManagerModel;
import interface_adapter.check_favorite.CheckFavoriteController;
import interface_adapter.check_favorite.CheckFavoriteState;
import interface_adapter.check_favorite.CheckFavoriteViewModel;
import interface_adapter.listing_detail.ListingDetailState;
import interface_adapter.listing_detail.ListingDetailViewModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.save_favorite.SaveFavoriteController;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * View for displaying the user's favorite listings.
 */
public class CheckFavoriteView extends JPanel implements PropertyChangeListener {

    private final String viewName = "favourite listings";

    private final CheckFavoriteViewModel viewModel;
    private final ViewManagerModel viewManagerModel;

    private final CheckFavoriteController checkFavoriteController;
    private final SaveFavoriteController saveFavoriteController;
    private final LoggedInViewModel loggedInViewModel;

    // For jumping to detail page – same pattern as ListingCardPanel
    private final ListingDetailViewModel listingDetailViewModel;

    private final JPanel listingsPanel;
    private final JLabel titleLabel;

    public CheckFavoriteView(CheckFavoriteViewModel viewModel,
                             ViewManagerModel viewManagerModel,
                             CheckFavoriteController checkFavoriteController,
                             SaveFavoriteController saveFavoriteController,
                             LoggedInViewModel loggedInViewModel) {

        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;
        this.checkFavoriteController = checkFavoriteController;
        this.saveFavoriteController = saveFavoriteController;
        this.loggedInViewModel = loggedInViewModel;
        this.listingDetailViewModel = ListingDetailViewModel.getInstance();

        this.viewModel.addPropertyChangeListener(this);

        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titleLabel = new JLabel("My Favorite Listings");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titlePanel.add(titleLabel, BorderLayout.WEST);

        // Back button
        JButton backButton = new JButton("← Back to Listings");
        backButton.addActionListener(e -> {
            viewManagerModel.setState("logged in");
            viewManagerModel.firePropertyChange();
        });
        titlePanel.add(backButton, BorderLayout.EAST);

        // Listings display panel
        listingsPanel = new JPanel();
        listingsPanel.setLayout(new BoxLayout(listingsPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(listingsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        this.add(titlePanel, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        CheckFavoriteState state = viewModel.getState();

        if (state.getError() != null) {
            displayError(state.getError());
        } else {
            displayFavoriteListings(state.getFavouriteListings());
        }
    }

    private void displayFavoriteListings(List<Listing> listings) {
        listingsPanel.removeAll();

        if (listings == null || listings.isEmpty()) {
            titleLabel.setText("My Favorite Listings");

            JLabel emptyLabel = new JLabel("You have no favorite listings yet.");
            emptyLabel.setFont(new Font("Arial", Font.ITALIC, 14));
            emptyLabel.setForeground(Color.GRAY);

            listingsPanel.add(emptyLabel);
        } else {
            titleLabel.setText("My Favorite Listings (" + listings.size() + ")");

            for (Listing listing : listings) {
                JPanel listingCard = createListingCard(listing);
                listingsPanel.add(listingCard);
                listingsPanel.add(Box.createVerticalStrut(10));
            }
        }

        listingsPanel.revalidate();
        listingsPanel.repaint();
    }

    private JPanel createListingCard(Listing listing) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JLabel nameLabel = new JLabel(listing.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JButton viewButton = new JButton("View Details");
        JButton removeButton = new JButton("Remove");
        removeButton.setForeground(Color.RED);

        // VIEW DETAILS – same behavior as clicking title in ListingCardPanel
        viewButton.addActionListener(e -> {
            ListingDetailState state = listingDetailViewModel.getState();
            state.setCurrentListing(listing);
            state.setCurrentUser(loggedInViewModel.getState().getUser());

            listingDetailViewModel.setState(state);
            listingDetailViewModel.firePropertyChange();

            viewManagerModel.setState(ListingDetailViewModel.VIEW_NAME);
            viewManagerModel.firePropertyChange();
        });

        // REMOVE FAVORITE (toggle)
        removeButton.addActionListener(e -> {
            String username = loggedInViewModel.getState().getUsername();
            saveFavoriteController.addToFavorites(username, listing.getName());
            checkFavoriteController.loadFavouriteListings(username);
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(viewButton);
        buttonPanel.add(removeButton);

        card.add(nameLabel, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.EAST);

        return card;
    }

    private void displayError(String errorMessage) {
        listingsPanel.removeAll();

        JLabel errorLabel = new JLabel("Error: " + errorMessage);
        errorLabel.setFont(new Font("Arial", Font.BOLD, 14));
        errorLabel.setForeground(Color.RED);

        listingsPanel.add(errorLabel);

        listingsPanel.revalidate();
        listingsPanel.repaint();
    }

    public String getViewName() {
        return viewName;
    }
}
