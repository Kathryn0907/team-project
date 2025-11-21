package data_access;

import Entities.Listing;
import Entities.User;
import use_case.cancel_account.CancelAccountDataAccessInterface;
import use_case.create_listing.CreateListingDataAccessInterface;
import use_case.filter.FilterListingsDataAccessInterface;
import use_case.search_listings.SearchListingDataAccessInterface;
import use_case.my_listings.MyListingsDataAccessInterface;
import use_case.extract_tags.ExtractTagsDataAccessInterface;
import use_case.save_favorite.SaveFavoriteDataAccessInterface;
import use_case.check_favorite.CheckFavoriteDataAccessInterface;

import java.util.*;

public class InMemoryListingDataAccessObject implements SearchListingDataAccessInterface,
        CreateListingDataAccessInterface,
        MyListingsDataAccessInterface,
        ExtractTagsDataAccessInterface,
        SaveFavoriteDataAccessInterface,
        CheckFavoriteDataAccessInterface,
        FilterListingsDataAccessInterface,
        CancelAccountDataAccessInterface {

    private final ArrayList<Listing> listings;
    private final HashMap<String, User> users;

    public InMemoryListingDataAccessObject() {
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

    @Override
    public void save(Listing listing) {
        listings.removeIf(l -> l.getName().equals(listing.getName()));
        listings.add(listing);
    }

    public void addListing(Listing listing) {
        listings.add(listing);
    }

    public void addUser(User user) {
        users.put(user.getUsername(), user);
    }


    @Override
    public boolean existByUsername(String username) {
        return users.containsKey(username);
    }

    @Override
    public void cancelAccount(String username) {
        listings.removeIf(l -> l.getOwner().getUsername().equals(username));
        users.remove(username);
    }

    // SaveFavoriteDataAccessInterface methods
    @Override
    public User getUser(String username) {
        return users.get(username);
    }

    @Override
    public void saveUser(User user) {
        users.put(user.getUsername(), user);
    }
}