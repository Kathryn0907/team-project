package data_access;

import use_case.filter.DistanceService;

public class GoogleDistanceService implements DistanceService {
    @Override
    public double calculateDistanceKm(String origin, String destination) {
        // call Google API here
    }
}