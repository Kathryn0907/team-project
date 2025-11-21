package view;

import Entities.Comment;
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

    // === New: comment list UI ===
    private final JPanel commentsPanel = new JPanel();
    private final JScrollPane commentsScrollPane =
            new JScrollPane(commentsPanel,
                    JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);


    public CommentView(CommentController controller, CommentViewModel viewModel) {
        this.controller = controller;
        this.viewModel = viewModel;

        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());

        // --- New: comments panel setup ---
        commentsPanel.setLayout(new BoxLayout(commentsPanel, BoxLayout.Y_AXIS));
        commentsScrollPane.setPreferredSize(new Dimension(100, 250));
        add(commentsScrollPane, BorderLayout.CENTER);

        // input panel
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

        // === New: refresh comments list ===
        java.util.List<Comment> comments = viewModel.getState().getComments();

        commentsPanel.removeAll();

        if (comments == null || comments.isEmpty()) {
            commentsPanel.add(new JLabel("No comments yet."));
        } else {
            for (Comment c : comments) {
                String author = (c.getUser() != null) ? c.getUser().getUsername() : "Anonymous";
                String text = "• " + author + ": " + c.getContent();

                JLabel label = new JLabel(text);
                label.setAlignmentX(Component.LEFT_ALIGNMENT);
                commentsPanel.add(label);
                commentsPanel.add(Box.createVerticalStrut(5));
            }
        }
        commentsPanel.revalidate();
        commentsPanel.repaint();
    }
}
