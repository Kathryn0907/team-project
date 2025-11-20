import app.ExtractTagsUseCaseFactory;
import data_access.InMemoryListingDAO;
import interface_adapter.extract_tags.*;
import use_case.extract_tags.ExtractTagsOutputData;
import Entities.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ExtractTagsUITest {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Imagga Tag Extraction Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 500);
        frame.setLayout(new BorderLayout(10, 10));

        // Setup data access
        InMemoryListingDAO dataAccess = new InMemoryListingDAO();
        User testUser = new User("test_user", "password");
        dataAccess.addUser(testUser);

        // Top panel: Input
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel listingNameLabel = new JLabel("Listing Name:");
        JTextField listingNameField = new JTextField("Test Listing");

        JLabel imageUrlLabel = new JLabel("Image URL:");
        JTextField imageUrlField = new JTextField("https://images.unsplash.com/photo-1564013799919-ab600027ffc6");

        JButton extractButton = new JButton("Extract Tags");

        inputPanel.add(listingNameLabel);
        inputPanel.add(listingNameField);
        inputPanel.add(imageUrlLabel);
        inputPanel.add(imageUrlField);
        inputPanel.add(new JLabel());
        inputPanel.add(extractButton);

        // Center panel: Results - FIXED FONT
        JTextArea resultsArea = new JTextArea();
        resultsArea.setEditable(false);
        resultsArea.setFont(new Font("Dialog", Font.PLAIN, 12));  // Changed font
        JScrollPane scrollPane = new JScrollPane(resultsArea);

        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Button action
        extractButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String listingName = listingNameField.getText().trim();
                String imageUrl = imageUrlField.getText().trim();

                if (listingName.isEmpty() || imageUrl.isEmpty()) {
                    JOptionPane.showMessageDialog(frame,
                            "Please enter both listing name and image URL",
                            "Missing Information",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                resultsArea.setText("Extracting tags from Imagga API...\nThis may take a few seconds...\n\n");
                extractButton.setEnabled(false);

                // Run in background thread
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // Create listing
                            Listing testListing = new Listing(
                                    listingName,
                                    testUser,
                                    null,
                                    new ArrayList<>(),
                                    new ArrayList<>(),
                                    "Test description",
                                    150.0,
                                    "Test address",
                                    5.0,
                                    1000.0,
                                    2,
                                    1,
                                    Listing.BuildingType.APARTMENT,
                                    true
                            );
                            dataAccess.addListing(testListing);

                            // Extract tags
                            ExtractTagsViewModel viewModel = new ExtractTagsViewModel();
                            ExtractTagsController controller =
                                    ExtractTagsUseCaseFactory.createExtractTagsUseCase(viewModel, dataAccess);

                            ExtractTagsOutputData result = controller.execute(listingName, imageUrl);

                            // Update UI on EDT - FIXED FORMATTING
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    if (result.isSuccess()) {
                                        StringBuilder sb = new StringBuilder();
                                        sb.append("===========================================\n");
                                        sb.append("         SUCCESS - TAGS EXTRACTED          \n");
                                        sb.append("===========================================\n\n");

                                        sb.append("EXTRACTED TAGS (" + result.getExtractedTags().size() + "):\n");
                                        sb.append("-------------------------------------------\n");
                                        for (int i = 0; i < result.getExtractedTags().size(); i++) {
                                            sb.append(String.format("%2d. %s\n", i+1, result.getExtractedTags().get(i)));
                                        }

                                        sb.append("\n");
                                        sb.append("ASSIGNED CATEGORIES:\n");
                                        sb.append("-------------------------------------------\n");
                                        if (result.getCategories().isEmpty()) {
                                            sb.append("  (No categories assigned)\n");
                                        } else {
                                            for (String category : result.getCategories()) {
                                                sb.append("  * " + category + "\n");
                                            }
                                        }
                                        sb.append("\n===========================================\n");

                                        resultsArea.setText(sb.toString());
                                    } else {
                                        resultsArea.setText("===========================================\n" +
                                                "              FAILED                    \n" +
                                                "===========================================\n\n" +
                                                "Tag extraction was not successful.\n" +
                                                "Check console for error details.");
                                    }
                                    extractButton.setEnabled(true);
                                }
                            });

                        } catch (Exception ex) {
                            ex.printStackTrace();
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    resultsArea.setText("===========================================\n" +
                                            "              ERROR                     \n" +
                                            "===========================================\n\n" +
                                            ex.getMessage() + "\n\n" +
                                            "Check console for full stack trace.");
                                    extractButton.setEnabled(true);
                                }
                            });
                        }
                    }
                }).start();
            }
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        System.out.println("========================================");
        System.out.println("TAG EXTRACTION UI TEST");
        System.out.println("========================================");
        System.out.println("\nTry these image URLs:");
        System.out.println("  - House: https://images.unsplash.com/photo-1564013799919-ab600027ffc6");
        System.out.println("  - Apartment: https://images.unsplash.com/photo-1522708323590-d24dbb6b0267");
        System.out.println("  - Villa: https://images.unsplash.com/photo-1613490493576-7fde63acd811");
        System.out.println("\nNote: Make sure config.properties has your Imagga API credentials!");
    }
}