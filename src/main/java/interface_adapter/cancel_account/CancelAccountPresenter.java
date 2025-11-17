package interface_adapter.cancel_account;

import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.login.LoginState;
import interface_adapter.login.LoginViewModel;
import use_case.cancel_account.CancelAccountOutputBoundary;
import use_case.cancel_account.CancelAccountOutputData;


public class CancelAccountPresenter implements CancelAccountOutputBoundary {

    private CancelAccountViewModel cancelAccountViewModel;
    private LoginViewModel loginViewModel;
    private ViewManagerModel viewManagerModel;

    // profile part not done yet. I expect the cancel account function will be placed in profile.
    // private ProfileViewModel profileViewModel;

    public CancelAccountPresenter(CancelAccountViewModel cancelAccountViewModel,
                                  LoginViewModel loginViewModel,
                                  ViewManagerModel viewManagerModel
                                // ProfileViewModel profileViewModel
    ) {
        this.cancelAccountViewModel = cancelAccountViewModel;
        this.loginViewModel = loginViewModel;
        this.viewManagerModel = viewManagerModel;
        // this.profileViewModel = profileViewModel
    }


    @Override
    public void prepareSuccess(CancelAccountOutputData cancelAccountOutputData) {

        final CancelAccountState state = cancelAccountViewModel.getState();
        state.setUsername("");
        cancelAccountViewModel.firePropertyChange();


    }

    @Override
    public void prepareFailure(String message) {

    }

    @Override
    public void back() {

    }
}
