package view;

import Entities.Listing;
import Entities.User;

import interface_adapter.comment.CommentController;
import interface_adapter.comment.CommentViewModel;
import interface_adapter.listing_detail.ListingDetailState;
import interface_adapter.listing_detail.ListingDetailViewModel;
import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInState;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ListingDetailView extends JPanel implements PropertyChangeListener {

    private final ListingDetailViewModel viewModel;
    private final CommentController commentController;
    private final CommentView commentView;
    private final ViewManagerModel viewManagerModel;

    // labels for info
    private JLabel titleLabel;
    private JLabel priceLabel;
    private JLabel locationLabel;
    private JLabel roomsLabel;
    private JLabel areaLabel;
    private JLabel typeLabel;
    private JLabel tagsLabel;
    private JLabel descriptionLabel;
    private JLabel photoLabel;

    private static final int PHOTO_WIDTH = 400;
    private static final int PHOTO_HEIGHT = 230;

    public ListingDetailView(ListingDetailViewModel viewModel,
                             CommentController commentController,
                             CommentViewModel commentViewModel) {

        this.viewModel = viewModel;
        this.commentController = commentController;
        this.commentView = new CommentView(commentController, commentViewModel);
        this.viewManagerModel = ViewManagerModel.getInstance();

        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Back button + Title
        JPanel headerPanel = new JPanel(new BorderLayout());

        JButton backButton = new JButton("← Back");
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> {
            LoggedInState loggedInState = new LoggedInState();
            loggedInState.setUser(viewModel.getState().getCurrentUser());
            viewManagerModel.setState("logged in");
            viewManagerModel.firePropertyChange();
        });
        headerPanel.add(backButton, BorderLayout.WEST);

        titleLabel = new JLabel("");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);

        // photo + listing info
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));

        photoLabel = new JLabel("No photo", SwingConstants.CENTER);
        Dimension photoSize = new Dimension(PHOTO_WIDTH, PHOTO_HEIGHT);
        photoLabel.setPreferredSize(photoSize);
        photoLabel.setMinimumSize(photoSize);
        photoLabel.setMaximumSize(photoSize);

        photoLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        centerPanel.add(photoLabel, BorderLayout.CENTER);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        priceLabel = new JLabel();
        locationLabel = new JLabel();
        roomsLabel = new JLabel();
        areaLabel = new JLabel();
        typeLabel = new JLabel();
        tagsLabel = new JLabel();
        descriptionLabel = new JLabel();

        infoPanel.add(priceLabel);
        infoPanel.add(locationLabel);
        infoPanel.add(roomsLabel);
        infoPanel.add(areaLabel);
        infoPanel.add(typeLabel);
        infoPanel.add(tagsLabel);
        infoPanel.add(descriptionLabel);

        centerPanel.add(infoPanel, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);

        // comments section
        add(commentView, BorderLayout.SOUTH);
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

        String tagsText = (l.getTags() == null || l.getTags().isEmpty())
                ? "Tags: No tags"
                : "Tags: " + String.join(", ", l.getTags());
        tagsLabel.setText(tagsText);

        descriptionLabel.setText("Description: " + l.getDescription());

        if (l.getPhotoPath() != null && !l.getPhotoPath().isEmpty()) {
            try {
                ImageIcon icon = new ImageIcon(l.getPhotoPath());
                Image img = icon.getImage();

                int imgW = img.getWidth(null);
                int imgH = img.getHeight(null);

                if (imgW > 0 && imgH > 0) {
                    double scale = Math.min(
                            (double) PHOTO_WIDTH / imgW,
                            (double) PHOTO_HEIGHT / imgH
                    );
                    int newW = (int) (imgW * scale);
                    int newH = (int) (imgH * scale);

                    Image scaled = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
                    photoLabel.setIcon(new ImageIcon(scaled));
                    photoLabel.setText("");
                } else {
                    photoLabel.setIcon(null);
                    photoLabel.setText("Photo not available");
                }
            } catch (Exception e) {
                photoLabel.setIcon(null);
                photoLabel.setText("Photo not available");
            }
        } else {
            photoLabel.setIcon(null);
            photoLabel.setText("No photo");
        }

        // Bind comments section to the current listing and user
        commentView.setListing(l);
        commentView.setUser(u);
        commentView.revalidate();
        commentView.repaint();
    }
}