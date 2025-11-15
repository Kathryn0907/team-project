package use_case.my_listings;

import Entities.User;

public interface MyListingsDataAccessInterface {
    User getUserByUsername(String username);
}