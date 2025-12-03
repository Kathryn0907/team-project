package view;

import interface_adapter.ViewManagerModel;
import interface_adapter.create_listing.CreateListingState;
import interface_adapter.create_listing.CreateListingViewModel;
import interface_adapter.edit_listing.EditListingController;
import Entities.Listing;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

public class EditListingView extends JPanel implements PropertyChangeListener {

    private final String viewName = "edit listing";

    private final CreateListingViewModel createListingViewModel;
    private EditListingController editListingController = null;
    private Listing editingTarget = null;

    // CRITICAL: Store the original photo Base64 string
    private String originalPhotoBase64 = null;
    private boolean photoChanged = false;

    private final JButton updateButton = new JButton("Update Listing");

    private final JTextField nameInputField = new JTextField(15);
    private final JTextArea descriptionInputArea = new JTextArea(4, 15);
    private final JTextField priceField = new JTextField(15);
    private final JTextField addressField = new JTextField(15);
    private final JTextField areaField = new JTextField(15);
    private final JTextField bedroomsField = new JTextField(15);
    private final JTextField bathroomsField = new JTextField(15);

    private final JTextField tagInputField = new JTextField(15);
    private final DefaultListModel<String> tagListModel = new DefaultListModel<>();
    private final JList<String> tagList = new JList<>(tagListModel);
    private final JButton addTagButton = new JButton("Add Tag");

    private final JComboBox<Listing.BuildingType> buildingTypeDropdown =
            new JComboBox<>(Listing.BuildingType.values());

    private final JButton uploadButton = new JButton("Upload Photo");
    private final JLabel photoPathLabel = new JLabel("No Photo");
    private final JLabel photoPreviewLabel = new JLabel();

    public EditListingView(CreateListingViewModel vm) {
        this.createListingViewModel = vm;
        vm.addPropertyChangeListener(this);

        this.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Edit Listing");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel formPanel = buildFormPanel();

        JPanel buttons = new JPanel();
        buttons.add(updateButton);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(title);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(formPanel);
        add(buttons);

        updateButton.setVisible(true);

        addListeners();
    }

    private JPanel buildFormPanel() {
        JPanel view = new JPanel(new GridLayout(0, 2, 12, 12));
        view.setBorder(new EmptyBorder(10, 10, 10, 10));

        view.add(new JLabel("Name:"));
        view.add(nameInputField);

        view.add(new JLabel("Description:"));
        descriptionInputArea.setLineWrap(true);
        descriptionInputArea.setWrapStyleWord(true);
        view.add(new JScrollPane(descriptionInputArea));

        view.add(new JLabel("Price:"));
        view.add(priceField);

        view.add(new JLabel("Address:"));
        view.add(addressField);

        view.add(new JLabel("Area:"));
        view.add(areaField);

        view.add(new JLabel("Bedrooms:"));
        view.add(bedroomsField);

        view.add(new JLabel("Bathrooms:"));
        view.add(bathroomsField);

        view.add(new JLabel("Building Type:"));
        view.add(buildingTypeDropdown);

        view.add(new JLabel("Tags:"));
        JPanel tagPanel = new JPanel(new BorderLayout(3,3));
        tagPanel.add(tagInputField, BorderLayout.NORTH);
        tagPanel.add(addTagButton, BorderLayout.CENTER);
        tagList.setVisibleRowCount(4);
        tagList.setFixedCellWidth(100);
        tagPanel.add(new JScrollPane(tagList), BorderLayout.SOUTH);
        view.add(tagPanel);

        view.add(new JLabel("Photo:"));
        JPanel photoPanel = new JPanel(new BorderLayout(5, 5));
        JPanel buttonAndPath = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        buttonAndPath.add(uploadButton);
        buttonAndPath.add(photoPathLabel);

        photoPanel.add(buttonAndPath, BorderLayout.NORTH);
        photoPreviewLabel.setPreferredSize(new Dimension(150, 150));
        photoPreviewLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        photoPanel.add(photoPreviewLabel, BorderLayout.CENTER);

        view.add(photoPanel);

        return view;
    }

    private void addListeners() {
        addDocumentListener(nameInputField, text -> getState().setName(text));
        addDocumentListener(descriptionInputArea, text -> getState().setDescription(text));
        addDocumentListener(priceField, text -> getState().setPrice(text));
        addDocumentListener(addressField, text -> getState().setAddress(text));
        addDocumentListener(areaField, text -> getState().setArea(text));
        addDocumentListener(bedroomsField, text -> getState().setBedrooms(text));
        addDocumentListener(bathroomsField, text -> getState().setBathrooms(text));

        addTagButton.addActionListener(e -> {
            String tag = tagInputField.getText().trim();
            if (!tag.isEmpty() && !tagListModel.contains(tag)) {
                tagListModel.addElement(tag);
                getState().setTags(java.util.Collections.list(tagListModel.elements()));
                createListingViewModel.setState(getState());
                tagInputField.setText("");
            }
        });

        uploadButton.addActionListener(e -> handlePhotoSelection());

        updateButton.addActionListener(e -> handleUpdate());
    }

