package app;

import data_access.ListingDataAccessObject;
import interface_adapter.my_listings.*;
import use_case.my_listings.*;

public class MyListingsUseCaseFactory {

    public static MyListingsController createMyListingsUseCase(
            MyListingsViewModel viewModel,
            ListingDataAccessObject dataAccess) {

        MyListingsOutputBoundary presenter = new MyListingsPresenter(viewModel);
        MyListingsInputBoundary interactor = new MyListingsInteractor(dataAccess, presenter);
        return new MyListingsController(interactor);
    }
}