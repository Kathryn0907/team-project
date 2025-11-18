package view;

import interface_adapter.create_listing.CreateListingState;
import interface_adapter.create_listing.CreateListingViewModel;
import interface_adapter.create_listing.CreateListingController;
import Entities.Listing;

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

    private final JLabel photoPreviewLabel = new JLabel();

    private final JButton createButton = new JButton(CreateListingViewModel.CREATE_BUTTON_LABEL);
    private final JButton cancelButton = new JButton(CreateListingViewModel.CANCEL_BUTTON_LABEL);

    public CreateListingView(CreateListingViewModel createListingViewModel) {
        this.createListingViewModel = createListingViewModel;
        createListingViewModel.addPropertyChangeListener(this);

        this.setBorder(new EmptyBorder(20, 20, 20, 20)); // top, left, bottom, right

        JLabel title = new JLabel(CreateListingViewModel.TITLE_LABEL);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel view = new JPanel();
        view.setLayout(new GridLayout(0, 2, 12, 12));
        view.setBorder(new EmptyBorder(10, 10, 10, 10));

        view.add(new JLabel(CreateListingViewModel.NAME_LABEL));
        view.add(nameInputField);

        view.add(new JLabel(CreateListingViewModel.DESCRIPTION_LABEL));
        view.add(new JScrollPane(descriptionInputArea));
        descriptionInputArea.setLineWrap(true);
        descriptionInputArea.setWrapStyleWord(true);

    /*
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
    */

        view.add(new JLabel(CreateListingViewModel.PHOTO_LABEL));

        JPanel photoPanel = new JPanel();
        photoPanel.setLayout(new BorderLayout(5, 5));  // spacing inside the photo panel
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
        buttons.setBorder(new EmptyBorder(10, 0, 0, 0));   // space above buttons
        buttons.add(createButton);
        buttons.add(cancelButton);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(title);

        add(Box.createRigidArea(new Dimension(0, 10))); // space between title and form
        add(view);
        add(Box.createRigidArea(new Dimension(0, 10))); // space between form and buttons
        add(buttons);

        addNameListener();
        addDescriptionListener();
        setupButtonListeners();
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

    private void setupButtonListeners() {
        createButton.addActionListener(e -> {
            if (createListingController != null) {
                createListingController.execute(
                        nameInputField.getText(),
                        null, // owner for testing
                        photoPathLabel.getText(),
                        null, // tags
                        null, // mainCategories
                        descriptionInputArea.getText(),
                        100.0,
                        "123 Test St",
                        1.0,
                        50.0,
                        1,
                        1,
                        Listing.BuildingType.OTHER,
                        true
                );
            }
        });

        cancelButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Cancel pressed"));

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
}