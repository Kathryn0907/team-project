package use_case.cancel_account;

import Entities.Listing;
import Entities.User;
import data_access.ListingDataAccessObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CancelAccountInteractorTest {

    @Test
    public void testSuccessCancelAccount() {

        CancelAccountInputData inputData = new CancelAccountInputData("Jason", "password");
        ListingDataAccessObject userRepository = new ListingDataAccessObject();

        User user1 = new User("Jason", "password");
        userRepository.addUser(user1);
        User user2 = new User("Paul", "password");
        userRepository.addUser(user2);

        Listing listing1 = new Listing();
        listing1.setOwner(user1);
        Listing listing2 = new Listing();
        listing2.setOwner(user2);

        userRepository.addListing(listing1);
        userRepository.addListing(listing2);

        CancelAccountOutputBoundary successPresenter = new CancelAccountOutputBoundary() {
            @Override
            public void prepareSuccess(CancelAccountOutputData cancelAccountOutputData) {
                assertEquals("Jason", cancelAccountOutputData.getUsername());

                assertFalse(userRepository.existByUsername(cancelAccountOutputData.getUsername()));
                assertFalse(userRepository.getAllActiveListings().contains(listing1));
                assertTrue(userRepository.getAllActiveListings().contains(listing2));
            }

            @Override
            public void prepareFailure(String message) {
                fail("Use case failure is unexpected.");
            }

            @Override
            public void back() {
                fail("Use case failure is unexpected.");
            }
        };

        CancelAccountInputBoundary cancelAccountInteractor = new CancelAccountInteractor(userRepository, successPresenter);
        cancelAccountInteractor.execute(inputData);


    }
}
