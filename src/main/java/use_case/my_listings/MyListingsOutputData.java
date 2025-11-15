package use_case.my_listings;

import Entities.Listing;
import java.util.ArrayList;

public class MyListingsOutputData {
    private final ArrayList<Listing> listings;
    private final boolean success;

    public MyListingsOutputData(ArrayList<Listing> listings, boolean success) {
        this.listings = listings;
        this.success = success;
    }

    public ArrayList<Listing> getListings() {
        return listings;
    }

    public boolean isSuccess() {
        return success;
    }
}