package interface_adapter;

import Entities.Listing;
import interface_adapter.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class ProfileViewModel extends ViewModel<List<Listing>> {

    private List<Listing> myListings = new ArrayList<>();

    public ProfileViewModel() {
        super("profile");
        setState(myListings);
    }

    public void addListing(Listing listing) {
        myListings.add(listing);
        setState(myListings);
        firePropertyChange();
    }

    public List<Listing> getMyListings() {
        return myListings;
    }
}
