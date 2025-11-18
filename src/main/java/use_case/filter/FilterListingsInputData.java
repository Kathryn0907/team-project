package use_case.filter;

import Entities.Listing;


public class FilterListingsInputData {

    private final String userAddress;
    private final Double maxDistanceKm;

    private final Double minPrice;
    private final Double maxPrice;

    private final Double minArea;
    private final Double maxArea;

    private final Integer minBedrooms;
    private final Integer minBathrooms;

    private final Listing.BuildingType buildingType;

    public FilterListingsInputData(String userAddress,
                                   Double maxDistanceKm,
                                   Double minPrice,
                                   Double maxPrice,
                                   Double minArea,
                                   Double maxArea,
                                   Integer minBedrooms,
                                   Integer minBathrooms,
                                   Listing.BuildingType buildingType) {

        this.userAddress = userAddress;
        this.maxDistanceKm = maxDistanceKm;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.minArea = minArea;
        this.maxArea = maxArea;
        this.minBedrooms = minBedrooms;
        this.minBathrooms = minBathrooms;
        this.buildingType = buildingType;
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

    public Listing.BuildingType getBuildingType() {
        return buildingType;
    }
}