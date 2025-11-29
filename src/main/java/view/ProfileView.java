package view;

import Entities.Listing;
import interface_adapter.ProfileViewModel;
import interface_adapter.ViewManagerModel;
import interface_adapter.delete_listing.DeleteListingController;
import interface_adapter.edit_listing.EditListingController;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ProfileView extends JPanel implements PropertyChangeListener {

    private final ProfileViewModel profileViewModel;
    private final ViewManagerModel viewManagerModel;

    private DeleteListingController deleteListingController;
    private EditListingController editListingController;

    private final JPanel listingsPanel = new JPanel();

    public ProfileView(ProfileViewModel vm,
                       ViewManagerModel viewManagerModel,
                       DeleteListingController deleteListingController,
                       EditListingController editListingController) {

        this.profileViewModel = vm;
        this.viewManagerModel = viewManagerModel;
        this.deleteListingController = deleteListingController;
        this.editListingController = editListingController;

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

            JPanel wrapper = new JPanel(new BorderLayout());
            wrapper.add(card, BorderLayout.CENTER);


            JButton editButton = new JButton("Edit");
            editButton.setPreferredSize(new Dimension(100, 40));
            editButton.addActionListener(e -> {
                viewManagerModel.setState("create listing");
                viewManagerModel.firePropertyChange();

                SwingUtilities.invokeLater(() -> {
                    CreateListingView view =
                            (CreateListingView) SwingUtilities.getAncestorOfClass(
                                    CreateListingView.class, this);

                    Component root = SwingUtilities.getRoot(this);
                    if (root instanceof JFrame frame) {
                        for (Component comp : frame.getContentPane().getComponents()) {
                            if (comp instanceof JScrollPane scroll &&
                                    scroll.getViewport().getView() instanceof CreateListingView clv) {
                                clv.enterEditMode(listing);
                                break;
                            }
                        }
                    }
                });
            });

            JButton deleteButton = new JButton("Delete");
            deleteButton.setPreferredSize(new Dimension(100, 40));

            deleteButton.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to delete this listing?",
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
}