    private void addDocumentListener(JTextComponent field, java.util.function.Consumer<String> setter) {
        field.getDocument().addDocumentListener(new DocumentListener() {
            private void update() {
                setter.accept(field.getText());
                createListingViewModel.setState(getState());
            }

            @Override public void insertUpdate(DocumentEvent e) { update(); }
            @Override public void removeUpdate(DocumentEvent e) { update(); }
            @Override public void changedUpdate(DocumentEvent e) { update(); }
        });
    }

    private void handlePhotoSelection() {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            photoPathLabel.setText(file.getName());
            ImageIcon icon = new ImageIcon(file.getAbsolutePath());
            Image img = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            photoPreviewLabel.setIcon(new ImageIcon(img));

            try {
                byte[] bytes = Files.readAllBytes(file.toPath());
                String newPhotoBase64 = Base64.getEncoder().encodeToString(bytes);

                // Mark that photo has been changed
                photoChanged = true;

                getState().setPhotoBase64(newPhotoBase64);
                createListingViewModel.setState(getState());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Failed to load photo.");
            }
        }
    }

    private void handleUpdate() {
        if (editListingController == null || editingTarget == null) return;

        try {
            double price = Double.parseDouble(priceField.getText());
            double area = Double.parseDouble(areaField.getText());
            int bedrooms = Integer.parseInt(bedroomsField.getText());
            int bathrooms = Integer.parseInt(bathroomsField.getText());

            editingTarget.setName(nameInputField.getText());
            editingTarget.setDescription(descriptionInputArea.getText());
            editingTarget.setPrice(price);
            editingTarget.setAddress(addressField.getText());
            editingTarget.setArea(area);
            editingTarget.setBedrooms(bedrooms);
            editingTarget.setBathrooms(bathrooms);
            editingTarget.setBuildingType(
                    buildingTypeDropdown.getItemAt(buildingTypeDropdown.getSelectedIndex()));

            // Get tags from the tag list model
            java.util.List<String> currentTags = java.util.Collections.list(tagListModel.elements());
            editingTarget.setTags(currentTags);

            // CRITICAL FIX: Only update photo if a new one was uploaded
            if (photoChanged) {
                editingTarget.setPhotoBase64(getState().getPhotoBase64());
            } else {
                // Keep the original photo
                editingTarget.setPhotoBase64(originalPhotoBase64);
            }

            editListingController.saveEdits(editingTarget);

            // Reset photo changed flag
            photoChanged = false;

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Please enter valid numbers for price, area, bedrooms, and bathrooms.");
        }
    }

    public void loadListing(Listing listing) {
        this.editingTarget = listing;

        // CRITICAL: Store the original photo Base64
        this.originalPhotoBase64 = listing.getPhotoBase64();
        this.photoChanged = false; // Reset flag

        nameInputField.setText(listing.getName());
        descriptionInputArea.setText(listing.getDescription());
        priceField.setText(String.valueOf(listing.getPrice()));
        addressField.setText(listing.getAddress());
        areaField.setText(String.valueOf(listing.getArea()));
        bedroomsField.setText(String.valueOf(listing.getBedrooms()));
        bathroomsField.setText(String.valueOf(listing.getBathrooms()));
        buildingTypeDropdown.setSelectedItem(listing.getBuildingType());

        // Load tags into the list model
        tagListModel.clear();
        for (String tag : listing.getTags()) {
            tagListModel.addElement(tag);
        }

        // Load and display the existing photo
        String base64 = listing.getPhotoBase64();
        if (base64 != null && !base64.isEmpty()) {
            try {
                byte[] bytes = Base64.getDecoder().decode(base64);
                ImageIcon icon = new ImageIcon(bytes);
                Image img = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                photoPreviewLabel.setIcon(new ImageIcon(img));
                photoPathLabel.setText("Current photo");
            } catch (Exception e) {
                photoPathLabel.setText("No photo");
                photoPreviewLabel.setIcon(null);
            }
        } else {
            photoPathLabel.setText("No photo");
            photoPreviewLabel.setIcon(null);
        }

        // Also set the photo in the state
        CreateListingState state = getState();
        state.setPhotoBase64(originalPhotoBase64);
        createListingViewModel.setState(state);
    }

    private CreateListingState getState() {
        return createListingViewModel.getState();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {}

    public String getViewName() { return viewName; }

    public void setEditListingController(EditListingController controller) {
        this.editListingController = controller;
    }
}