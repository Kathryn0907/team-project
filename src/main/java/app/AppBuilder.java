package app;

import javax.swing.*;
import java.awt.*;

import data_access.InMemoryListingDataAccessObject;
import data_access.InMemoryUserDataAccessObject;
import data_access.GoogleDistanceService;
import interface_adapter.ProfileViewModel;
import interface_adapter.ViewManagerModel;
import interface_adapter.create_listing.CreateListingController;
import interface_adapter.create_listing.CreateListingPresenter;
import interface_adapter.create_listing.CreateListingViewModel;
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
import interface_adapter.save_favorite.SaveFavoriteController;
import interface_adapter.save_favorite.SaveFavoritePresenter;
import interface_adapter.save_favorite.SaveFavoriteViewModel;
import interface_adapter.check_favorite.CheckFavoriteController;
import interface_adapter.check_favorite.CheckFavoritePresenter;
import interface_adapter.check_favorite.CheckFavoriteViewModel;
import use_case.create_listing.CreateListingInputBoundary;
import use_case.create_listing.CreateListingInteractor;
import use_case.create_listing.CreateListingOutputBoundary;
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
import use_case.save_favorite.SaveFavoriteInputBoundary;
import use_case.save_favorite.SaveFavoriteInteractor;
import use_case.save_favorite.SaveFavoriteOutputBoundary;
import use_case.check_favorite.CheckFavoriteInputBoundary;
import use_case.check_favorite.CheckFavoriteInteractor;
import use_case.check_favorite.CheckFavoriteOutputBoundary;
import view.*;

/**
 * AppBuilder with Favorites functionality integrated
 * Updated by Jonathan (Use Case 9 & 14)
 */
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
    private SaveFavoriteViewModel saveFavoriteViewModel;
    private CheckFavoriteViewModel checkFavoriteViewModel;
    private LoggedInView loggedInView;
    private LoginView loginView;
    private CheckFavoriteView checkFavoriteView;
    private ProfileViewModel profileViewModel;
    private ProfileView profileView;
    private CreateListingView createListingView;
    private CreateListingViewModel createListingViewModel;

    // Controllers that will be created in use case methods
    private SearchListingController searchController;
    private FilterListingsController filterController;
    private SaveFavoriteController saveController;
    private CheckFavoriteController checkController;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
        userDataAccessObject.setListingDataAccessObject(listingDataAccessObject);
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
        saveFavoriteViewModel = new SaveFavoriteViewModel();
        checkFavoriteViewModel = new CheckFavoriteViewModel();

        // Note: Controllers will be injected by use case methods
        // Create the logged in view (controllers will be set later)
        loggedInView = new LoggedInView(
                loggedInViewModel,
                searchListingViewModel,
                null,  // searchController - added later
                null,  // filterController - added later
                null,  // saveController - added later
                null,  // checkController - added later
                viewManagerModel
        );
        cardPanel.add(loggedInView, loggedInView.getViewName());
        return this;
    }

    public AppBuilder addCheckFavoriteView() {
        if (checkFavoriteViewModel == null) {
            checkFavoriteViewModel = new CheckFavoriteViewModel();
        }

        checkFavoriteView = new CheckFavoriteView(checkFavoriteViewModel, viewManagerModel);
        cardPanel.add(checkFavoriteView, checkFavoriteView.getViewName());
        return this;
    }

    public AppBuilder addCreateListingView() {
        createListingViewModel = new CreateListingViewModel();
        createListingView = new CreateListingView(createListingViewModel);
        cardPanel.add(createListingView, createListingView.getViewName());
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

        return this;
    }

    public AppBuilder addSaveFavoriteUseCase() {
        // Create save favorite use case
        SaveFavoriteOutputBoundary savePresenter =
                new SaveFavoritePresenter(saveFavoriteViewModel);
        SaveFavoriteInputBoundary saveInteractor =
                new SaveFavoriteInteractor(userDataAccessObject, savePresenter);
        saveController = new SaveFavoriteController(saveInteractor);

        return this;
    }

    public AppBuilder addCheckFavoriteUseCase() {
        CheckFavoriteOutputBoundary checkPresenter =
                new CheckFavoritePresenter(viewManagerModel, checkFavoriteViewModel);

        // ✅ Use userDataAccessObject, which now implements CheckFavoriteDataAccessInterface
        CheckFavoriteInputBoundary checkInteractor =
                new CheckFavoriteInteractor(userDataAccessObject, checkPresenter);

        checkController = new CheckFavoriteController(checkInteractor);
        return this;
    }

    public AppBuilder addCreateListingUseCase() {
        final CreateListingOutputBoundary createListingPresenter =
                new CreateListingPresenter(
                        createListingViewModel,
                        profileViewModel,
                        viewManagerModel
                );
        final CreateListingInputBoundary createListingInteractor =
                new CreateListingInteractor(listingDataAccessObject, createListingPresenter);

        final CreateListingController createListingController =
                new CreateListingController(createListingInteractor);
        createListingView.setCreateListingController(createListingController);

        return this;
    }

    public AppBuilder rebuildLoggedInView() {
        // Now recreate LoggedInView with all the controllers
        cardPanel.remove(loggedInView);
        loggedInView = new LoggedInView(
                loggedInViewModel,
                searchListingViewModel,
                searchController,
                filterController,
                saveController,
                checkController,
                viewManagerModel
        );
        cardPanel.add(loggedInView, loggedInView.getViewName());

        return this;
    }

    public AppBuilder addProfileView() {
        profileViewModel = new ProfileViewModel();
        profileView = new ProfileView(profileViewModel, viewManagerModel);
        cardPanel.add(profileView, profileView.getViewName());
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