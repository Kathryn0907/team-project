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

    // NOTE: For a real test environment, load your key from environment variables
    // or a secure configuration file, NOT hardcoded here.
    private static final String API_KEY = "AIzaSyBXp5N97WR6RPh5H4yJIq4n0tPdRf6RBQQ";
    private static GeoApiContext context;

    @BeforeAll
    static void setUp() {
        // Initialize the context once before all tests run
        if (API_KEY == null || API_KEY.equals("YOUR_API_KEY")) {
            System.err.println("API Key not set. Skipping tests.");
            return;
        }
        context = new GeoApiContext.Builder()
                .apiKey(API_KEY)
                .build();
    }

    @AfterAll
    static void tearDown() {
        // Shut down the context after all tests complete
        if (context != null) {
            context.shutdown();
        }
    }

    @Test
    void testSingleDistanceCalculation() throws ApiException, InterruptedException, IOException {
        if (context == null) return;

        String origin = "CN Tower, Toronto";
        String destination = "Bahen Centre for Information Technology, Toronto";

        GoogleDistanceService singleDistanceCalculator = new GoogleDistanceService();

        long distance = singleDistanceCalculator.getDistanceForSingleDestination(context, origin, destination);

        // Assert that the distance is a positive number (in meters)
        assertTrue(distance > 0, "Distance should be positive");

        // Assert that the distance is roughly within an expected range (e.g., Manhattan is ~8km across)
        // We allow a range for traffic/route variations.
        long expectedDistanceInMeters = 2000; // ~2 km
        long tolerance = 500; // allow a 500m variance
        assertTrue(distance >= expectedDistanceInMeters - tolerance &&
                        distance <= expectedDistanceInMeters + tolerance,
                "Distance should be around 2 km");
    }

    @Test
    void testMultiDestinationDistanceCalculation() throws ApiException, InterruptedException, IOException {
        if (context == null) return;

        String origin = "Eiffel Tower, Paris";
        String[] destinations = {
                "Louvre Museum, Paris", // Closer
                "Palace of Versailles, Versailles" // Further
        };

        GoogleDistanceService multiDestinationDistanceCalculator = new GoogleDistanceService();

        List<Long> distances = multiDestinationDistanceCalculator.getDistancesToDestinations(context, origin, destinations);

        assertNotNull(distances, "Distance list should not be null");
        assertEquals(2, distances.size(), "Should return exactly two distances");

        long distanceToLouvre = distances.get(0);
        long distanceToVersailles = distances.get(1);
        System.out.println(distanceToLouvre + " " + distanceToVersailles);

        assertTrue(distanceToLouvre > 0, "Distance to Louvre should be positive");
        assertTrue(distanceToVersailles > 0, "Distance to Versailles should be positive");

        // The distance to Versailles should be significantly greater than to the Louvre
        assertTrue(distanceToVersailles > distanceToLouvre, "Versailles should be further than the Louvre");

        // Check approximate distance to Louvre (~5km)
        long expectedLouvreDistance = 3400;
        long tolerance = 3000;
        assertTrue(distanceToLouvre >= expectedLouvreDistance - tolerance &&
                        distanceToLouvre <= expectedLouvreDistance + tolerance,
                "Distance to Louvre should be around 3.4 km");

        long expectedVersailleDistance = 20000;
        long tolerance2 = 10000;
        assertTrue(distanceToVersailles >= expectedVersailleDistance - tolerance2 &&
                        distanceToVersailles <= expectedVersailleDistance + tolerance2,
                "Distance to Louvre should be around 17 km");
    }

    @Test
    void testInvalidDestinationHandling() throws ApiException, InterruptedException, IOException {
        if (context == null) return;

        // Use a non-existent place to force an error status
        String origin = "London, UK";
        String destination = "Imaginary NonExistent Place ABC 123";

        GoogleDistanceService singleDistanceCalculator = new GoogleDistanceService();
        // The method handles the error internally and returns -1
        long distance = singleDistanceCalculator.getDistanceForSingleDestination(context, origin, destination);

        assertEquals(-1, distance, "Should return -1 for an invalid location");
    }
}
