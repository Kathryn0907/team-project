package view;

import interface_adapter.comment.CommentController;
import interface_adapter.comment.CommentViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import Entities.Listing;
import Entities.User;

public class CommentView extends JPanel implements PropertyChangeListener {

    private final CommentController controller;
    private final CommentViewModel viewModel;

    private final JTextArea commentBox = new JTextArea(3, 20);
    private final JButton submitButton = new JButton("Add Comment");
    private final JLabel messageLabel = new JLabel("");

    private Listing currentListing;
    private User currentUser;

    public CommentView(CommentController controller, CommentViewModel viewModel) {
        this.controller = controller;
        this.viewModel = viewModel;

        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(new JScrollPane(commentBox), BorderLayout.CENTER);
        inputPanel.add(submitButton, BorderLayout.EAST);

        add(inputPanel, BorderLayout.NORTH);
        add(messageLabel, BorderLayout.SOUTH);

        submitButton.addActionListener(e -> {
            String content = commentBox.getText().trim();
            if (content.isEmpty()) {
                messageLabel.setText("Comment cannot be empty.");
                return;
            }

            if(currentListing == null || currentUser == null) {
                messageLabel.setText("No listing or user set.");
                return;
            }

            controller.addComment(
                    java.util.UUID.randomUUID().toString(),
                    currentListing,
                    currentUser,
                    content
            );
        });
    }

    public void setListing(Listing listing) {
        this.currentListing = listing;
    }

    public void setUser(User user) {
        this.currentUser = user;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (viewModel.getState().getErrorMessage() != null) {
            messageLabel.setText(viewModel.getState().getErrorMessage());
        } else if (viewModel.getState().getSuccessMessage() != null) {
            messageLabel.setText(viewModel.getState().getSuccessMessage());
            commentBox.setText("");
        }
    }
}
