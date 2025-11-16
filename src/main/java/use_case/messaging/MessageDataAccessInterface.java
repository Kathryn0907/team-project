package use_case.messaging;

import Entities.Message;
import java.util.ArrayList;

/**
 * Interface for message data access
 * Your friend will implement this with MongoDB
 */
public interface MessageDataAccessInterface {
    void saveMessage(Message message);
    ArrayList<Message> getMessagesForUser(String username);
    ArrayList<Message> getConversation(String user1, String user2);
    ArrayList<Message> getUnreadMessages(String username);
    void markAsRead(String messageId);
}