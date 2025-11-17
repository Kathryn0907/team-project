package websocket;

import Entities.Message;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;

import java.net.URI;
import java.time.Instant;

/**
 * WebSocket client for connecting to chat server
 */
public class ChatClient extends WebSocketClient {

    private final String username;
    private MessageCallback messageCallback;

    public ChatClient(URI serverUri, String username) {
        super(serverUri);
        this.username = username;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("✅ Connected to chat server");

        // Send login message
        JSONObject login = new JSONObject();
        login.put("type", "login");
        login.put("username", username);
        send(login.toString());
    }

    @Override
    public void onMessage(String message) {
        try {
            JSONObject json = new JSONObject(message);
            String type = json.getString("type");

            if ("login_success".equals(type)) {
                System.out.println("🔐 Logged in as: " + username);

            } else if ("new_message".equals(type)) {
                // Received a message
                String id = json.getString("id");
                String from = json.getString("from");
                String to = json.getString("to");
                String content = json.getString("content");
                String listingId = json.optString("listingId", null);
                String timestamp = json.getString("timestamp");

                Message msg = new Message(id, from, to, listingId, content);
                msg.setTimestamp(Instant.parse(timestamp));

                if (messageCallback != null) {
                    messageCallback.onMessageReceived(msg);
                }

            } else if ("message_sent".equals(type)) {
                System.out.println("✉️ Message sent successfully");
            }

        } catch (Exception e) {
            System.err.println("Error processing message: " + e.getMessage());
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("❌ Disconnected from server: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        System.err.println("WebSocket error: " + ex.getMessage());
    }

    /**
     * Send a message to another user
     */
    public void sendMessage(String from, String to, String listingId, String content) {
        JSONObject json = new JSONObject();
        json.put("type", "message");
        json.put("from", from);
        json.put("to", to);
        json.put("content", content);
        if (listingId != null) {
            json.put("listingId", listingId);
        }

        send(json.toString());
    }

    public void setMessageCallback(MessageCallback callback) {
        this.messageCallback = callback;
    }

    /**
     * Callback interface for received messages
     */
    public interface MessageCallback {
        void onMessageReceived(Message message);
    }
}