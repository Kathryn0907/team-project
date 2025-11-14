package use_case.search_listings;

import Entities.Listing;
import java.util.ArrayList;

public interface SearchListingDataAccessInterface {
    ArrayList<Listing> getAllActiveListings();
}