package interface_adapter.cancel_account;

import interface_adapter.ProfileViewModel;
import interface_adapter.ViewManagerModel;
import interface_adapter.login.LoginState;
import interface_adapter.login.LoginViewModel;
import use_case.cancel_account.CancelAccountOutputBoundary;
import use_case.cancel_account.CancelAccountOutputData;


public class CancelAccountPresenter implements CancelAccountOutputBoundary {

    private final CancelAccountViewModel cancelAccountViewModel;
    private final LoginViewModel loginViewModel;
    private final ViewManagerModel viewManagerModel;
    private final ProfileViewModel profileViewModel;

    public CancelAccountPresenter(CancelAccountViewModel cancelAccountViewModel,
                                  LoginViewModel loginViewModel,
                                  ViewManagerModel viewManagerModel,
                                  ProfileViewModel profileViewModel
    ) {
        this.cancelAccountViewModel = cancelAccountViewModel;
        this.loginViewModel = loginViewModel;
        this.viewManagerModel = viewManagerModel;
        this.profileViewModel = profileViewModel;
    }


    @Override
    public void prepareSuccess(CancelAccountOutputData cancelAccountOutputData) {

        final CancelAccountState state = cancelAccountViewModel.getState();
        state.setUsername("");
        cancelAccountViewModel.firePropertyChange();

        final LoginState loginState = loginViewModel.getState();
        loginState.setCancelAccountSuccessMsg("The account with username " +
                cancelAccountOutputData.getUsername() + " has been successfully canceled.");
        loginViewModel.firePropertyChange();

        this.viewManagerModel.setState(loginViewModel.getViewName());
        this.viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareFailure(String message) {
        final CancelAccountState state = cancelAccountViewModel.getState();
        state.setCancelAccountError(message);
        cancelAccountViewModel.firePropertyChange();
    }

    @Override
    public void back() {
        // Back to profile.
        viewManagerModel.setState(profileViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }
}