package view;

import Entities.Message;
import interface_adapter.messaging.*;
import websocket.ChatClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

/**
 * Messaging UI with WebSocket real-time updates
 */
public class MessagingView extends JPanel implements PropertyChangeListener {

    private final String currentUsername;
    private final MessageViewModel viewModel;
    private final MessageController controller;
    private final ChatClient chatClient;

    private JTextField recipientField;
    private JTextArea conversationArea;
    private JTextField messageField;
    private JButton sendButton;

    public MessagingView(String currentUsername, MessageViewModel viewModel,
                         MessageController controller, ChatClient chatClient) {
        this.currentUsername = currentUsername;
        this.viewModel = viewModel;
        this.controller = controller;
        this.chatClient = chatClient;

        this.viewModel.addPropertyChangeListener(this);

        setupUI();
        setupChatClient();
    }

    private void setupUI() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top panel: Recipient selection
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Messaging as: " + currentUsername));
        topPanel.add(Box.createHorizontalStrut(20));
        topPanel.add(new JLabel("To:"));
        recipientField = new JTextField(15);
        topPanel.add(recipientField);

        // Center: Conversation area
        conversationArea = new JTextArea();
        conversationArea.setEditable(false);
        conversationArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
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
        // Set callback for incoming messages
        chatClient.setMessageCallback(new ChatClient.MessageCallback() {
            @Override
            public void onMessageReceived(Message message) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        displayMessage(message);
                    }
                });
            }
        });
    }

    private void sendMessage() {
        String recipient = recipientField.getText().trim();
        String content = messageField.getText().trim();

        if (recipient.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a recipient username",
                    "No Recipient",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (content.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a message",
                    "Empty Message",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Send via WebSocket
        chatClient.sendMessage(currentUsername, recipient, null, content);

        // Display in conversation
        conversationArea.append("[You -> " + recipient + "] " + content + "\n");

        // Clear input
        messageField.setText("");
    }

    private void displayMessage(Message message) {
        String fromDisplay = message.getFromUsername().equals(currentUsername) ? "You" : message.getFromUsername();
        String toDisplay = message.getToUsername().equals(currentUsername) ? "You" : message.getToUsername();

        String display = String.format("[%s -> %s] %s\n", fromDisplay, toDisplay, message.getContent());
        conversationArea.append(display);

        // Auto-scroll to bottom
        conversationArea.setCaretPosition(conversationArea.getDocument().getLength());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        MessageState state = viewModel.getState();

        if (!state.isSuccess()) {
            JOptionPane.showMessageDialog(this,
                    state.getErrorMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}