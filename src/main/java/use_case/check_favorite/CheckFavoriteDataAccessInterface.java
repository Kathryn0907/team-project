package use_case.check_favorite;

import Entities.User;


public interface CheckFavoriteDataAccessInterface {
    User getUser(String username);
}
