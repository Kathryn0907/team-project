import java.awt.Window;
import app.MessagingUseCaseFactory;
import data_access.MongoDBMessageDAO;
import interface_adapter.ViewManagerModel;
import interface_adapter.messaging.*;
import view.MessagingView;
import websocket.*;

import javax.swing.*;
import java.net.URI;

public class MessagingSystemTest {

    private static final int SERVER_PORT = 8887;
    private static ChatServer server;
    private static MongoDBMessageDAO messageDAO;

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("MESSAGING SYSTEM TEST (WebSocket + MongoDB)");
        System.out.println("========================================\n");

        try {
            messageDAO = new MongoDBMessageDAO();
            System.out.println("✅ MongoDB connected\n");
        } catch (Exception e) {
            System.err.println("❌ MongoDB connection failed: " + e.getMessage());
            System.err.println("Test will continue with limited functionality\n");
        }

        MessageHandler messageHandler = new MessageHandler() {
            @Override
            public Entities.Message handleMessage(String from, String to, String listingId, String content) {
                MessageViewModel viewModel = new MessageViewModel();
                MessageController controller = MessagingUseCaseFactory.createMessagingUseCase(
                        viewModel, messageDAO
                );

                use_case.messaging.SendMessageOutputData result =
                        controller.sendMessage(from, to, listingId, content);

                return result.getMessage();
            }
        };

        server = new ChatServer(SERVER_PORT, messageHandler);

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

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        createUserWindow("alice", "bob", messageDAO);
        createUserWindow("bob", "alice", messageDAO);

        System.out.println("\n========================================");
        System.out.println("TWO CHAT WINDOWS OPENED");
        System.out.println("========================================");
        System.out.println("\nTry sending messages between alice and bob!");
        System.out.println("Messages should appear instantly in both windows.");
        System.out.println("Messages are saved to MongoDB and will persist after restart.");
        System.out.println("\n⚠️  Close ALL windows to stop the server properly.\n");
    }

    private static void createUserWindow(String username, String recipient, MongoDBMessageDAO messageDAO) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    URI serverUri = new URI("ws://localhost:" + SERVER_PORT);
                    ChatClient chatClient = new ChatClient(serverUri, username);
                    chatClient.connect();

                    Thread.sleep(500);

                    MessageViewModel viewModel = new MessageViewModel();
                    MessageController controller = MessagingUseCaseFactory.createMessagingUseCase(
                            viewModel, messageDAO
                    );
                    ViewManagerModel viewManagerModel = new ViewManagerModel();

                    MessagingView messagingView = new MessagingView(
                            username,
                            viewModel,
                            controller,
                            chatClient,
                            recipient,
                            viewManagerModel,
                            messageDAO
                    );

                    JFrame frame = new JFrame("Chat - " + username);

                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                    frame.setSize(600, 500);
                    frame.setLocationRelativeTo(null);
                    frame.add(messagingView);
                    frame.setVisible(true);

                    frame.addWindowListener(new java.awt.event.WindowAdapter() {
                        @Override
                        public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                            chatClient.close();
                            System.out.println("👋 " + username + " disconnected");

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

                    System.out.println("✅ Created chat window for: " + username + " (chatting with " + recipient + ")");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}