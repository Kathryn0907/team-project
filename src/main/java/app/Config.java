package app;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static Properties properties = new Properties();

    static {
        try {
            InputStream input = Config.class.getClassLoader()
                    .getResourceAsStream("config.properties");

            if (input == null) {
                System.err.println("⚠️  Unable to find config.properties");
            } else {
                properties.load(input);
            }
        } catch (IOException e) {
            System.err.println("❌ Error loading configuration: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static String getImaggaApiKey() {
        return properties.getProperty("imagga.api.key");
    }

    public static String getImaggaApiSecret() {
        return properties.getProperty("imagga.api.secret");
    }

    public static int getWebSocketPort() {
        return Integer.parseInt(properties.getProperty("websocket.server.port", "8887"));
    }
}