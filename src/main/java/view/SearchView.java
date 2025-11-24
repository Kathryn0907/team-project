package view;

import interface_adapter.filter.FilterListingsController;
import interface_adapter.search_listings.*;
import Entities.Listing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import interface_adapter.ViewManagerModel;
import interface_adapter.listing_detail.ListingDetailViewModel;

/**
 * Search View - Keyword search interface with results display
 * Updated with Messages button
 */
public class SearchView extends JPanel implements PropertyChangeListener {

    public final String viewName = "search";

    private final SearchListingViewModel searchViewModel;
    private final SearchListingController searchController;
    private final FilterListingsController filterController;
    private final ViewManagerModel viewManagerModel;

    private final JTextField keywordField;
    private final JButton searchButton;
    private final JButton messagesButton;
    private final JButton viewFavoritesButton;
    private final JPanel resultsPanel;
    private final JScrollPane resultsScrollPane;

    public SearchView(SearchListingViewModel searchViewModel,
                      SearchListingController searchController,
                      FilterListingsController filterController,
                      ViewManagerModel viewManagerModel,
                      ListingDetailViewModel listingDetailViewModel) {

        this.searchViewModel = searchViewModel;
        this.searchController = searchController;
        this.filterController = filterController;
        this.viewManagerModel = viewManagerModel;
        this.searchViewModel.addPropertyChangeListener(this);

        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top: Search input panel with buttons
        JPanel topPanel = new JPanel(new BorderLayout());

        // Left side: Search bar
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel searchLabel = new JLabel("Enter a key word:");
        keywordField = new JTextField(30);
        searchButton = new JButton("Search");

        searchPanel.add(searchLabel);
        searchPanel.add(keywordField);
        searchPanel.add(searchButton);

        // Right side: Action buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        messagesButton = new JButton("✉️ Messages");
        messagesButton.setFont(new Font("Arial", Font.BOLD, 12));
        viewFavoritesButton = new JButton("❤ View My Favorites");
        viewFavoritesButton.setFont(new Font("Arial", Font.BOLD, 12));

        actionPanel.add(messagesButton);
        actionPanel.add(viewFavoritesButton);

        topPanel.add(searchPanel, BorderLayout.WEST);
        topPanel.add(actionPanel, BorderLayout.EAST);

        // Center: Results display panel
        resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        resultsScrollPane = new JScrollPane(resultsPanel);
        resultsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Left: Filter sidebar
        if (filterController != null) {
            FilterSidebarPanel filterPanel = new FilterSidebarPanel(filterController);
            this.add(filterPanel, BorderLayout.WEST);
        }

        this.add(topPanel, BorderLayout.NORTH);
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

        // Messages button action
        messagesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (viewManagerModel != null) {
                    viewManagerModel.setState("conversations");
                    viewManagerModel.firePropertyChange();
                }
            }
        });

        // View Favorites button action
        viewFavoritesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (viewManagerModel != null) {
                    viewManagerModel.setState("favourite listings");
                    viewManagerModel.firePropertyChange();
                }
            }
        });
    }

    /**
     * Performs keyword search
     */
    private void performSearch() {
        String keyword = keywordField.getText().trim();

        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a keyword to search",
                    "Empty Keyword",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        searchController.execute(keyword, null);
    }

    /**
     * Listen for state changes from ViewModel
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        SearchListingState state = searchViewModel.getState();

        if (state.isSuccess()) {
            displayResults(state.getListings());
        } else {
            displayError(state.getErrorMessage(), state.getListings());
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
            JLabel resultsLabel = new JLabel("Found " + listings.size() + " listing(s):");
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