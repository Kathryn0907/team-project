package view;

import Entities.Message;
import interface_adapter.messaging.*;
import interface_adapter.ViewManagerModel;
import websocket.ChatClient;
import data_access.MongoDBMessageDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Messaging UI with WebSocket real-time updates and back navigation
 * Fixed: Marks messages as read and displays timestamps
 */
public class MessagingView extends JPanel implements PropertyChangeListener {

    private final String currentUsername;
    private final String recipientUsername;
    private final MessageViewModel viewModel;
    private final MessageController controller;
    private final ChatClient chatClient;
    private final ViewManagerModel viewManagerModel;
    private final MongoDBMessageDAO messageDAO;

    private JTextArea conversationArea;
    private JTextField messageField;
    private JButton sendButton;
    private JButton backButton;

    private static final DateTimeFormatter TIME_FORMATTER =
            DateTimeFormatter.ofPattern("HH:mm").withZone(ZoneId.systemDefault());

    public MessagingView(String currentUsername,
                         MessageViewModel viewModel,
                         MessageController controller,
                         ChatClient chatClient,
                         String recipientUsername,
                         ViewManagerModel viewManagerModel,
                         MongoDBMessageDAO messageDAO) {
        this.currentUsername = currentUsername;
        this.recipientUsername = recipientUsername;
        this.viewModel = viewModel;
        this.controller = controller;
        this.chatClient = chatClient;
        this.viewManagerModel = viewManagerModel;
        this.messageDAO = messageDAO;

        if (this.viewModel != null) {
            this.viewModel.addPropertyChangeListener(this);
        }

        setupUI();
        setupChatClient();
        loadConversationHistory();
        markMessagesAsRead();  // Mark messages as read when opening conversation
    }

    private void setupUI() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top panel: Back button and recipient name
        JPanel topPanel = new JPanel(new BorderLayout());

        backButton = new JButton("Back to Conversations");
        JLabel recipientLabel = new JLabel("Conversation with: " + recipientUsername);
        recipientLabel.setFont(new Font("Arial", Font.BOLD, 16));

        topPanel.add(backButton, BorderLayout.WEST);
        topPanel.add(recipientLabel, BorderLayout.CENTER);

        // Center: Conversation area
        conversationArea = new JTextArea();
        conversationArea.setEditable(false);
        conversationArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        conversationArea.setLineWrap(true);
        conversationArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(conversationArea);

        // Bottom: Message input
        JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));
        messageField = new JTextField();
        sendButton = new JButton("Send");

        bottomPanel.add(messageField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);

        this.add(topPanel, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(bottomPanel, BorderLayout.SOUTH);

        // Back button action
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (viewManagerModel != null) {
                    viewManagerModel.setState("conversations");
                    viewManagerModel.firePropertyChange();
                }
            }
        });

        // Send button action
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        // Enter key sends message
        messageField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
    }

    private void setupChatClient() {
        if (chatClient != null) {
            chatClient.setMessageCallback(new ChatClient.MessageCallback() {
                @Override
                public void onMessageReceived(Message message) {
                    // Only display if it's part of this conversation
                    if ((message.getFromUsername().equals(currentUsername) &&
                            message.getToUsername().equals(recipientUsername)) ||
                            (message.getFromUsername().equals(recipientUsername) &&
                                    message.getToUsername().equals(currentUsername))) {

                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                displayMessage(message);
                                // Mark as read if it's from the recipient
                                if (message.getFromUsername().equals(recipientUsername) &&
                                        messageDAO != null) {
                                    messageDAO.markAsRead(message.getId());
                                }
                            }
                        });
                    }
                }
            });
        }
    }

    private void loadConversationHistory() {
        conversationArea.append("=== Conversation History ===\n\n");

        if (messageDAO != null) {
            ArrayList<Message> conversation = messageDAO.getConversation(currentUsername, recipientUsername);

            for (Message msg : conversation) {
                displayMessage(msg);
            }
        }
    }

    /**
     * Mark all unread messages from recipient as read when opening conversation
     */
    private void markMessagesAsRead() {
        if (messageDAO != null) {
            ArrayList<Message> conversation = messageDAO.getConversation(currentUsername, recipientUsername);

            for (Message msg : conversation) {
                // Mark as read if it's from the recipient to current user and not yet read
                if (msg.getFromUsername().equals(recipientUsername) &&
                        msg.getToUsername().equals(currentUsername) &&
                        !msg.isRead()) {
                    messageDAO.markAsRead(msg.getId());
                }
            }
        }
    }

    private void sendMessage() {
        String content = messageField.getText().trim();

        if (content.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a message",
                    "Empty Message",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Send via WebSocket
        if (chatClient != null) {
            chatClient.sendMessage(currentUsername, recipientUsername, null, content);
        }

        // Display in conversation immediately (for the sender)
        conversationArea.append("[" + TIME_FORMATTER.format(java.time.Instant.now()) + "] You: " + content + "\n");

        // Auto-scroll to bottom
        conversationArea.setCaretPosition(conversationArea.getDocument().getLength());

        // Clear input
        messageField.setText("");
    }

    private void displayMessage(Message message) {
        String fromDisplay = message.getFromUsername().equals(currentUsername) ?
                "You" : message.getFromUsername();

        // Format timestamp as HH:mm
        String timestamp = TIME_FORMATTER.format(message.getTimestamp());

        String display = String.format("[%s] %s: %s\n", timestamp, fromDisplay, message.getContent());
        conversationArea.append(display);

        // Auto-scroll to bottom
        conversationArea.setCaretPosition(conversationArea.getDocument().getLength());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (viewModel != null) {
            MessageState state = viewModel.getState();

            if (!state.isSuccess()) {
                JOptionPane.showMessageDialog(this,
                        state.getErrorMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}