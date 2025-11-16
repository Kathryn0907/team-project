
import java.awt.Window;
import app.MessagingUseCaseFactory;
import data_access.InMemoryMessageDAO;
import interface_adapter.messaging.*;
import view.MessagingView;
import websocket.*;

import javax.swing.*;
import java.net.URI;

public class MessagingSystemTest {

    private static final int SERVER_PORT = 8887;
    private static ChatServer server;  // Make it static so shutdown hook can access

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("MESSAGING SYSTEM TEST (WebSocket)");
        System.out.println("========================================\n");

        // Initialize data access
        InMemoryMessageDAO dataAccess = new InMemoryMessageDAO();

        // Start WebSocket server
        MessageHandler messageHandler = new MessageHandler() {
            @Override
            public Entities.Message handleMessage(String from, String to, String listingId, String content) {
                MessageViewModel viewModel = new MessageViewModel();
                MessageController controller = MessagingUseCaseFactory.createMessagingUseCase(
                        viewModel, dataAccess
                );

                use_case.messaging.SendMessageOutputData result =
                        controller.sendMessage(from, to, listingId, content);

                return result.getMessage();
            }
        };

        server = new ChatServer(SERVER_PORT, messageHandler);

        // Add shutdown hook to properly close server
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("\n🛑 Shutting down server...");
                    if (server != null) {
                        server.stop(1000);
                    }
                    System.out.println("✅ Server stopped");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }));

        server.start();
        System.out.println("✅ Server started on port " + SERVER_PORT + "\n");

        // Give server time to start
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Create two user windows for testing
        createUserWindow("alice", dataAccess);
        createUserWindow("bob", dataAccess);

        System.out.println("\n========================================");
        System.out.println("TWO CHAT WINDOWS OPENED");
        System.out.println("========================================");
        System.out.println("\nTry sending messages between alice and bob!");
        System.out.println("Messages should appear instantly in both windows.");
        System.out.println("\n⚠️  Close ALL windows to stop the server properly.\n");
    }

    private static void createUserWindow(String username, use_case.messaging.MessageDataAccessInterface dataAccess) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    // Create WebSocket client
                    URI serverUri = new URI("ws://localhost:" + SERVER_PORT);
                    ChatClient chatClient = new ChatClient(serverUri, username);
                    chatClient.connect();

                    // Give client time to connect
                    Thread.sleep(500);

                    // Create view components
                    MessageViewModel viewModel = new MessageViewModel();
                    MessageController controller = MessagingUseCaseFactory.createMessagingUseCase(
                            viewModel, dataAccess
                    );

                    MessagingView messagingView = new MessagingView(
                            username, viewModel, controller, chatClient
                    );

                    // Create window
                    JFrame frame = new JFrame("Chat - " + username);

                    // CHANGED: Use DISPOSE_ON_CLOSE instead of EXIT_ON_CLOSE
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                    frame.setSize(600, 500);
                    frame.setLocationRelativeTo(null);
                    frame.add(messagingView);
                    frame.setVisible(true);

                    // Add window listener to close client connection and check if all windows closed
                    frame.addWindowListener(new java.awt.event.WindowAdapter() {
                        @Override
                        public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                            chatClient.close();
                            System.out.println("👋 " + username + " disconnected");

                            // Check if this was the last window
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    if (Window.getWindows().length == 0) {
                                        System.out.println("🛑 All windows closed. Shutting down...");
                                        System.exit(0);
                                    }
                                }
                            });
                        }
                    });

                    System.out.println("✅ Created chat window for: " + username);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}