package data_access;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.DistanceMatrixElementStatus;
import com.google.maps.model.TravelMode;
import use_case.filter.DistanceService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GoogleDistanceService implements DistanceService {

    private final String apiKey;
    private final GeoApiContext context;

    public GoogleDistanceService() {
        this.apiKey = System.getenv("GOOGLE_API_KEY");
        if (this.apiKey == null || this.apiKey.isEmpty()) {
            throw new RuntimeException("GOOGLE_API_KEY environment variable is not set.");
        }
        this.context = new GeoApiContext.Builder()
                .apiKey(this.apiKey)
                .build();
    }

    public GoogleDistanceService(String apiKey) {
        this.apiKey = apiKey;
        if (this.apiKey == null || this.apiKey.isEmpty()) {
            throw new RuntimeException("API key cannot be null or empty");
        }
        this.context = new GeoApiContext.Builder()
                .apiKey(this.apiKey)
                .build();
    }

    public List<Long> getDistancesToDestinations(String origin, String[] destinations)
            throws ApiException, InterruptedException, IOException {

        List<Long> distancesInMeters = new ArrayList<>();
        String[] originsArray = {origin};

        DistanceMatrix matrix = DistanceMatrixApi.newRequest(context)
                .origins(originsArray)
                .destinations(destinations)
                .mode(TravelMode.DRIVING)
                .await();

        if (matrix.rows != null && matrix.rows.length > 0) {
            DistanceMatrixElement[] elements = matrix.rows[0].elements;
            for (DistanceMatrixElement element : elements) {
                if (element.status == DistanceMatrixElementStatus.OK) {
                    distancesInMeters.add(element.distance.inMeters);
                } else {
                    System.err.println("Error for one destination: " + element.status);
                    distancesInMeters.add(null);
                }
            }
        }
        return distancesInMeters;
    }

    public long getDistanceForSingleDestination(String origin, String destination)
            throws ApiException, InterruptedException, IOException {

        String[] origins = {origin};
        String[] destinations = {destination};
        long distance;

        DistanceMatrix matrix = DistanceMatrixApi.newRequest(context)
                .origins(origins)
                .destinations(destinations)
                .mode(TravelMode.DRIVING)
                .await();

        if (matrix.rows != null && matrix.rows.length > 0 && matrix.rows[0].elements.length > 0) {
            DistanceMatrixElement element = matrix.rows[0].elements[0];
            if (element.status == DistanceMatrixElementStatus.OK) {
                distance = element.distance.inMeters;
            } else {
                System.err.println("API Error: " + element.status);
                distance = -1;
            }
        } else {
            System.err.println("No response from API");
            distance = -1;
        }
        return distance;
    }

    @Override
    public double calculateDistanceKm(String origin, String destination) {
        try {
            long distanceMeters = getDistanceForSingleDestination(origin, destination);
            if (distanceMeters == -1) {
                return -1.0;
            }
            return distanceMeters / 1000.0; // Convert to kilometers
        } catch (Exception e) {
            System.err.println("Error calculating distance: " + e.getMessage());
            e.printStackTrace();
            return -1.0;
        }
    }

    public void shutdown() {
        if (context != null) {
            context.shutdown();
        }
    }
}