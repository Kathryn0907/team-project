package interface_adapter;

import Entities.Listing;

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

    public void setListings(List<Listing> listings) {
        this.myListings = listings != null ? listings : new ArrayList<>();
        setState(this.myListings);
        firePropertyChange();
    }

    public void replaceListing(Listing updated) {
        for (int i = 0; i < myListings.size(); i++) {
            if (myListings.get(i).getId().equals(updated.getId())) {
                myListings.set(i, updated);
                break;
            }
        }
        setState(myListings);
        firePropertyChange();
    }
}
