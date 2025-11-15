package data_access;

import Entities.User;
import use_case.change_password.ChangePasswordUserDataAccessInterface;
import use_case.login.LoginUserDataAccessInterface;
import use_case.logout.LogoutUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;

import java.util.HashMap;
import java.util.Map;


/**
 * In-memory implementation of the DAO for storing user data. This implementation does
 * NOT persist data between runs of the program.
 * ---
 * Can be used for testing
 * This DAO only saves User info for now.
 * ---
 */

public class InMemoryUserDataAccessObject implements SignupUserDataAccessInterface,
                                                    LoginUserDataAccessInterface,
                                                    ChangePasswordUserDataAccessInterface,
                                                    LogoutUserDataAccessInterface {

    private final Map<String, User> users = new HashMap<>();

    private String currentUsername;


    @Override
    public void changePassword(User user) {
        users.put(user.getUsername(), user);
    }

    @Override
    public User get(String username) {
        return users.get(username);
    }

    @Override
    public void setCurrentUsername(String name) {
        currentUsername = name;
    }

    @Override
    public String getCurrentUsername() {
        return currentUsername;
    }

    @Override
    public boolean existsByName(String username) {
        return users.containsKey(username);
    }

    @Override
    public void save(User user) {
        users.put(user.getUsername(), user);
    }
}
