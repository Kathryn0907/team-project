package view;

import interface_adapter.filter.FilterListingsController;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.search_listings.*;
import Entities.Listing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

/**
 * The View for when the user is logged into the program.
 * Displays all available listings by default, with search and filter capabilities.
 */
public class LoggedInView extends JPanel implements PropertyChangeListener {

    private final String viewName = "logged in";
    private final LoggedInViewModel loggedInViewModel;
    private final SearchListingViewModel searchViewModel;
    private final SearchListingController searchController;
    private final FilterListingsController filterController;

    private final JTextField keywordField;
    private final JButton searchButton;
    private final JPanel resultsPanel;
    private final JScrollPane resultsScrollPane;

    public LoggedInView(LoggedInViewModel loggedInViewModel,
                        SearchListingViewModel searchViewModel,
                        SearchListingController searchController,
                        FilterListingsController filterController) {
        this.loggedInViewModel = loggedInViewModel;
        this.searchViewModel = searchViewModel;
        this.searchController = searchController;
        this.filterController = filterController;

        this.loggedInViewModel.addPropertyChangeListener(this);
        this.searchViewModel.addPropertyChangeListener(this);

        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top: Search input panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel searchLabel = new JLabel("Enter a keyword:");
        keywordField = new JTextField(30);
        searchButton = new JButton("Search");

        searchPanel.add(searchLabel);
        searchPanel.add(keywordField);
        searchPanel.add(searchButton);

        // Center: Results display panel
        resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        resultsScrollPane = new JScrollPane(resultsPanel);
        resultsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Left: Filter sidebar
        FilterSidebarPanel filterPanel = new FilterSidebarPanel(filterController);

        this.add(filterPanel, BorderLayout.WEST);
        this.add(searchPanel, BorderLayout.NORTH);
        this.add(resultsScrollPane, BorderLayout.CENTER);

        // Search button action
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch();
            }
        });

        // Enter key triggers search
        keywordField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch();
            }
        });
    }

    /**
     * Load and display all available listings (called when view first appears)
     */
    public void loadAllListings() {
        // Trigger a search with empty keyword to get all listings
        searchController.execute("", null);
    }

    /**
     * Performs keyword search
     */
    private void performSearch() {
        String keyword = keywordField.getText().trim();

        if (keyword.isEmpty()) {
            // If empty, show all listings
            loadAllListings();
            return;
        }

        searchController.execute(keyword, null);
    }

    /**
     * Listen for state changes from ViewModels
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getSource() == searchViewModel) {
            SearchListingState state = searchViewModel.getState();

            if (state.isSuccess()) {
                displayResults(state.getListings());
            } else {
                displayError(state.getErrorMessage(), state.getListings());
            }
        }
    }

    /**
     * Display search results
     */
    private void displayResults(ArrayList<Listing> listings) {
        resultsPanel.removeAll();

        if (listings.isEmpty()) {
            JLabel noResults = new JLabel("No listings found.");
            noResults.setFont(new Font("Arial", Font.PLAIN, 14));
            resultsPanel.add(noResults);
        } else {
            JLabel resultsLabel = new JLabel("Showing " + listings.size() + " listing(s):");
            resultsLabel.setFont(new Font("Arial", Font.BOLD, 14));
            resultsPanel.add(resultsLabel);
            resultsPanel.add(Box.createVerticalStrut(10));

            for (Listing listing : listings) {
                ListingCardPanel card = new ListingCardPanel(listing);
                resultsPanel.add(card);
                resultsPanel.add(Box.createVerticalStrut(10));
            }
        }

        resultsPanel.revalidate();
        resultsPanel.repaint();
    }

    /**
     * Display error message with fallback results
     */
    private void displayError(String errorMessage, ArrayList<Listing> fallbackListings) {
        resultsPanel.removeAll();

        JLabel errorLabel = new JLabel(errorMessage);
        errorLabel.setFont(new Font("Arial", Font.BOLD, 14));
        errorLabel.setForeground(Color.RED);
        resultsPanel.add(errorLabel);
        resultsPanel.add(Box.createVerticalStrut(10));

        if (!fallbackListings.isEmpty()) {
            JLabel fallbackLabel = new JLabel("Showing all available listings:");
            fallbackLabel.setFont(new Font("Arial", Font.ITALIC, 12));
            resultsPanel.add(fallbackLabel);
            resultsPanel.add(Box.createVerticalStrut(10));

            for (Listing listing : fallbackListings) {
                ListingCardPanel card = new ListingCardPanel(listing);
                resultsPanel.add(card);
                resultsPanel.add(Box.createVerticalStrut(10));
            }
        }

        resultsPanel.revalidate();
        resultsPanel.repaint();
    }

    public String getViewName() {
        return viewName;
    }
}