package use_case.edit_listing;

public interface EditListingOutputBoundary {

    void prepareSuccessView(EditListingOutputData data);

    void prepareFailView(String errorMessage);
}