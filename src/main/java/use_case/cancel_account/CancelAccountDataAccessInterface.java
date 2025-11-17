package use_case.cancel_account;
import Entities.User;

public interface CancelAccountDataAccessInterface {


    boolean existByUsername(String username);

    void cancelAccount(String username);

    User getUser(String username);
}
