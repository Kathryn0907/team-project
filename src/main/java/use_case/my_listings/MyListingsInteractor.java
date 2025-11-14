package use_case.my_listings;

import Entities.User;
import Entities.Listing;
import java.util.ArrayList;

public class MyListingsInteractor implements MyListingsInputBoundary {
    private final MyListingsDataAccessInterface dataAccess;
    private final MyListingsOutputBoundary presenter;

    public MyListingsInteractor(MyListingsDataAccessInterface dataAccess,
                                MyListingsOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public MyListingsOutputData execute(MyListingsInputData inputData) {
        User user = dataAccess.getUserByUsername(inputData.getUsername());

        if (user == null) {
            presenter.prepareFailView("User not found");
            return new MyListingsOutputData(new ArrayList<>(), false);
        }

        ArrayList<Listing> userListings = new ArrayList<>(user.getMyListings());
        MyListingsOutputData outputData = new MyListingsOutputData(userListings, true);
        presenter.prepareSuccessView(outputData);
        return outputData;
    }
}