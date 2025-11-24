import data_access.MongoDBMessageDAO;

// Create a simple test
public class TestMongoConnection {
    public static void main(String[] args) {
        try {
            MongoDBMessageDAO dao = new MongoDBMessageDAO();
            System.out.println("✅ MongoDB connected successfully!");
        } catch (Exception e) {
            System.err.println("❌ MongoDB connection failed: " + e.getMessage());
        }
    }
}