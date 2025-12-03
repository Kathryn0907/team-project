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
    private final JComboBox<String> buildingTypeComboBox;

    private final JButton applyButton = new JButton("Filter");
    private final JButton resetButton = new JButton("Clear Filters");

    public FilterSidebarPanel(FilterListingsController controller) {
        this.controller = controller;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        gbc.gridx = 0;
        gbc.gridy = row;
        add(new JLabel("User Address:"), gbc);
        gbc.gridx = 1;
        add(userAddressField, gbc);
        row++;

        gbc.gridx = 0;
        gbc.gridy = row;
        add(new JLabel("Max dist (km):"), gbc);
        gbc.gridx = 1;
        add(maxDistanceField, gbc);
        row++;

        gbc.gridx = 0;
        gbc.gridy = row;
        add(new JLabel("Min price (usd):"), gbc);
        gbc.gridx = 1;
        add(minPriceField, gbc);
        row++;

        gbc.gridx = 0;
        gbc.gridy = row;
        add(new JLabel("Max price (usd) :"), gbc);
        gbc.gridx = 1;
        add(maxPriceField, gbc);
        row++;

        gbc.gridx = 0;
        gbc.gridy = row;
        add(new JLabel("Min area(sq ft):"), gbc);
        gbc.gridx = 1;
        add(minAreaField, gbc);
        row++;

        gbc.gridx = 0;
        gbc.gridy = row;
        add(new JLabel("Max area(sq ft):"), gbc);
        gbc.gridx = 1;
        add(maxAreaField, gbc);
        row++;

        gbc.gridx = 0;
        gbc.gridy = row;
        add(new JLabel("Min beds:"), gbc);
        gbc.gridx = 1;
        add(minBedroomsField, gbc);
        row++;

        gbc.gridx = 0;
        gbc.gridy = row;
        add(new JLabel("Min baths:"), gbc);
        gbc.gridx = 1;
        add(minBathroomsField, gbc);
        row++;

        buildingTypeComboBox = new JComboBox<>();
        buildingTypeComboBox.addItem("-- Any Type --");
        for (Listing.BuildingType type : Listing.BuildingType.values()) {
            buildingTypeComboBox.addItem(type.name());
        }

        gbc.gridx = 0;
        gbc.gridy = row;
        add(new JLabel("Building type:"), gbc);
        gbc.gridx = 1;
        add(buildingTypeComboBox, gbc);
        row++;

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        buttonPanel.add(applyButton);
        buttonPanel.add(resetButton);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        add(buttonPanel, gbc);

        applyButton.addActionListener(e -> onApplyFilter());
        resetButton.addActionListener(e -> resetFilters());
    }

    private void resetFilters() {
        System.out.println("=== RESET FILTERS CALLED ===");

        userAddressField.setText("");
        maxDistanceField.setText("");
        minPriceField.setText("");
        maxPriceField.setText("");
        minAreaField.setText("");
        maxAreaField.setText("");
        minBedroomsField.setText("");
        minBathroomsField.setText("");
        buildingTypeComboBox.setSelectedIndex(0);

        System.out.println("All fields cleared");

        onApplyFilter();
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

            Listing.BuildingType buildingType = null;
            if (buildingTypeComboBox.getSelectedIndex() > 0) {
                String selected = (String) buildingTypeComboBox.getSelectedItem();
                buildingType = Listing.BuildingType.valueOf(selected);
            }

            controller.applyFilter(
                    userAddress, maxDistance, minPrice, maxPrice,
                    minArea, maxArea, minBedrooms, minBathrooms, buildingType
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
