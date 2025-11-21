package view;

import Entities.Listing;
import interface_adapter.comment.CommentViewModel;
import interface_adapter.listing_detail.ListingDetailViewModel;

import javax.swing.*;
import java.awt.*;

import interface_adapter.ViewManagerModel;
import interface_adapter.listing_detail.ListingDetailViewModel;
import interface_adapter.listing_detail.ListingDetailState;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Cursor;
import interface_adapter.logged_in.LoggedInViewModel;

/**
 * Individual listing card display component
 */
public class ListingCardPanel extends JPanel {

    private final Listing listing;

    // Needed for jumping to detail page
    private final ViewManagerModel viewManagerModel = ViewManagerModel.getInstance();
    private final ListingDetailViewModel listingDetailViewModel = ListingDetailViewModel.getInstance();
    private final LoggedInViewModel loggedInViewModel = LoggedInViewModel.getInstance();

    public ListingCardPanel(Listing listing) {
        this.listing = listing;

        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        this.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        this.setPreferredSize(new Dimension(700, 150));

        JPanel picturePanel = createPicturePanel();
        JPanel detailsPanel = createDetailsPanel();

        this.add(picturePanel, BorderLayout.WEST);
        this.add(detailsPanel, BorderLayout.CENTER);
    }

    private JPanel createPicturePanel() {
        JPanel picturePanel = new JPanel();
        picturePanel.setPreferredSize(new Dimension(150, 150));
        picturePanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        picturePanel.setLayout(new BorderLayout());

        JLabel pictureLabel = new JLabel("Picture", SwingConstants.CENTER);
        pictureLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        pictureLabel.setForeground(Color.GRAY);

        picturePanel.add(pictureLabel, BorderLayout.CENTER);
        return picturePanel;
    }

    private JPanel createDetailsPanel() {
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JLabel nameLabel = new JLabel(listing.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));

        //Make the title look clickable
        nameLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        //Add click listener to jump to detail page
        nameLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ListingDetailState state = listingDetailViewModel.getState();
                state.setCurrentListing(listing);

                state.setCurrentUser(loggedInViewModel.getState().getUser());

                listingDetailViewModel.setState(state);
                listingDetailViewModel.firePropertyChange();

                viewManagerModel.setState(ListingDetailViewModel.VIEW_NAME);
                viewManagerModel.firePropertyChange();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                nameLabel.setForeground(Color.BLUE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                nameLabel.setForeground(Color.BLACK);
            }
        });


        JLabel priceLabel = new JLabel(String.format("$%.2f per night", listing.getPrice()));
        priceLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel locationLabel = new JLabel("Location: " + listing.getAddress());
        locationLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        JLabel roomsLabel = new JLabel(String.format("%d bedrooms, %d bathrooms",
                listing.getBedrooms(),
                listing.getBathrooms()));
        roomsLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        JLabel areaLabel = new JLabel(String.format("Area: %.0f sq ft", listing.getArea()));
        areaLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        JLabel typeLabel = new JLabel("Type: " + listing.getBuildingType());
        typeLabel.setFont(new Font("Arial", Font.ITALIC, 11));

        String tagsText = listing.getTags().isEmpty() ? "No tags" : String.join(", ", listing.getTags());
        JLabel tagsLabel = new JLabel("Tags: " + tagsText);
        tagsLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        tagsLabel.setForeground(Color.DARK_GRAY);

        detailsPanel.add(nameLabel);
        detailsPanel.add(Box.createVerticalStrut(5));
        detailsPanel.add(priceLabel);
        detailsPanel.add(Box.createVerticalStrut(5));
        detailsPanel.add(locationLabel);
        detailsPanel.add(roomsLabel);
        detailsPanel.add(areaLabel);
        detailsPanel.add(typeLabel);
        detailsPanel.add(Box.createVerticalStrut(5));
        detailsPanel.add(tagsLabel);

        return detailsPanel;
    }
}