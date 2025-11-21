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


    private String apiKey;


    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }


    /**
     * This method returns a list of distances for one origin and multiple destinations.
     * @param origin The origin, String.
     * @param destinations The destinations, a list of String.
     * @return A list of Long. Same order with the input destinations. Give a null distance for failed destination.
     * @throws ApiException Api exception
     * @throws InterruptedException Api exception
     * @throws IOException Api exception
     */
    public List<Long> getDistancesToDestinations(GeoApiContext context,String origin, String[] destinations)
            throws ApiException, InterruptedException, IOException {

        List<Long> distancesInMeters = new ArrayList<>();
        String[] originsArray = {origin}; // API requires an array for origins

        // Make the API request
        DistanceMatrix matrix = DistanceMatrixApi.newRequest(context)
                .origins(originsArray)
                .destinations(destinations)
                .mode(TravelMode.DRIVING)
                .await(); // Synchronous call

        // Process the response
        if (matrix.rows != null && matrix.rows.length > 0) {
            DistanceMatrixElement[] elements = matrix.rows[0].elements;

            for (DistanceMatrixElement element : elements) {
                if (element.status == DistanceMatrixElementStatus.OK) {
                    distancesInMeters.add(element.distance.inMeters);
                } else {
                    // Handle cases where a specific destination failed (e.g., location not found)
                    System.err.println("Error for one destination: " + element.status);
                    distancesInMeters.add(null);
                }
            }
        }


        return distancesInMeters;
    }


    /**
     * Simple method that returns the distance in meters between single origin and single destination.
     * @param origin String, the origin
     * @param destination String, the destination.
     * @return long, distance in meters. return -1 if the call failed.
     */
    public long getDistanceForSingleDestination(GeoApiContext context, String origin, String destination)
            throws ApiException, InterruptedException, IOException{

        String[] origins = {origin};
        String[] destinations = {destination};
        long distance;


        // Make the synchronous request to the Distance Matrix API
        DistanceMatrix matrix = DistanceMatrixApi.newRequest(context)
                .origins(origins)
                .destinations(destinations)
                .mode(TravelMode.DRIVING) // Specify the travel mode
                .await(); // Use await() for synchronous requests

        if (matrix.rows != null && matrix.rows.length > 0 && matrix.rows[0].elements.length > 0) {
            DistanceMatrixElement element = matrix.rows[0].elements[0];

            if (element.status == com.google.maps.model.DistanceMatrixElementStatus.OK) {
                distance = element.distance.inMeters;
            } else {
                System.err.println("API Element Status Error: " + element.status + " for route: " + origin + " to " + destination);
                distance = -1; // Indicate failure
            }
        } else {
            System.err.println("API Response Error: No rows or elements returned.");
            distance = -1; // Indicate failure
        }


        return distance;
    }


    /**
     * For single origin and single destination.
     * @param origin String, the origin
     * @param destination String, the destination.
     * @return long, distance in meters. return -1 if the call failed.
     */
    @Override
    public double calculateDistanceKm(String origin, String destination) {

        // I wrote the method here. But I recommend using getDistancesToDestinations above
        // because I think it is efficient for your filter@samantha

        long dis;

        setApiKey(System.getenv("GOOGLE_API_KEY"));

        // Object for submitting Api key. Like request.
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(apiKey)
                .build();

        try{
            dis = getDistanceForSingleDestination(context,origin,destination);
        }
        catch(Exception e){
            dis = -1;
        }

        // NECESSARY! Shutdown the context. Please have it at the end of your method.
        context.shutdown();

        return dis;
    }
}