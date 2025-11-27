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

        JButton backToSearchButton = new JButton("← Back to Search");
        backToSearchButton.addActionListener(e -> {
            viewManagerModel.setState("logged in");
            viewManagerModel.firePropertyChange();
        });

        JButton createListingButton = new JButton("Create Listing");
        createListingButton.addActionListener(e -> {
            viewManagerModel.setState("create listing");
            viewManagerModel.firePropertyChange();
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.add(backToSearchButton);
        buttonPanel.add(createListingButton);

        header.add(title, BorderLayout.WEST);
        header.add(buttonPanel, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        listingsPanel.setLayout(new BoxLayout(listingsPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(listingsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        add(scrollPane, BorderLayout.CENTER);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        updateListings();
    }

    private void updateListings() {
        listingsPanel.removeAll();

        for (Listing listing : profileViewModel.getMyListings()) {

            ListingCardPanel card = new ListingCardPanel(listing);

            card.setAlignmentX(Component.LEFT_ALIGNMENT);
            listingsPanel.add(card);

            listingsPanel.add(Box.createVerticalStrut(10)); // spacing between cards
        }

        listingsPanel.revalidate();
        listingsPanel.repaint();
    }

    public String getViewName() {
        return "profile";
    }
}
