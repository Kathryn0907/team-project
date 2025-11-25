package view;

import Entities.Listing;
import Entities.User;

import interface_adapter.comment.CommentController;
import interface_adapter.comment.CommentViewModel;
import interface_adapter.listing_detail.ListingDetailState;
import interface_adapter.listing_detail.ListingDetailViewModel;
import interface_adapter.ViewManagerModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class ListingDetailView extends JPanel implements PropertyChangeListener {
    public final String viewName = ListingDetailViewModel.VIEW_NAME;

    private final ListingDetailViewModel viewModel;
    private final CommentController commentController;
    private final CommentView commentView;

    private JLabel titleLabel;
    private JLabel priceLabel;
    private JLabel locationLabel;
    private JLabel roomsLabel;
    private JLabel areaLabel;
    private JLabel typeLabel;
    private JLabel tagsLabel;
    private JLabel descriptionLabel;
    private JLabel photoLabel;
    private JButton getTagsButton;
    private JProgressBar tagsProgressBar;

    public ListingDetailView(ListingDetailViewModel viewModel,
                             CommentController commentController,
                             CommentViewModel commentViewModel
                             ) {

        this.viewModel = viewModel;
        this.commentController = commentController;
        this.commentView = new CommentView(commentController, commentViewModel);

        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel detailsPanel = buildDetailsPanel();

        // Add Back to Search button
        JButton backButton = new JButton("← Back to Search");
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> {
            ViewManagerModel.getInstance().setState("search");
            ViewManagerModel.getInstance().firePropertyChange();
        });

        // Panel for Back button + Details
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(backButton, BorderLayout.NORTH);
        topPanel.add(detailsPanel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(commentView, BorderLayout.SOUTH);
    }

    private JPanel buildDetailsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        titleLabel = new JLabel("");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        priceLabel = new JLabel("");
        locationLabel = new JLabel("");
        roomsLabel = new JLabel("");
        areaLabel = new JLabel("");
        typeLabel = new JLabel("");

        // Photo section
        JPanel photoPanel = new JPanel(new BorderLayout(5, 5));
        photoLabel = new JLabel("", SwingConstants.CENTER);
        photoLabel.setPreferredSize(new Dimension(200, 200));
        photoLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JPanel photoButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        getTagsButton = new JButton("🏷️ Get Tags from Image");
        getTagsButton.setFont(new Font("Arial", Font.BOLD, 12));
        tagsProgressBar = new JProgressBar();
        tagsProgressBar.setIndeterminate(true);
        tagsProgressBar.setVisible(false);

        photoButtonPanel.add(getTagsButton);
        photoButtonPanel.add(tagsProgressBar);

        photoPanel.add(photoLabel, BorderLayout.CENTER);
        photoPanel.add(photoButtonPanel, BorderLayout.SOUTH);

        tagsLabel = new JLabel("");
        descriptionLabel = new JLabel("");

        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(photoPanel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(priceLabel);
        panel.add(locationLabel);
        panel.add(roomsLabel);
        panel.add(areaLabel);
        panel.add(typeLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(tagsLabel);
        panel.add(descriptionLabel);

        return panel;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        ListingDetailState state = viewModel.getState();
        Listing l = state.getCurrentListing();
        User u = state.getCurrentUser();

        if (l == null) {
            titleLabel.setText("No Listing Selected");
            return;
        }

        titleLabel.setText(l.getName());
        priceLabel.setText("Price: $" + l.getPrice());
        locationLabel.setText("Location: " + l.getAddress());
        roomsLabel.setText(l.getBedrooms() + " bedrooms, " + l.getBathrooms() + " bathrooms");
        areaLabel.setText("Area: " + l.getArea());
        typeLabel.setText("Type: " + l.getBuildingType());
        descriptionLabel.setText("Description: " + l.getDescription());

        // Display photo if available
        if (l.getPhotoPath() != null && !l.getPhotoPath().isEmpty()) {
            try {
                ImageIcon icon = new ImageIcon(l.getPhotoPath());
                Image scaled = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                photoLabel.setIcon(new ImageIcon(scaled));
            } catch (Exception e) {
                photoLabel.setText("Photo not available");
            }
        } else {
            photoLabel.setText("No photo");
        }


        commentView.setListing(l);
        commentView.setUser(u);

        commentView.revalidate();
        commentView.repaint();
    }
}