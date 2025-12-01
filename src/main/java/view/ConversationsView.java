package view;

import Entities.Message;
import data_access.MongoDBMessageDAO;
import data_access.MongoDBUserDAO;
import interface_adapter.ViewManagerModel;
import interface_adapter.messaging.MessageController;
import websocket.ChatClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * View showing all conversations for the current user
 * Fixed: Properly passes MessageDAO to MessagingView
 */
public class ConversationsView extends JPanel {

    private final String viewName = "conversations";
    private final String currentUsername;
    private final ViewManagerModel viewManagerModel;
    private final MongoDBMessageDAO messageDAO;
    private final MongoDBUserDAO userDAO;
    private final MessageController messageController;
    private final ChatClient chatClient;

    private final JPanel conversationsPanel;
    private final JButton newConversationButton;
    private final JButton backButton;

    public ConversationsView(String currentUsername,
                             ViewManagerModel viewManagerModel,
                             MongoDBMessageDAO messageDAO,
                             MongoDBUserDAO userDAO,
                             MessageController messageController,
                             ChatClient chatClient) {
        this.currentUsername = currentUsername;
        this.viewManagerModel = viewManagerModel;
        this.messageDAO = messageDAO;
        this.userDAO = userDAO;
        this.messageController = messageController;
        this.chatClient = chatClient;

        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top panel with title and buttons
        JPanel topPanel = new JPanel(new BorderLayout());

        JLabel titleLabel = new JLabel("My Conversations");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        newConversationButton = new JButton("New Conversation");
        backButton = new JButton("Back");
        buttonPanel.add(newConversationButton);
        buttonPanel.add(backButton);

        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);

        // Center panel with conversations list
        conversationsPanel = new JPanel();
        conversationsPanel.setLayout(new BoxLayout(conversationsPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(conversationsPanel);

        this.add(topPanel, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);

        // Button actions
        newConversationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showNewConversationDialog();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewManagerModel.setState("logged in");
                viewManagerModel.firePropertyChange();
            }
        });

        // Load conversations
        loadConversations();
    }

    private void loadConversations() {
        conversationsPanel.removeAll();

        ArrayList<String> partners = messageDAO.getConversationPartners(currentUsername);

        if (partners.isEmpty()) {
            JLabel emptyLabel = new JLabel("No conversations yet. Start a new one!");
            emptyLabel.setFont(new Font("Arial", Font.ITALIC, 14));
            emptyLabel.setForeground(Color.GRAY);
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            conversationsPanel.add(Box.createVerticalStrut(50));
            conversationsPanel.add(emptyLabel);
        } else {
            for (String partner : partners) {
                JPanel conversationCard = createConversationCard(partner);
                conversationsPanel.add(conversationCard);
                conversationsPanel.add(Box.createVerticalStrut(10));
            }
        }

        conversationsPanel.revalidate();
        conversationsPanel.repaint();
    }

    private JPanel createConversationCard(String partner) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Left side: Partner info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        JLabel nameLabel = new JLabel("Chat with " + partner);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));

        // Get last message
        Message lastMessage = messageDAO.getLastMessage(currentUsername, partner);
        String lastMessageText = lastMessage != null ?
                truncate(lastMessage.getContent(), 50) : "No messages yet";
        JLabel lastMsgLabel = new JLabel(lastMessageText);
        lastMsgLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        lastMsgLabel.setForeground(Color.GRAY);

        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(lastMsgLabel);

        // Right side: Unread count (if any)
        ArrayList<Message> unread = getUnreadFromUser(partner);
        JLabel unreadLabel = null;
        if (!unread.isEmpty()) {
            unreadLabel = new JLabel(unread.size() + " new");
            unreadLabel.setFont(new Font("Arial", Font.BOLD, 12));
            unreadLabel.setForeground(Color.WHITE);
            unreadLabel.setOpaque(true);
            unreadLabel.setBackground(new Color(0, 123, 255));
            unreadLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        }

        card.add(infoPanel, BorderLayout.CENTER);
        if (unreadLabel != null) {
            card.add(unreadLabel, BorderLayout.EAST);
        }

        // Click to open conversation
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                openConversation(partner);
            }

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                card.setBackground(new Color(240, 240, 240));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                card.setBackground(Color.WHITE);
            }
        });

        return card;
    }

    private void showNewConversationDialog() {
        String recipient = JOptionPane.showInputDialog(
                this,
                "Enter username to start a conversation:",
                "New Conversation",
                JOptionPane.PLAIN_MESSAGE
        );

        if (recipient != null && !recipient.trim().isEmpty()) {
            recipient = recipient.trim();

            // Check if user exists in MongoDB
            if (userDAO.findUserByUsername(recipient) == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "User '" + recipient + "' does not exist.",
                        "User Not Found",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            if (recipient.equals(currentUsername)) {
                JOptionPane.showMessageDialog(
                        this,
                        "You cannot start a conversation with yourself.",
                        "Invalid User",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // Open conversation with this user
            openConversation(recipient);
        }
    }

    private void openConversation(String partner) {
        // Create MessagingView and pass messageDAO
        MessagingView messagingView = new MessagingView(
                currentUsername,
                null,
                messageController,
                chatClient,
                partner,
                viewManagerModel,
                messageDAO  // Pass the DAO so messages can be marked as read
        );

        // Add to parent container
        Container parent = this.getParent();
        if (parent instanceof JPanel) {
            JPanel cardPanel = (JPanel) parent;
            cardPanel.add(messagingView, "messaging_" + partner);

            CardLayout layout = (CardLayout) cardPanel.getLayout();
            layout.show(cardPanel, "messaging_" + partner);
        }
    }

    private ArrayList<Message> getUnreadFromUser(String fromUser) {
        ArrayList<Message> allUnread = messageDAO.getUnreadMessages(currentUsername);
        ArrayList<Message> unreadFromUser = new ArrayList<>();

        for (Message msg : allUnread) {
            if (msg.getFromUsername().equals(fromUser)) {
                unreadFromUser.add(msg);
            }
        }

        return unreadFromUser;
    }

    private String truncate(String text, int maxLength) {
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength) + "...";
    }

    public String getViewName() {
        return viewName;
    }

    public void refreshConversations() {
        loadConversations();
    }
}