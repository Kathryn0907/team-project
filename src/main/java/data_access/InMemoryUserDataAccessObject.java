package data_access;

import Entities.User;
import Entities.Listing;

import use_case.change_password.ChangePasswordUserDataAccessInterface;
import use_case.login.LoginUserDataAccessInterface;
import use_case.logout.LogoutUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;
import use_case.check_favorite.CheckFavoriteDataAccessInterface;
import use_case.save_favorite.SaveFavoriteDataAccessInterface;

import java.util.HashMap;
import java.util.Map;

public class InMemoryUserDataAccessObject implements
        SignupUserDataAccessInterface,
        LoginUserDataAccessInterface,
        ChangePasswordUserDataAccessInterface,
        LogoutUserDataAccessInterface,
        CheckFavoriteDataAccessInterface,
        SaveFavoriteDataAccessInterface {

    private final Map<String, User> users = new HashMap<>();
    private String currentUsername;

    // Reference to listing DAO so we can resolve listings by name for favourites
    private InMemoryListingDataAccessObject listingDataAccessObject;

    public InMemoryUserDataAccessObject() {}

    // Called from AppBuilder to inject the listing DAO
    public void setListingDataAccessObject(InMemoryListingDataAccessObject listingDAO) {
        this.listingDataAccessObject = listingDAO;
    }

    // ===== Common user methods =====

    @Override
    public void changePassword(User user) {
        users.put(user.getUsername(), user);
    }

    @Override
    public User get(String username) {
        return users.get(username);
    }

    @Override
    public boolean existsByName(String username) {
        return users.containsKey(username);
    }

    @Override
    public void save(User user) {
        users.put(user.getUsername(), user);
    }

    @Override
    public void setCurrentUsername(String name) {
        currentUsername = name;
    }

    @Override
    public String getCurrentUsername() {
        return currentUsername;
    }

    // ===== For CheckFavoriteDataAccessInterface =====

    @Override
    public User getUser(String username) {
        return users.get(username);
    }

    // ===== For SaveFavoriteDataAccessInterface =====

    @Override
    public void saveUser(User user) {
        // same as save(...)
        users.put(user.getUsername(), user);
    }

    @Override
    public Listing getListingByName(String listingName) {
        if (listingDataAccessObject == null) {
            return null; // or throw if you want it to blow up loudly
        }
        return listingDataAccessObject.getListingByName(listingName);
    }
}
