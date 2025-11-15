package use_case.create_listing;

public interface CreateListingOutputBoundary {

    void prepareSuccessView(CreateListingOutputData outputData);

    void prepareFailView(String errorMessage);

    void switchToProfileView();

}
