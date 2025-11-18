package use_case.filter;

import Entities.Listing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Output data for the Filter Listings use case.
 * Contains a list of listings that matched the filter criteria,
 * in a UI-friendly format (no direct Entity exposure if you don't want to).
 */
public class FilterListingsOutputData {

    /**
     * A simplified snapshot of a Listing for display.
     */
    public static class ListingResult {
        private final String name;
        private final double price;
        private final String address;
        private final double distanceKm;
        private final double area;
        private final int bedrooms;
        private final int bathrooms;
        private final Listing.BuildingType buildingType;
        private final List<String> tags;
        private final List<String> mainCategories;

        public ListingResult(String name,
                             double price,
                             String address,
                             double distanceKm,
                             double area,
                             int bedrooms,
                             int bathrooms,
                             Listing.BuildingType buildingType,
                             List<String> tags,
                             List<String> mainCategories) {
            this.name = name;
            this.price = price;
            this.address = address;
            this.distanceKm = distanceKm;
            this.area = area;
            this.bedrooms = bedrooms;
            this.bathrooms = bathrooms;
            this.buildingType = buildingType;

            // defensive copies
            this.tags = tags != null
                    ? Collections.unmodifiableList(new ArrayList<>(tags))
                    : Collections.emptyList();

            this.mainCategories = mainCategories != null
                    ? Collections.unmodifiableList(new ArrayList<>(mainCategories))
                    : Collections.emptyList();
        }

        public String getName() {
            return name;
        }

        public double getPrice() {
            return price;
        }

        public String getAddress() {
            return address;
        }

        public double getDistanceKm() {
            return distanceKm;
        }

        public double getArea() {
            return area;
        }

        public int getBedrooms() {
            return bedrooms;
        }

        public int getBathrooms() {
            return bathrooms;
        }

        public Listing.BuildingType getBuildingType() {
            return buildingType;
        }

        public List<String> getTags() {
            return tags;
        }

        public List<String> getMainCategories() {
            return mainCategories;
        }
    }

    // ---------- Actual output list ----------

    private final List<ListingResult> results;

    public FilterListingsOutputData(List<ListingResult> results) {
        // defensive copy
        this.results = results != null
                ? Collections.unmodifiableList(new ArrayList<>(results))
                : Collections.emptyList();
    }

    public List<ListingResult> getResults() {
        return results;
    }
}