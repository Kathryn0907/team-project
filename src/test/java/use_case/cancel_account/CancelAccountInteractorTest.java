package use_case.cancel_account;

import Entities.Listing;
import Entities.User;
import data_access.InMemoryListingDataAccessObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CancelAccountInteractorTest {

    @Test
    public void testSuccessCancelAccount() {

        CancelAccountInputData inputData = new CancelAccountInputData("Jason", "password");
        CancelAccountDataAccessInterface userRepository = new InMemoryListingDataAccessObject();

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
                System.out.println("Line 34 test successful");
                assertFalse(userRepository.existByUsername(cancelAccountOutputData.getUsername()));
                System.out.println("Line 36 test successful");
                assertFalse(userRepository.getAllActiveListings().contains(listing1));
                System.out.println("Line 38 test successful");
                assertTrue(userRepository.getAllActiveListings().contains(listing2));
                System.out.println("Line 40 test successful");
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