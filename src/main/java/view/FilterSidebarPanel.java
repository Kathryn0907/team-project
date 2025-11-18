package view;

import Entities.Listing;
import interface_adapter.filter.FilterListingsController;

import javax.swing.*;
import java.awt.*;

public class FilterSidebarPanel extends JPanel {

    private final FilterListingsController controller;

    private final JTextField userAddressField = new JTextField(15);
    private final JTextField maxDistanceField = new JTextField(6);
    private final JTextField minPriceField = new JTextField(6);
    private final JTextField maxPriceField = new JTextField(6);
    private final JTextField minAreaField = new JTextField(6);
    private final JTextField maxAreaField = new JTextField(6);
    private final JTextField minBedroomsField = new JTextField(4);
    private final JTextField minBathroomsField = new JTextField(4);
    private final JComboBox<Listing.BuildingType> buildingTypeComboBox =
            new JComboBox<>(Listing.BuildingType.values());

    private final JButton applyButton = new JButton("Filter");

    public FilterSidebarPanel(FilterListingsController controller) {
        this.controller = controller;

        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder("Filters"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 3, 3, 3);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        addRow("Address:", userAddressField, row++, gbc);
        addRow("Max dist (km):", maxDistanceField, row++, gbc);
        addRow("Min price:", minPriceField, row++, gbc);
        addRow("Max price:", maxPriceField, row++, gbc);
        addRow("Min area:", minAreaField, row++, gbc);
        addRow("Max area:", maxAreaField, row++, gbc);
        addRow("Min beds:", minBedroomsField, row++, gbc);
        addRow("Min baths:", minBathroomsField, row++, gbc);
        addRow("Building type:", buildingTypeComboBox, row++, gbc);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        add(applyButton, gbc);

        applyButton.addActionListener(e -> onApplyFilter());
    }

    private void addRow(String label, JComponent comp, int row, GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        add(new JLabel(label), gbc);

        gbc.gridx = 1;
        add(comp, gbc);
    }

    private void onApplyFilter() {
        try {
            String userAddress = emptyToNull(userAddressField.getText());
            Double maxDistance = parseDoubleOrNull(maxDistanceField.getText());
            Double minPrice = parseDoubleOrNull(minPriceField.getText());
            Double maxPrice = parseDoubleOrNull(maxPriceField.getText());
            Double minArea = parseDoubleOrNull(minAreaField.getText());
            Double maxArea = parseDoubleOrNull(maxAreaField.getText());
            Integer minBedrooms = parseIntOrNull(minBedroomsField.getText());
            Integer minBathrooms = parseIntOrNull(minBathroomsField.getText());
            Listing.BuildingType buildingType =
                    (Listing.BuildingType) buildingTypeComboBox.getSelectedItem();

            controller.applyFilter(
                    userAddress,
                    maxDistance,
                    minPrice,
                    maxPrice,
                    minArea,
                    maxArea,
                    minBedrooms,
                    minBathrooms,
                    buildingType
            );

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Please enter valid numbers for distance, price, area, bedrooms and bathrooms.",
                    "Invalid input",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private String emptyToNull(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }

    private Double parseDoubleOrNull(String text) {
        text = emptyToNull(text);
        if (text == null) return null;
        return Double.parseDouble(text);
    }

    private Integer parseIntOrNull(String text) {
        text = emptyToNull(text);
        if (text == null) return null;
        return Integer.parseInt(text);
    }
}
