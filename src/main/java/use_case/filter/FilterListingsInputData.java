package use_case.filter;

import Entities.Listing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * All input parameters for the Filter Listings use case.
 *
 * Any field that is null (or an empty list) means
 * "do NOT filter on this property".
 */
public class FilterListingsInputData {

    // -------- Location / distance (Google API will use these) --------

    /** Address of the user performing the search (can be null if no distance filter). */
    private final String userAddress;

    /** Maximum distance in km from userAddress to the listing (null = no distance filter). */
    private final Double maxDistanceKm;

    // -------- Price --------
    private final Double minPrice;
    private final Double maxPrice;

    // -------- Area (square meters / feet) --------
    private final Double minArea;
    private final Double maxArea;

    // -------- Bedrooms & bathrooms --------
    private final Integer minBedrooms;
    private final Integer minBathrooms;

    // -------- Building types (CONDO, HOUSE, etc.) --------
    /**
     * Allowed building types. Empty list (or null in constructor) means
     * "any building type is allowed".
     */
    private final List<Listing.BuildingType> buildingTypes;

    // -------- Tags & main categories (from Imagga) --------
    /**
     * Tags that the listing should contain (list can be empty).
     */
    private final List<String> requiredTags;

    /**
     * Main categories that the listing should contain (list can be empty).
     */
    private final List<String> requiredCategories;

    public FilterListingsInputData(String userAddress,
                                   Double maxDistanceKm,
                                   Double minPrice,
                                   Double maxPrice,
                                   Double minArea,
                                   Double maxArea,
                                   Integer minBedrooms,
                                   Integer minBathrooms,
                                   List<Listing.BuildingType> buildingTypes,
                                   List<String> requiredTags,
                                   List<String> requiredCategories) {

        this.userAddress = userAddress;
        this.maxDistanceKm = maxDistanceKm;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.minArea = minArea;
        this.maxArea = maxArea;
        this.minBedrooms = minBedrooms;
        this.minBathrooms = minBathrooms;

        // Defensive copies so nobody can mutate from the outside
        if (buildingTypes != null) {
            this.buildingTypes = Collections.unmodifiableList(new ArrayList<>(buildingTypes));
        } else {
            this.buildingTypes = Collections.emptyList();
        }

        if (requiredTags != null) {
            this.requiredTags = Collections.unmodifiableList(new ArrayList<>(requiredTags));
        } else {
            this.requiredTags = Collections.emptyList();
        }

        if (requiredCategories != null) {
            this.requiredCategories = Collections.unmodifiableList(new ArrayList<>(requiredCategories));
        } else {
            this.requiredCategories = Collections.emptyList();
        }
    }


    public String getUserAddress() {
        return userAddress;
    }

    public Double getMaxDistanceKm() {
        return maxDistanceKm;
    }

    public Double getMinPrice() {
        return minPrice;
    }

    public Double getMaxPrice() {
        return maxPrice;
    }

    public Double getMinArea() {
        return minArea;
    }

    public Double getMaxArea() {
        return maxArea;
    }

    public Integer getMinBedrooms() {
        return minBedrooms;
    }

    public Integer getMinBathrooms() {
        return minBathrooms;
    }

    public List<Listing.BuildingType> getBuildingTypes() {
        return buildingTypes;
    }

    public List<String> getRequiredTags() {
        return requiredTags;
    }

    public List<String> getRequiredCategories() {
        return requiredCategories;
    }
}