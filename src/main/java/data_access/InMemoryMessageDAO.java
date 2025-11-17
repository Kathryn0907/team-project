package data_access;

import Entities.Message;
import use_case.messaging.MessageDataAccessInterface;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * TEMPORARY PLACEHOLDER - In-memory message storage
 * Your friend will replace this with MongoDBMessageDAO
 */
public class InMemoryMessageDAO implements MessageDataAccessInterface {
    private final ArrayList<Message> messages;

    public InMemoryMessageDAO() {
        this.messages = new ArrayList<>();
    }

    @Override
    public void saveMessage(Message message) {
        messages.add(message);
        System.out.println("💾 [InMemory] Saved message: " + message);
    }

    @Override
    public ArrayList<Message> getMessagesForUser(String username) {
        ArrayList<Message> userMessages = new ArrayList<>();
        for (Message msg : messages) {
            if (msg.getFromUsername().equals(username) ||
                    msg.getToUsername().equals(username)) {
                userMessages.add(msg);
            }
        }
        return userMessages;
    }

    @Override
    public ArrayList<Message> getConversation(String user1, String user2) {
        ArrayList<Message> conversation = new ArrayList<>();
        for (Message msg : messages) {
            boolean isConversation =
                    (msg.getFromUsername().equals(user1) && msg.getToUsername().equals(user2)) ||
                            (msg.getFromUsername().equals(user2) && msg.getToUsername().equals(user1));

            if (isConversation) {
                conversation.add(msg);
            }
        }

        // Sort by timestamp
        conversation.sort((a, b) -> a.getTimestamp().compareTo(b.getTimestamp()));
        return conversation;
    }

    @Override
    public ArrayList<Message> getUnreadMessages(String username) {
        ArrayList<Message> unread = new ArrayList<>();
        for (Message msg : messages) {
            if (msg.getToUsername().equals(username) && !msg.isRead()) {
                unread.add(msg);
            }
        }
        return unread;
    }

    @Override
    public void markAsRead(String messageId) {
        for (Message msg : messages) {
            if (msg.getId().equals(messageId)) {
                msg.setRead(true);
                break;
            }
        }
    }
}