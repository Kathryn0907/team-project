package interface_adapter.delete_listing;

import interface_adapter.ProfileViewModel;
import interface_adapter.ViewManagerModel;
import use_case.delete_listing.DeleteListingOutputBoundary;
import use_case.delete_listing.DeleteListingOutputData;

public class DeleteListingPresenter implements DeleteListingOutputBoundary {

    private final ProfileViewModel profileViewModel;
    private final ViewManagerModel viewManagerModel;

    public DeleteListingPresenter(ProfileViewModel profileViewModel,
                                  ViewManagerModel viewManagerModel) {
        this.profileViewModel = profileViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView(DeleteListingOutputData data) {

        profileViewModel.getMyListings()
                .removeIf(listing -> listing.getId().equals(data.getListingId()));

        profileViewModel.setState(profileViewModel.getMyListings());
        profileViewModel.firePropertyChange();

        viewManagerModel.setState("profile");
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String error) {
        System.err.println("Delete Listing Error: " + error);
    }
}
