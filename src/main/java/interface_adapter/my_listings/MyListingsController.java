package interface_adapter.my_listings;

import use_case.my_listings.MyListingsInputBoundary;
import use_case.my_listings.MyListingsInputData;
import use_case.my_listings.MyListingsOutputData;

public class MyListingsController {
    private final MyListingsInputBoundary interactor;

    public MyListingsController(MyListingsInputBoundary interactor) {
        this.interactor = interactor;
    }

    public MyListingsOutputData execute(String username) {
        MyListingsInputData inputData = new MyListingsInputData(username);
        return interactor.execute(inputData);
    }
}