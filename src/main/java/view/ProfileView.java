package view;

import Entities.Listing;
import interface_adapter.ProfileViewModel;
import interface_adapter.ViewManagerModel;
import interface_adapter.delete_listing.DeleteListingController;
import interface_adapter.edit_listing.EditListingController;
import interface_adapter.logged_in.LoggedInViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ProfileView extends JPanel implements PropertyChangeListener {

    private final ProfileViewModel profileViewModel;
    private final ViewManagerModel viewManagerModel;
    private final LoggedInViewModel loggedInViewModel;

    private DeleteListingController deleteListingController;
    private EditListingController editListingController;
    private CreateListingView createListingView;
    private EditListingView editListingView;

    private final JPanel listingsPanel = new JPanel();

    public ProfileView(ProfileViewModel vm,
                       ViewManagerModel viewManagerModel,
                       DeleteListingController deleteListingController,
                       EditListingController editListingController) {

        this.profileViewModel = vm;
        this.viewManagerModel = viewManagerModel;
        this.loggedInViewModel = LoggedInViewModel.getInstance();
        this.deleteListingController = deleteListingController;
        this.editListingController = editListingController;

        vm.addPropertyChangeListener(this);

        setLayout(new BorderLayout());

        JPanel header = new JPanel(new BorderLayout());

        JLabel title = new JLabel("My Profile");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JButton backToSearchButton = new JButton("← Back to Search");
        backToSearchButton.addActionListener(e -> {
            viewManagerModel.setState("logged in");
            viewManagerModel.firePropertyChange();
        });

        JButton createListingButton = new JButton("+ Create Listing");
        createListingButton.addActionListener(e -> {
            if (createListingView != null) {
                createListingView.resetToCreateMode();
            }
            viewManagerModel.setState("create listing");
            viewManagerModel.firePropertyChange();
        });

        JButton viewMyListingsButton = new JButton("🏠 Refresh My Listings");
        viewMyListingsButton.addActionListener(e -> {
            loadMyListingsFromMongoDB();
        });

        JButton cancelAccountButton = new JButton("❌ Cancel Account");
        cancelAccountButton.addActionListener(e -> {
            viewManagerModel.setState("cancel account");
            viewManagerModel.firePropertyChange();
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.add(backToSearchButton);
        buttonPanel.add(viewMyListingsButton);
        buttonPanel.add(createListingButton);
        buttonPanel.add(cancelAccountButton);

        header.add(title, BorderLayout.WEST);
        header.add(buttonPanel, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        listingsPanel.setLayout(new BoxLayout(listingsPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(listingsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Load user's listings from MongoDB via the User entity's myListings attribute
     */
    private void loadMyListingsFromMongoDB() {
        String username = loggedInViewModel.getState().getUsername();

        if (username == null || username.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please log in to view your listings",
                    "Not Logged In",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Get user from MongoDB
            data_access.MongoDBUserDAO userDAO = new data_access.MongoDBUserDAO();
            Entities.User user = userDAO.getUserByUsername(username);

            if (user == null) {
                JOptionPane.showMessageDialog(this,
                        "User not found in database",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Get user's listings from the User entity
            java.util.ArrayList<Listing> myListings = user.getMyListings();

            // Update the profile view model
            profileViewModel.setListings(myListings);

            System.out.println("✅ Loaded " + myListings.size() + " listings for user: " + username);

        } catch (Exception e) {
            System.err.println("❌ Error loading listings: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading listings: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        updateListings();
    }

    private void updateListings() {
        listingsPanel.removeAll();

        java.util.List<Listing> listings = profileViewModel.getMyListings();

        if (listings.isEmpty()) {
            JLabel emptyLabel = new JLabel("No listings yet. Create your first listing!");
            emptyLabel.setFont(new Font("Arial", Font.ITALIC, 14));
            emptyLabel.setForeground(Color.GRAY);
            emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);

            JPanel emptyPanel = new JPanel(new BorderLayout());
            emptyPanel.setBorder(BorderFactory.createEmptyBorder(50, 20, 50, 20));
            emptyPanel.add(emptyLabel, BorderLayout.CENTER);

            listingsPanel.add(emptyPanel);
        } else {
            for (Listing listing : listings) {
                ListingCardPanel card = new ListingCardPanel(listing);

                JPanel wrapper = new JPanel(new BorderLayout());
                wrapper.add(card, BorderLayout.CENTER);

                JButton editButton = new JButton("✏️ Edit");
                editButton.setPreferredSize(new Dimension(100, 40));

                editButton.addActionListener(e -> {
                    if (createListingView != null) {
                        createListingView.enterEditMode(listing);
                        viewManagerModel.setState("create listing");
                        viewManagerModel.firePropertyChange();
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Create listing view not initialized",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                });

                JButton deleteButton = new JButton("🗑️ Delete");
                deleteButton.setPreferredSize(new Dimension(100, 40));

                deleteButton.addActionListener(e -> {
                    int confirm = JOptionPane.showConfirmDialog(
                            this,
                            "Are you sure you want to delete \"" + listing.getName() + "\"?",
                            "Confirm Delete",
                            JOptionPane.YES_NO_OPTION
                    );
                    if (confirm == JOptionPane.YES_OPTION) {
                        deleteListingController.execute(listing.getId(), listing.getOwnerId());
                    }
                });

                JPanel rightPanel = new JPanel();
                rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
                rightPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

                rightPanel.add(editButton);
                rightPanel.add(Box.createVerticalStrut(10));
                rightPanel.add(deleteButton);

                wrapper.add(rightPanel, BorderLayout.EAST);

                wrapper.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
                wrapper.setAlignmentX(Component.LEFT_ALIGNMENT);

                listingsPanel.add(wrapper);
                listingsPanel.add(Box.createVerticalStrut(10));
            }
        }

        listingsPanel.revalidate();
        listingsPanel.repaint();
    }

    public void setDeleteListingController(DeleteListingController controller) {
        this.deleteListingController = controller;
    }

    public void setEditListingController(EditListingController controller) {
        this.editListingController = controller;
    }

    public String getViewName() {
        return "profile";
    }

    public void setCreateListingView(CreateListingView createListingView) {
        this.createListingView = createListingView;
    }

    /**
     * Call this when the view becomes visible to load listings from MongoDB
     */
    public void onViewShown() {
        loadMyListingsFromMongoDB();
    }
}
    public void setEditListingView(EditListingView editListingView) {
        this.editListingView = editListingView;
    }
}
