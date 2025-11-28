package interface_adapter.create_listing;

import interface_adapter.ProfileViewModel;
import interface_adapter.ViewManagerModel;
import use_case.create_listing.CreateListingOutputBoundary;
import use_case.create_listing.CreateListingOutputData;

public class CreateListingPresenter implements CreateListingOutputBoundary {
    private final CreateListingViewModel createListingViewModel;
    private final ProfileViewModel profileViewModel;
    private final ViewManagerModel viewManagerModel;

    public CreateListingPresenter(CreateListingViewModel createListingViewModel, ProfileViewModel profileViewModel,
                                  ViewManagerModel viewManagerModel) {
        this.createListingViewModel = createListingViewModel;
        this.profileViewModel = profileViewModel;
        this.viewManagerModel = viewManagerModel;
    }
    @Override
    public void prepareSuccessView(CreateListingOutputData response) {
        profileViewModel.addListing(
                response.getListing());

        viewManagerModel.setState(profileViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String error) {
        final CreateListingState createListingState = createListingViewModel.getState();
        createListingState.setNameError(error);
        createListingViewModel.firePropertyChange();
    }

    @Override
    public void switchToProfileView() {
        viewManagerModel.setState(profileViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }
}
