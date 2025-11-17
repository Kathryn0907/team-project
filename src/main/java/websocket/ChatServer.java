package websocket;

import Entities.Message;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONObject;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * WebSocket server for real-time messaging
 */
public class ChatServer extends WebSocketServer {

    // Map username -> WebSocket connection
    private final Map<String, WebSocket> activeUsers;
    private final MessageHandler messageHandler;

    public ChatServer(int port, MessageHandler messageHandler) {
        super(new InetSocketAddress(port));
        this.activeUsers = new HashMap<>();
        this.messageHandler = messageHandler;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("🔗 New connection from: " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        // Remove user from active users
        String disconnectedUser = null;
        for (Map.Entry<String, WebSocket> entry : activeUsers.entrySet()) {
            if (entry.getValue().equals(conn)) {
                disconnectedUser = entry.getKey();
                break;
            }
        }

        if (disconnectedUser != null) {
            activeUsers.remove(disconnectedUser);
            System.out.println("❌ User disconnected: " + disconnectedUser);
        }
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        try {
            JSONObject json = new JSONObject(message);
            String type = json.getString("type");

            if ("login".equals(type)) {
                // User logging in
                String username = json.getString("username");
                activeUsers.put(username, conn);
                System.out.println("✅ User logged in: " + username);

                // Send confirmation
                JSONObject response = new JSONObject();
                response.put("type", "login_success");
                response.put("username", username);
                conn.send(response.toString());

            } else if ("message".equals(type)) {
                // User sending message
                String from = json.getString("from");
                String to = json.getString("to");
                String content = json.getString("content");
                String listingId = json.optString("listingId", null);

                // Save message through handler
                Message msg = messageHandler.handleMessage(from, to, listingId, content);

                // Send to recipient if online
                WebSocket recipient = activeUsers.get(to);
                if (recipient != null && recipient.isOpen()) {
                    JSONObject messageJson = new JSONObject();
                    messageJson.put("type", "new_message");
                    messageJson.put("id", msg.getId());
                    messageJson.put("from", msg.getFromUsername());
                    messageJson.put("to", msg.getToUsername());
                    messageJson.put("content", msg.getContent());
                    messageJson.put("listingId", msg.getListingId());
                    messageJson.put("timestamp", msg.getTimestamp().toString());

                    recipient.send(messageJson.toString());
                    System.out.println("📨 Message sent to " + to + " (online)");
                } else {
                    System.out.println("📭 Message saved for " + to + " (offline)");
                }

                // Confirm to sender
                JSONObject confirmation = new JSONObject();
                confirmation.put("type", "message_sent");
                confirmation.put("messageId", msg.getId());
                conn.send(confirmation.toString());
            }

        } catch (Exception e) {
            System.err.println("Error processing message: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.err.println("WebSocket error: " + ex.getMessage());
        ex.printStackTrace();
    }

    @Override
    public void onStart() {
        System.out.println("🚀 Chat server started on port " + getPort());
    }

    /**
     * Send a message to a specific user
     */
    public void sendToUser(String username, String message) {
        WebSocket conn = activeUsers.get(username);
        if (conn != null && conn.isOpen()) {
            conn.send(message);
        }
    }

    /**
     * Check if user is online
     */
    public boolean isUserOnline(String username) {
        WebSocket conn = activeUsers.get(username);
        return conn != null && conn.isOpen();
    }
}