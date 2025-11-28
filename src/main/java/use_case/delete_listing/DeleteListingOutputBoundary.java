package use_case.delete_listing;

public interface DeleteListingOutputBoundary {

    void prepareSuccessView(DeleteListingOutputData data);

    void prepareFailView(String error);
}
