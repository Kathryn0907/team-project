package interface_adapter.login;

import app.AppBuilder;
import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.signup.SignupViewModel;
import use_case.login.LoginOutputBoundary;
import use_case.login.LoginOutputData;

/**
 * The Presenter for the Login Use Case.
 * Initializes conversations after successful login.
 */
public class LoginPresenter implements LoginOutputBoundary {

    private final LoginViewModel loginViewModel;
    private final LoggedInViewModel loggedInViewModel;
    private final ViewManagerModel viewManagerModel;
    private final SignupViewModel signupViewModel;

    public LoginPresenter(ViewManagerModel viewManagerModel,
                          LoggedInViewModel loggedInViewModel,
                          LoginViewModel loginViewModel,
                          SignupViewModel signupViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.loggedInViewModel = loggedInViewModel;
        this.loginViewModel = loginViewModel;
        this.signupViewModel = signupViewModel;
    }

    @Override
    public void prepareSuccessView(LoginOutputData response) {
        // On success, update the loggedInViewModel's state
        final LoggedInState loggedInState = loggedInViewModel.getState();
        loggedInState.setUser(response.getUser());
        loggedInState.setUsername(response.getUsername());
        this.loggedInViewModel.firePropertyChange();

        // Clear everything from the LoginViewModel's state
        loginViewModel.setState(new LoginState());

        // Initialize conversations view for this user
        try {
            AppBuilder appBuilder = AppBuilder.getInstance();
            if (appBuilder != null) {
                appBuilder.initializeConversationsForUser(response.getUsername());
                System.out.println("✅ Conversations initialized after login for: " + response.getUsername());
            } else {
                System.err.println("⚠️  AppBuilder instance is null");
            }
        } catch (Exception e) {
            System.err.println("⚠️  Could not initialize conversations: " + e.getMessage());
            e.printStackTrace();
        }

        // Switch to the logged in view
        this.viewManagerModel.setState(loggedInViewModel.getViewName());
        this.viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String error) {
        final LoginState loginState = loginViewModel.getState();
        loginState.setLoginError(error);
        loginViewModel.firePropertyChange();
    }

    @Override
    public void switchToSignup() {
        loginViewModel.setState(new LoginState());
        loginViewModel.firePropertyChange();

        viewManagerModel.setState(signupViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }
}