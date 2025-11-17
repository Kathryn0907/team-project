package use_case.cancel_account;
import Entities.User;

public interface CancelAccountDataAccessInterface {


    boolean existByUsername(String username);


    /**
     * Cancel the account by username.
     * Note: The Listings belongs to this user is also deleted (or deactivated)
     * @param username username
     */
    void cancelAccount(String username);

    User getUser(String username);
}
