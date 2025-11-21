package data_access;

import com.google.maps.errors.ApiException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DistanceCalculatorTest {

    @Test
    void testSingleDistanceCalculation() throws ApiException, InterruptedException, IOException {
        String origin = "CN Tower, 290 Bremner Blvd, Toronto, ON M5V 3L9, Canada";
        String destination = "Bahen Centre for Information Technology, 40 St George St, Toronto, ON M5S 2E4, Canada";

        GoogleDistanceService singleDistanceCalculator = new GoogleDistanceService();

        long distance = singleDistanceCalculator.getDistanceForSingleDestination(origin, destination);

        assertTrue(distance > 0, "Distance should be positive");

        long expectedDistanceInMeters = 2000;
        long tolerance = 500;
        assertTrue(distance >= expectedDistanceInMeters - tolerance &&
                        distance <= expectedDistanceInMeters + tolerance,
                "Distance should be around 2 km");
    }

    @Test
    void testMultiDestinationDistanceCalculation() throws ApiException, InterruptedException, IOException {
        String origin = "Eiffel Tower, 5 Avenue Anatole France, 75007 Paris, France";
        String[] destinations = {
                "Louvre Museum, Rue de Rivoli, 75001 Paris, France",
                "Palace of Versailles, Place d'Armes, 78000 Versailles, France"
        };

        GoogleDistanceService multiDestinationDistanceCalculator = new GoogleDistanceService();

        List<Long> distances = multiDestinationDistanceCalculator.getDistancesToDestinations(origin, destinations);

        assertNotNull(distances, "Distance list should not be null");
        assertEquals(2, distances.size(), "Should return exactly two distances");

        long distanceToLouvre = distances.get(0);
        long distanceToVersailles = distances.get(1);
        System.out.println(distanceToLouvre + " " + distanceToVersailles);

        assertTrue(distanceToLouvre > 0, "Distance to Louvre should be positive");
        assertTrue(distanceToVersailles > 0, "Distance to Versailles should be positive");

        assertTrue(distanceToVersailles > distanceToLouvre, "Versailles should be further than the Louvre");

        long expectedLouvreDistance = 3400;
        long tolerance = 3000;
        assertTrue(distanceToLouvre >= expectedLouvreDistance - tolerance &&
                        distanceToLouvre <= expectedLouvreDistance + tolerance,
                "Distance to Louvre should be around 3.4 km");

        long expectedVersailleDistance = 20000;
        long tolerance2 = 10000;
        assertTrue(distanceToVersailles >= expectedVersailleDistance - tolerance2 &&
                        distanceToVersailles <= expectedVersailleDistance + tolerance2,
                "Distance to Versailles should be around 17 km");
    }


    @Test
    void testInvalidDestinationHandling() throws ApiException, InterruptedException, IOException {

        String origin = "London, UK";
        String destination = "Imaginary NonExistent Place ABC 123";

        GoogleDistanceService singleDistanceCalculator = new GoogleDistanceService();

        long distance = singleDistanceCalculator.getDistanceForSingleDestination(origin, destination);

        assertEquals(-1, distance, "Should return -1 for an invalid location");
    }
}