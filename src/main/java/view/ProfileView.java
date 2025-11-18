package view;

import Entities.Listing;
import interface_adapter.ProfileViewModel;
import interface_adapter.ViewManagerModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ProfileView extends JPanel implements PropertyChangeListener {

    private final ProfileViewModel profileViewModel;
    private final ViewManagerModel viewManagerModel;

    private final JPanel listingsPanel = new JPanel();

    public ProfileView(ProfileViewModel vm, ViewManagerModel viewManagerModel) {
        this.profileViewModel = vm;
        this.viewManagerModel = viewManagerModel;

        vm.addPropertyChangeListener(this);

        setLayout(new BorderLayout());

        JPanel header = new JPanel(new BorderLayout());

        JLabel title = new JLabel("My Listings");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JButton createListingButton = new JButton("Create Listing");
        createListingButton.setPreferredSize(new Dimension(150, 30));

        createListingButton.addActionListener(e -> {
            viewManagerModel.setState("create listing");
            viewManagerModel.firePropertyChange();
        });

        header.add(title, BorderLayout.WEST);
        header.add(createListingButton, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        listingsPanel.setLayout(new BoxLayout(listingsPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(listingsPanel);

        add(scrollPane, BorderLayout.CENTER);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        updateListings();
    }

    private void updateListings() {
        listingsPanel.removeAll();

        for (Listing listing : profileViewModel.getMyListings()) {

            JPanel listingCard = new JPanel(new BorderLayout());
            listingCard.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            if (listing.getPhotoPath() != null && !listing.getPhotoPath().isEmpty()) {
                ImageIcon icon = new ImageIcon(listing.getPhotoPath());
                Image scaled = icon.getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH);

                JLabel imageLabel = new JLabel(new ImageIcon(scaled));
                listingCard.add(imageLabel, BorderLayout.WEST);
            }
            JLabel nameLabel = new JLabel(listing.getName());
            nameLabel.setFont(new Font("Arial", Font.BOLD, 14));

            listingCard.add(nameLabel, BorderLayout.CENTER);

            listingsPanel.add(listingCard);
        }

        listingsPanel.revalidate();
        listingsPanel.repaint();
    }

    public String getViewName() {
        return "profile";
    }
}