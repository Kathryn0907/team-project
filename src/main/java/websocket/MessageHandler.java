package websocket;

import Entities.Message;

/**
 * Interface for handling messages in WebSocket server
 */
public interface MessageHandler {
    Message handleMessage(String from, String to, String listingId, String content);
}