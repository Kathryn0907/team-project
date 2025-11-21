package app;

import javax.swing.*;
import java.awt.*;

import data_access.InMemoryListingDataAccessObject;
import data_access.InMemoryUserDataAccessObject;
import data_access.GoogleDistanceService;
import interface_adapter.ViewManagerModel;
import interface_adapter.comment.CommentPresenter;
import interface_adapter.filter.FilterListingsController;
import interface_adapter.logged_in.LoggedInViewModel;
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
import use_case.search_listings.SearchListingInputBoundary;
import use_case.search_listings.SearchListingInteractor;
import use_case.search_listings.SearchListingOutputBoundary;
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInteractor;
import use_case.signup.SignupOutputBoundary;
import view.LoggedInView;
import view.LoginView;
import view.SignupView;
import view.ViewManager;

import interface_adapter.listing_detail.ListingDetailViewModel;
import interface_adapter.comment.CommentViewModel;
import interface_adapter.comment.CommentController;
import view.ListingDetailView;

public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();

    final ViewManagerModel viewManagerModel = ViewManagerModel.getInstance();
    ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    // Data access objects
    public final InMemoryUserDataAccessObject userDataAccessObject = new InMemoryUserDataAccessObject();
    public final InMemoryListingDataAccessObject listingDataAccessObject = new InMemoryListingDataAccessObject();

    private SignupView signupView;
    private SignupViewModel signupViewModel;
    private LoginViewModel loginViewModel;
    private LoggedInViewModel loggedInViewModel;
    private SearchListingViewModel searchListingViewModel;
    private LoggedInView loggedInView;
    private LoginView loginView;

    // Controllers that will be created in use case methods
    private SearchListingController searchController;
    private FilterListingsController filterController;

    private ListingDetailViewModel listingDetailViewModel;
    private CommentViewModel commentViewModel;
    private ListingDetailView listingDetailView;

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
        loggedInViewModel = new LoggedInViewModel();
        searchListingViewModel = new SearchListingViewModel();

        // Note: Controllers will be injected by addSearchListingUseCase()
        // Create the logged in view (controllers will be set later)
        loggedInView = new LoggedInView(loggedInViewModel, searchListingViewModel,
                null, null);  // Controllers added later
        cardPanel.add(loggedInView, loggedInView.getViewName());
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
                loggedInViewModel, loginViewModel, signupViewModel);
        final LoginInputBoundary loginInteractor = new LoginInteractor(
                userDataAccessObject, loginOutputBoundary);

        LoginController loginController = new LoginController(loginInteractor);
        loginView.setLoginController(loginController);
        return this;
    }

    public AppBuilder addSearchListingUseCase() {
        // Create search use case
        SearchListingOutputBoundary searchPresenter =
                new SearchListingPresenter(searchListingViewModel);
        SearchListingInputBoundary searchInteractor =
                new SearchListingInteractor(listingDataAccessObject, searchPresenter);
        searchController = new SearchListingController(searchInteractor);

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

    public AppBuilder addListingDetailViewAndCommentUseCase() {
        listingDetailViewModel = ListingDetailViewModel.getInstance();
        commentViewModel = new CommentViewModel();

        CommentController commentController = CommentUseCaseFactory.create(viewManagerModel, commentViewModel);

        listingDetailView = new ListingDetailView(
                listingDetailViewModel,
                commentController,
                commentViewModel
        );

        cardPanel.add(listingDetailView, ListingDetailViewModel.VIEW_NAME);

        return this;
    }

    public JFrame build() {
        final JFrame application = new JFrame("Airbnb Listing Browser");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);

        viewManagerModel.setState(signupView.getViewName());
        viewManagerModel.firePropertyChange();

        return application;
    }
}