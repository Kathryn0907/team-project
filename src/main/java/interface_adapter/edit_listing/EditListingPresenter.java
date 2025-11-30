package interface_adapter.edit_listing;

import Entities.Listing;
import interface_adapter.ProfileViewModel;
import interface_adapter.ViewManagerModel;
import use_case.edit_listing.EditListingOutputBoundary;
import use_case.edit_listing.EditListingOutputData;

import javax.swing.*;

public class EditListingPresenter implements EditListingOutputBoundary {

    private final ProfileViewModel profileViewModel;
    private final ViewManagerModel viewManagerModel;

    public EditListingPresenter(ProfileViewModel profileViewModel,
                                ViewManagerModel viewManagerModel) {
        this.profileViewModel = profileViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView(EditListingOutputData data) {
        Listing updated = data.getUpdatedListing();

        profileViewModel.replaceListing(updated);

        viewManagerModel.setState("profile");
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        JOptionPane.showMessageDialog(null, errorMessage);
    }
}