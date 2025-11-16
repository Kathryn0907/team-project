package use_case.filter;

import Entities.Listing;

import java.util.List;

/**
 * Minimal data access needed by the Filter Listings use case.
 * Your actual Listing DAO in data_access can implement this.
 */
public interface FilterListingsDataAccessInterface {

    /**
     * Returns all active listings that are candidates for filtering.
     */
    List<Listing> getAllActiveListings();
}
