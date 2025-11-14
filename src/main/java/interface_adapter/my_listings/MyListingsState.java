package interface_adapter.my_listings;

import Entities.Listing;
import java.util.ArrayList;

public class MyListingsState {
    private ArrayList<Listing> listings = new ArrayList<>();
    private String errorMessage = "";
    private boolean success = false;

    public ArrayList<Listing> getListings() {
        return listings;
    }

    public void setListings(ArrayList<Listing> listings) {
        this.listings = listings;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}