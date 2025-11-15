package view;

import interface_adapter.create_listing.CreateListingState;
import interface_adapter.create_listing.CreateListingViewModel;
import interface_adapter.create_listing.CreateListingController;

import Entities.Listing;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * View for creating a new listing.
 */
public class CreateListingView extends JPanel implements ActionListener, PropertyChangeListener {

    private final String viewName = "create listing";

    private final CreateListingViewModel createListingViewModel;
    private CreateListingController createListingController = null;

    private final JTextField nameInputField = new JTextField(15);
    private final JTextArea descriptionInputArea = new JTextArea(4, 15);
    private final JTextField priceField = new JTextField(15);
    private final JTextField addressField = new JTextField(15);
    private final JTextField areaField = new JTextField(15);
    private final JTextField bedroomsField = new JTextField(15);
    private final JTextField bathroomsField = new JTextField(15);

    private final JComboBox<Listing.BuildingType> buildingTypeDropdown =
            new JComboBox<>(Listing.BuildingType.values());

    private final JButton uploadButton = new JButton("Upload Photo");
    private final JLabel photoPathLabel = new JLabel("No file selected");

    private final JButton createButton;
    private final JButton cancelButton;

    public CreateListingView(CreateListingViewModel createListingViewModel) {
        this.createListingViewModel = createListingViewModel;
        createListingViewModel.addPropertyChangeListener(this);

        JLabel title = new JLabel(CreateListingViewModel.TITLE_LABEL);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel view = new JPanel();
        view.setLayout(new GridLayout(0, 2, 8, 8));

        view.add(new JLabel(CreateListingViewModel.NAME_LABEL));
        view.add(nameInputField);

        view.add(new JLabel(CreateListingViewModel.DESCRIPTION_LABEL));
        view.add(new JScrollPane(descriptionInputArea));

        /* view.add(new JLabel(CreateListingViewModel.PRICE_LABEL));
        view.add(priceField);

        view.add(new JLabel(CreateListingViewModel.ADDRESS_LABEL));
        view.add(addressField);

        view.add(new JLabel(CreateListingViewModel.AREA_LABEL));
        view.add(areaField);

        view.add(new JLabel(CreateListingViewModel.BEDROOMS_LABEL));
        view.add(bedroomsField);

        view.add(new JLabel(CreateListingViewModel.BATHROOMS_LABEL));
        view.add(bathroomsField);

        view.add(new JLabel(CreateListingViewModel.BUILDING_TYPE_LABEL));
        view.add(buildingTypeDropdown);
        */
        view.add(new JLabel(CreateListingViewModel.PHOTO_LABEL));

        JPanel photoPanel = new JPanel();
        photoPanel.add(uploadButton);
        photoPanel.add(photoPathLabel);

        view.add(photoPanel);

        createButton = new JButton(CreateListingViewModel.CREATE_BUTTON_LABEL);
        cancelButton = new JButton(CreateListingViewModel.CANCEL_BUTTON_LABEL);

        JPanel buttons = new JPanel();
        buttons.add(createButton);
        buttons.add(cancelButton);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(title);
        add(view);
        add(buttons);
    }

    private void addNameListener() {
        descriptionInputArea.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final CreateListingState currentState = createListingViewModel.getState();
                currentState.setName(descriptionInputArea.getText());
                createListingViewModel.setState(currentState);
            }
            @Override
            public void insertUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                documentListenerHelper();
            }
        });
    }

    private void addDescriptionListener() {
        nameInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final CreateListingState currentState = createListingViewModel.getState();
                currentState.setName(nameInputField.getText());
                createListingViewModel.setState(currentState);
            }
            @Override
            public void insertUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                documentListenerHelper();
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        JOptionPane.showMessageDialog(this, "Not fully implemented");
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final CreateListingState state = (CreateListingState) evt.getNewValue();
        if (state.getNameError() != null) {
            JOptionPane.showMessageDialog(this, state.getNameError());
        }
    }

    public String getViewName() {
        return viewName;
    }

    public void setCreateListingController(CreateListingController controller) {
        this.createListingController = controller;
    }
}