package app;

import data_access.InMemoryListingDataAccessObject;
import interface_adapter.save_favorite.*;
import use_case.save_favorite.*;

public class SaveFavoriteUseCaseFactory {

    public static SaveFavoriteController createSaveFavoriteUseCase(
            SaveFavoriteViewModel viewModel,
            InMemoryListingDataAccessObject dataAccess) {

        SaveFavoriteOutputBoundary presenter = new SaveFavoritePresenter(viewModel);
        SaveFavoriteInputBoundary interactor = new SaveFavoriteInteractor(dataAccess, presenter);
        return new SaveFavoriteController(interactor);
    }
}