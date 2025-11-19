package data_access;

import Entities.Listing;
import Entities.User;
import interface_adapter.filter.FilterListingsPresenter;
import use_case.filter.FilterListingsDataAccessInterface;
import use_case.search_listings.SearchListingDataAccessInterface;
import use_case.my_listings.MyListingsDataAccessInterface;
import use_case.extract_tags.ExtractTagsDataAccessInterface;

import java.util.*;

public class InMemoryListingDAO implements SearchListingDataAccessInterface,
        MyListingsDataAccessInterface,
        ExtractTagsDataAccessInterface, FilterListingsDataAccessInterface {
    private final ArrayList<Listing> listings;
    private final HashMap<String, User> users;

    public InMemoryListingDAO() {
        this.listings = new ArrayList<>();
        this.users = new HashMap<>();
    }

    @Override
    public ArrayList<Listing> getAllActiveListings() {
        ArrayList<Listing> activeListings = new ArrayList<>();
        for (Listing listing : listings) {
            if (listing.isActive()) {
                activeListings.add(listing);
            }
        }
        return activeListings;
    }

    @Override
    public User getUserByUsername(String username) {
        return users.get(username);
    }

    @Override
    public Listing getListingByName(String name) {
        for (Listing listing : listings) {
            if (listing.getName().equals(name)) {
                return listing;
            }
        }
        return null;
    }

    @Override
    public void saveListing(Listing listing) {
        listings.removeIf(l -> l.getName().equals(listing.getName()));
        listings.add(listing);
    }

    public void addListing(Listing listing) {
        listings.add(listing);
    }

    public void addUser(User user) {
        users.put(user.getUsername(), user);
    }
}