package app;

import data_access.InMemoryListingDataAccessObject;
import interface_adapter.ViewManagerModel;
import interface_adapter.check_favorite.*;
import use_case.check_favorite.*;

public class CheckFavoriteUseCaseFactory {

    public static CheckFavoriteController createCheckFavoriteUseCase(
            ViewManagerModel viewManagerModel,
            CheckFavoriteViewModel checkFavoriteViewModel,
            InMemoryListingDataAccessObject dataAccess) {

        CheckFavoriteOutputBoundary presenter =
                new CheckFavoritePresenter(viewManagerModel, checkFavoriteViewModel);
        CheckFavoriteInputBoundary interactor =
                new CheckFavoriteInteractor(dataAccess, presenter);
        return new CheckFavoriteController(interactor);
    }
}