package view;

import interface_adapter.ViewManagerModel;
import interface_adapter.create_listing.CreateListingState;
import interface_adapter.create_listing.CreateListingViewModel;
import interface_adapter.create_listing.CreateListingController;
import Entities.Listing;
import interface_adapter.edit_listing.EditListingController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * View for creating a new listing.
 */
public class CreateListingView extends JPanel implements PropertyChangeListener {

    private final String viewName = "create listing";

    private final CreateListingViewModel createListingViewModel;
    private final ViewManagerModel viewManagerModel;
    private CreateListingController createListingController = null;
    private boolean isEditMode = false;
    private EditListingController editListingController = null;
    private Listing editingTarget = null;

    private final JButton updateButton = new JButton("Update Listing");

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
    private final JLabel photoPathLabel = new JLabel("No photo selected");


    private final JLabel photoPreviewLabel = new JLabel();

    private final JButton createButton = new JButton(CreateListingViewModel.CREATE_BUTTON_LABEL);



    public CreateListingView(CreateListingViewModel createListingViewModel, ViewManagerModel viewManagerModel) {
        this.createListingViewModel = createListingViewModel;
        this.viewManagerModel = viewManagerModel;
        createListingViewModel.addPropertyChangeListener(this);

        this.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel(CreateListingViewModel.TITLE_LABEL);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton backToProfileButton = new JButton(CreateListingViewModel.PROFILE_BUTTON_LABEL);
        backToProfileButton.setPreferredSize(new Dimension(150, 30));
        backToProfileButton.addActionListener(e -> {
            viewManagerModel.setState("profile");
            viewManagerModel.firePropertyChange();
        });


        JPanel view = new JPanel();
        view.setLayout(new GridLayout(0, 2, 12, 12));
        view.setBorder(new EmptyBorder(10, 10, 10, 10));

        view.add(new JLabel(CreateListingViewModel.NAME_LABEL));
        view.add(nameInputField);

        view.add(new JLabel(CreateListingViewModel.DESCRIPTION_LABEL));
        view.add(new JScrollPane(descriptionInputArea));
        descriptionInputArea.setLineWrap(true);
        descriptionInputArea.setWrapStyleWord(true);

        view.add(new JLabel(CreateListingViewModel.PRICE_LABEL));
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


        view.add(new JLabel(CreateListingViewModel.PHOTO_LABEL));

        JPanel photoPanel = new JPanel();
        photoPanel.setLayout(new BorderLayout(5, 5));
        photoPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        JPanel buttonAndPath = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        buttonAndPath.add(uploadButton);
        buttonAndPath.add(photoPathLabel);

        photoPanel.add(buttonAndPath, BorderLayout.NORTH);

        photoPreviewLabel.setPreferredSize(new Dimension(150, 150));
        photoPreviewLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        photoPanel.add(photoPreviewLabel, BorderLayout.CENTER);

        view.add(photoPanel);

        JPanel buttons = new JPanel();
        buttons.setBorder(new EmptyBorder(10, 0, 0, 0));
        buttons.add(createButton);
        updateButton.setVisible(false);
        buttons.add(updateButton);
        buttons.add(backToProfileButton);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(title);

        add(Box.createRigidArea(new Dimension(0, 10)));
        add(view);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(buttons);

        addNameListener();
        addDescriptionListener();
        addPriceListener();
        addAddressListener();
        addAreaListener();
        addBedroomsListener();
        addBathroomsListener();
        setupButtonListeners();
    }

    public void enterEditMode(Listing listing) {
        this.isEditMode = true;
        this.editingTarget = listing;

        nameInputField.setText(listing.getName());
        descriptionInputArea.setText(listing.getDescription());
        priceField.setText(String.valueOf(listing.getPrice()));
        addressField.setText(listing.getAddress());
        areaField.setText(String.valueOf(listing.getArea()));
        bedroomsField.setText(String.valueOf(listing.getBedrooms()));
        bathroomsField.setText(String.valueOf(listing.getBathrooms()));
        buildingTypeDropdown.setSelectedItem(listing.getBuildingType());
        photoPathLabel.setText(listing.getPhotoPath());

        createButton.setVisible(false);
        updateButton.setVisible(true);
    }

    public void resetToCreateMode() {
        isEditMode = false;
        editingTarget = null;

        nameInputField.setText("");
        descriptionInputArea.setText("");
        priceField.setText("");
        addressField.setText("");
        areaField.setText("");
        bedroomsField.setText("");
        bathroomsField.setText("");
        buildingTypeDropdown.setSelectedIndex(0);
        photoPathLabel.setText("No photo selected");
        photoPreviewLabel.setIcon(null);

        createButton.setVisible(true);
        updateButton.setVisible(false);
    }

    private void addNameListener() {
        nameInputField.getDocument().addDocumentListener(new DocumentListener() {
            private void update() {
                CreateListingState state = createListingViewModel.getState();
                state.setName(nameInputField.getText());
                createListingViewModel.setState(state);
            }
            @Override public void insertUpdate(DocumentEvent e) { update(); }
            @Override public void removeUpdate(DocumentEvent e) { update(); }
            @Override public void changedUpdate(DocumentEvent e) { update(); }
        });
    }

