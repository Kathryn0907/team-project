package data_access;

import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DistanceCalculatorTest {

    // Load API key from environment variable
    private static final String API_KEY = System.getenv("GOOGLE_API_KEY");
    private static GeoApiContext context;
    private static GoogleDistanceService distanceService;

    @BeforeAll
    static void setUp() {
        // Initialize the context once before all tests run
        if (API_KEY == null) {
            System.err.println("⚠️  API Key not set. Skipping tests.");
            System.err.println("Set GOOGLE_API_KEY environment variable before running tests.");
            return;
        }

        distanceService = new GoogleDistanceService();
        distanceService.setApiKey(API_KEY);

        context = new GeoApiContext.Builder()
                .apiKey(API_KEY)
                .build();

        System.out.println("✓ Test setup complete");
    }

    @AfterAll
    static void tearDown() {
        // Shut down the context after all tests complete
        if (context != null) {
            context.shutdown();
            System.out.println("✓ Context shut down");
        }
    }

    @Test
    void testSingleDistanceCalculation() throws ApiException, InterruptedException, IOException {
        if (context == null) {
            System.out.println("⚠️  Skipping test - API key not set");
            return;
        }

        String origin = "CN Tower, Toronto";
        String destination = "Bahen Centre for Information Technology, Toronto";

        // Call with context parameter
        long distanceMeters = distanceService.getDistanceForSingleDestination(context, origin, destination);

        // Assert that the distance is a positive number (in meters)
        assertTrue(distanceMeters > 0, "Distance should be positive");

        // Assert that the distance is roughly within an expected range
        // CN Tower to Bahen Centre is approximately 2-3 km
        long expectedDistanceInMeters = 2500; // ~2.5 km
        long tolerance = 1000; // allow a 1km variance
        assertTrue(distanceMeters >= expectedDistanceInMeters - tolerance &&
                        distanceMeters <= expectedDistanceInMeters + tolerance,
                "Distance should be around 2.5 km. Got: " + distanceMeters + " meters");

        System.out.println("✓ Distance: " + distanceMeters + " meters (~" + (distanceMeters/1000.0) + " km)");
    }

    @Test
    void testMultiDestinationDistanceCalculation() throws ApiException, InterruptedException, IOException {
        if (context == null) {
            System.out.println("⚠️  Skipping test - API key not set");
            return;
        }

        String origin = "CN Tower, Toronto";
        String[] destinations = {
                "Union Station, Toronto",           // Closer (~1 km)
                "Toronto Pearson Airport, Toronto"  // Further (~25 km)
        };

        // Call with context parameter
        List<Long> distances = distanceService.getDistancesToDestinations(context, origin, destinations);

        assertNotNull(distances, "Distance list should not be null");
        assertEquals(2, distances.size(), "Should return exactly two distances");

        Long distanceToUnion = distances.get(0);
        Long distanceToPearson = distances.get(1);

        assertNotNull(distanceToUnion, "Distance to Union Station should not be null");
        assertNotNull(distanceToPearson, "Distance to Pearson should not be null");

        assertTrue(distanceToUnion > 0, "Distance to Union Station should be positive");
        assertTrue(distanceToPearson > 0, "Distance to Pearson should be positive");

        // The distance to Pearson should be significantly greater than to Union Station
        assertTrue(distanceToPearson > distanceToUnion,
                "Pearson Airport should be further than Union Station");

        System.out.println("✓ Distance to Union Station: " + distanceToUnion + " meters (~" + (distanceToUnion/1000.0) + " km)");
        System.out.println("✓ Distance to Pearson Airport: " + distanceToPearson + " meters (~" + (distanceToPearson/1000.0) + " km)");

        // Check approximate distance to Union Station (~1km)
        long expectedUnionDistance = 1000;
        long tolerance = 500;
        assertTrue(distanceToUnion >= expectedUnionDistance - tolerance &&
                        distanceToUnion <= expectedUnionDistance + tolerance,
                "Distance to Union Station should be around 1 km");

        // Check approximate distance to Pearson (~25km)
        long expectedPearsonDistance = 25000;
        long tolerance2 = 5000;
        assertTrue(distanceToPearson >= expectedPearsonDistance - tolerance2 &&
                        distanceToPearson <= expectedPearsonDistance + tolerance2,
                "Distance to Pearson should be around 25 km");
    }

    @Test
    void testInvalidDestinationHandling() throws ApiException, InterruptedException, IOException {
        if (context == null) {
            System.out.println("⚠️  Skipping test - API key not set");
            return;
        }

        // Use a non-existent place to force an error status
        String origin = "Toronto, ON";
        String destination = "Imaginary NonExistent Place ABC XYZ 123456789";

        // The method handles the error internally and returns -1
        long distance = distanceService.getDistanceForSingleDestination(context, origin, destination);

        assertEquals(-1, distance, "Should return -1 for an invalid location");

        System.out.println("✓ Invalid destination correctly returned -1");
    }

    @Test
    void testCalculateDistanceKm() {
        if (API_KEY == null) {
            System.out.println("⚠️  Skipping test - API key not set");
            return;
        }

        String origin = "CN Tower, Toronto";
        String destination = "Bahen Centre, Toronto";

        // This method creates its own context internally
        double distanceKm = distanceService.calculateDistanceKm(origin, destination);

        assertTrue(distanceKm > 0, "Distance in km should be positive");

        // Should be around 2-3 km
        assertTrue(distanceKm >= 1.5 && distanceKm <= 3.5,
                "Distance should be between 1.5 and 3.5 km. Got: " + distanceKm + " km");

        System.out.println("✓ Distance (using calculateDistanceKm): " + distanceKm + " km");
    }
}