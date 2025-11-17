package view;

import interface_adapter.check_favorite.*;

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
    private final JPanel listingsPanel;
    private final JLabel titleLabel;

    public CheckFavoriteView(CheckFavoriteViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Title panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titleLabel = new JLabel("My Favorite Listings");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titlePanel.add(titleLabel);

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
            displayFavoriteListings(state.getFavouriteListingNames());
        }
    }

    private void displayFavoriteListings(List<String> listingNames) {
        listingsPanel.removeAll();

        if (listingNames.isEmpty()) {
            JLabel emptyLabel = new JLabel("You have no favorite listings yet.");
            emptyLabel.setFont(new Font("Arial", Font.ITALIC, 14));
            emptyLabel.setForeground(Color.GRAY);
            listingsPanel.add(emptyLabel);
        } else {
            titleLabel.setText("My Favorite Listings (" + listingNames.size() + ")");

            for (String listingName : listingNames) {
                JPanel listingCard = createListingCard(listingName);
                listingsPanel.add(listingCard);
                listingsPanel.add(Box.createVerticalStrut(10));
            }
        }

        listingsPanel.revalidate();
        listingsPanel.repaint();
    }

    private JPanel createListingCard(String listingName) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JLabel nameLabel = new JLabel(listingName);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JButton viewButton = new JButton("View Details");
        JButton removeButton = new JButton("Remove");
        removeButton.setForeground(Color.RED);

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