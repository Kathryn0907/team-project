package use_case.login;

import Entities.User;

/**
 * Output Data for the Login Use Case.
 */
public class LoginOutputData {

    private final String username;
    private final User user;

    public LoginOutputData(User user) {
        this.username = user.getUsername();
        this.user = user;
    }

    public LoginOutputData(String username) {
        this.user = null;
        this.username = username;
    }

    public User getUser() {
        return user;
    }

    public String getUsername() {
        return username;
    }

}