    private void addDescriptionListener() {
        descriptionInputArea.getDocument().addDocumentListener(new DocumentListener() {
            private void update() {
                CreateListingState state = createListingViewModel.getState();
                state.setDescription(descriptionInputArea.getText());
                createListingViewModel.setState(state);
            }
            @Override public void insertUpdate(DocumentEvent e) { update(); }
            @Override public void removeUpdate(DocumentEvent e) { update(); }
            @Override public void changedUpdate(DocumentEvent e) { update(); }
        });
    }
    private void addPriceListener() {
        priceField.getDocument().addDocumentListener(new DocumentListener() {
            private void update() {
                CreateListingState state = createListingViewModel.getState();
                state.setPrice(priceField.getText());
                createListingViewModel.setState(state);
            }
            @Override public void insertUpdate(DocumentEvent e) { update(); }
            @Override public void removeUpdate(DocumentEvent e) { update(); }
            @Override public void changedUpdate(DocumentEvent e) { update(); }
        });
    }

    private void addAddressListener() {
        addressField.getDocument().addDocumentListener(new DocumentListener() {
            private void update() {
                CreateListingState state = createListingViewModel.getState();
                state.setAddress(addressField.getText());
                createListingViewModel.setState(state);
            }
            @Override public void insertUpdate(DocumentEvent e) { update(); }
            @Override public void removeUpdate(DocumentEvent e) { update(); }
            @Override public void changedUpdate(DocumentEvent e) { update(); }
        });
    }

    private void addAreaListener() {
        areaField.getDocument().addDocumentListener(new DocumentListener() {
            private void update() {
                CreateListingState state = createListingViewModel.getState();
                state.setArea(areaField.getText());
                createListingViewModel.setState(state);
            }
            @Override public void insertUpdate(DocumentEvent e) { update(); }
            @Override public void removeUpdate(DocumentEvent e) { update(); }
            @Override public void changedUpdate(DocumentEvent e) { update(); }
        });
    }

    private void addBedroomsListener() {
        bedroomsField.getDocument().addDocumentListener(new DocumentListener() {
            private void update() {
                CreateListingState state = createListingViewModel.getState();
                state.setBedrooms(bedroomsField.getText());
                createListingViewModel.setState(state);
            }
            @Override public void insertUpdate(DocumentEvent e) { update(); }
            @Override public void removeUpdate(DocumentEvent e) { update(); }
            @Override public void changedUpdate(DocumentEvent e) { update(); }
        });
    }

    private void addBathroomsListener() {
        bathroomsField.getDocument().addDocumentListener(new DocumentListener() {
            private void update() {
                CreateListingState state = createListingViewModel.getState();
                state.setBathrooms(bathroomsField.getText());
                createListingViewModel.setState(state);
            }
            @Override public void insertUpdate(DocumentEvent e) { update(); }
            @Override public void removeUpdate(DocumentEvent e) { update(); }
            @Override public void changedUpdate(DocumentEvent e) { update(); }
        });
    }

    private void setupButtonListeners() {
        createButton.addActionListener(e -> {

            double price;
            try {
                price = Double.parseDouble(priceField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Price must be a positive number.");
                return;
            }

            double area;
            try {
                area = Double.parseDouble(areaField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Area must be a positive number.");
                return;
            }

            int bedrooms = 0;
            try {
                bedrooms = Integer.parseInt(bedroomsField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Bedrooms must be a whole positive number.");
                return;
            }

            int bathrooms = 0;
            try {
                bathrooms = Integer.parseInt(bathroomsField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Bathrooms must be a whole positive number.");
                return;
            }

            if (createListingController != null) {
                createListingController.execute(
                        nameInputField.getText(),
                        photoPathLabel.getText(),
                        null,
                        null,
                        descriptionInputArea.getText(),
                        price,
                        addressField.getText(),
                        1.0,
                        area,
                        bedrooms,
                        bathrooms,
                        buildingTypeDropdown.getItemAt(buildingTypeDropdown.getSelectedIndex()),
                        true
                );
            }
        });

        uploadButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int result = chooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                String path = chooser.getSelectedFile().getAbsolutePath();

                photoPathLabel.setText(path);

                ImageIcon icon = new ImageIcon(path);
                Image img = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                photoPreviewLabel.setIcon(new ImageIcon(img));
            }
        });

        updateButton.addActionListener(e -> {

            if (editListingController == null || editingTarget == null) {
                return;
            }

            double price, area;
            int bedrooms, bathrooms;

            try { price = Double.parseDouble(priceField.getText()); }
            catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Price must be positive number.");
                return;
            }

            try { area = Double.parseDouble(areaField.getText()); }
            catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Area must be a positive numbert.");
                return;
            }

            try { bedrooms = Integer.parseInt(bedroomsField.getText()); }
            catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Bedrooms must be a whole number.");
                return;
            }

            try { bathrooms = Integer.parseInt(bathroomsField.getText()); }
            catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Bathrooms must be a whole number.");
                return;
            }

            editingTarget.setName(nameInputField.getText());
            editingTarget.setDescription(descriptionInputArea.getText());
            editingTarget.setPrice(price);
            editingTarget.setAddress(addressField.getText());
            editingTarget.setArea(area);
            editingTarget.setBedrooms(bedrooms);
            editingTarget.setBathrooms(bathrooms);
            editingTarget.setBuildingType(
                    buildingTypeDropdown.getItemAt(buildingTypeDropdown.getSelectedIndex()));
            editingTarget.setPhotoPath(photoPathLabel.getText());
            editListingController.saveEdits(editingTarget);
        });
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        CreateListingState state = (CreateListingState) evt.getNewValue();
        if (state.getNameError() != null) {
            JOptionPane.showMessageDialog(this, state.getNameError());
        }
    }

    public String getViewName() { return viewName; }

    public void setCreateListingController(CreateListingController controller) {
        this.createListingController = controller;
    }

    public void setEditListingController(EditListingController controller) {
        this.editListingController = controller;
    }
}