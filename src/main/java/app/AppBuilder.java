package app;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import Entities.Listing;
import data_access.GoogleDistanceService;
import data_access.InMemoryListingDataAccessObject;
import data_access.InMemoryUserDataAccessObject;
import interface_adapter.ViewManagerModel;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;
import interface_adapter.search_listings.SearchListingController;
import interface_adapter.search_listings.SearchListingPresenter;
import interface_adapter.search_listings.SearchListingViewModel;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupPresenter;
import interface_adapter.signup.SignupViewModel;
import use_case.filter.DistanceService;
import use_case.filter.FilterListingsInputBoundary;
import use_case.filter.FilterListingsInteractor;
import use_case.filter.FilterListingsOutputBoundary;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.login.LoginOutputBoundary;
import use_case.search_listings.SearchListingDataAccessInterface;
import use_case.search_listings.SearchListingInputBoundary;
import use_case.search_listings.SearchListingInteractor;
import use_case.search_listings.SearchListingOutputBoundary;
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInteractor;
import use_case.signup.SignupOutputBoundary;
import view.*;

public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();

    final ViewManagerModel viewManagerModel = new ViewManagerModel();
    ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    // set which data access implementation to use, can be any
    // of the classes from the data_access package

    // DAO version using local file storage
    final InMemoryUserDataAccessObject userDataAccessObject = new InMemoryUserDataAccessObject();

    // DAO version using a shared external database
    // final DBUserDataAccessObject userDataAccessObject = new DBUserDataAccessObject(userFactory);

    private SignupView signupView;
    private SignupViewModel signupViewModel;
    private LoginViewModel loginViewModel;
    private SearchListingViewModel searchListingViewModel;
    private SearchView searchView;
    private LoginView loginView;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    public AppBuilder addSignupView() {
        signupViewModel = new SignupViewModel();
        signupView = new SignupView(signupViewModel);
        cardPanel.add(signupView, signupView.getViewName());
        return this;
    }

    public AppBuilder addLoginView() {
        loginViewModel = new LoginViewModel();
        loginView = new LoginView(loginViewModel);
        cardPanel.add(loginView, loginView.getViewName());
        return this;
    }

    public AppBuilder addLoggedInView() {
        searchListingViewModel = new SearchListingViewModel();
        searchView = new SearchView(searchListingViewModel);
        cardPanel.add(searchView, searchView.getViewName());
        return this;
    }

    public AppBuilder addSignupUseCase() {
        final SignupOutputBoundary signupOutputBoundary = new SignupPresenter(viewManagerModel,
                signupViewModel, loginViewModel);
        final SignupInputBoundary userSignupInteractor = new SignupInteractor(
                userDataAccessObject, signupOutputBoundary);

        SignupController controller = new SignupController(userSignupInteractor);
        signupView.setSignupController(controller);
        return this;
    }

    public AppBuilder addLoginUseCase() {
        final LoginOutputBoundary loginOutputBoundary = new LoginPresenter(viewManagerModel,
                searchListingViewModel, loginViewModel,signupViewModel);
        final LoginInputBoundary loginInteractor = new LoginInteractor(
                userDataAccessObject, loginOutputBoundary);

        LoginController loginController = new LoginController(loginInteractor);
        loginView.setLoginController(loginController);
        return this;
    }

    public AppBuilder addSearchListingUseCase() {
        SearchListingOutputBoundary searchPresenter =
                new SearchListingPresenter(searchListingViewModel);

        SearchListingDataAccessInterface listingDataAccessObject = new InMemoryListingDataAccessObject();
        SearchListingInputBoundary searchInteractor =
                new SearchListingInteractor(listingDataAccessObject, searchPresenter);
        SearchListingController searchController = new SearchListingController(searchInteractor);

        // Create filter use case
        DistanceService distanceService = new GoogleDistanceService();
        FilterListingsOutputBoundary filterPresenter =
                new interface_adapter.filter.FilterListingsPresenter(searchListingViewModel);
        FilterListingsInputBoundary filterInteractor =
                new FilterListingsInteractor(listingDataAccessObject, distanceService, filterPresenter);
        filterController = new FilterListingsController(filterInteractor);

        // Now recreate LoggedInView with the controllers
        cardPanel.remove(loggedInView);
        loggedInView = new LoggedInView(loggedInViewModel, searchListingViewModel,
                searchController, filterController);
        cardPanel.add(loggedInView, loggedInView.getViewName());

        return this;
    }

    public JFrame build() {
        final JFrame application = new JFrame("User Login Example");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);

        viewManagerModel.setState(signupView.getViewName());
        viewManagerModel.firePropertyChange();

        return application;
    }

}

