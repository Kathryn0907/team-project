package use_case.my_listings;

public interface MyListingsOutputBoundary {
    void prepareSuccessView(MyListingsOutputData outputData);
    void prepareFailView(String errorMessage);
}