package view;

import Entities.Listing;
import Entities.User;

import interface_adapter.comment.CommentController;
import interface_adapter.comment.CommentViewModel;
import interface_adapter.listing_detail.ListingDetailState;
import interface_adapter.listing_detail.ListingDetailViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

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

    public ListingDetailView(ListingDetailViewModel viewModel,
                             CommentController commentController,
                             CommentViewModel commentViewModel) {

        this.viewModel = viewModel;
        this.commentController = commentController;

        this.commentView = new CommentView(commentController, commentViewModel);

        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel detailsPanel = buildDetailsPanel();
        add(detailsPanel, BorderLayout.NORTH);

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
        tagsLabel = new JLabel("");

        panel.add(titleLabel);
        panel.add(priceLabel);
        panel.add(locationLabel);
        panel.add(roomsLabel);
        panel.add(areaLabel);
        panel.add(typeLabel);
        panel.add(tagsLabel);

        return panel;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        ListingDetailState state = viewModel.getState();
        Listing l = state.getCurrentListing();
        User u = state.getCurrentUser();

        if(l == null){
            titleLabel.setText("No Listing Selected");
            return;
        }

        titleLabel.setText(l.getName());
        priceLabel.setText("Price: $" + l.getPrice());
        locationLabel.setText("Location: " + l.getAddress());
        roomsLabel.setText(l.getBedrooms() + " bedrooms, " + l.getBathrooms() + " bathrooms");
        areaLabel.setText("Area: " + l.getArea());
        typeLabel.setText("Type: " + l.getBuildingType());

        String tagsText = l.getTags().isEmpty() ? "No tags" : String.join(", ", l.getTags());
        tagsLabel.setText("Tags: " + tagsText);

        commentView.setListing(l);
        commentView.setUser(u);

        commentView.revalidate();
        commentView.repaint();
    }
}
