package app;

import data_access.InMemoryListingDAO;
import data_access.ImaggaAPIService;
import interface_adapter.extract_tags.*;
import use_case.extract_tags.*;

public class ExtractTagsUseCaseFactory {

    public static ExtractTagsController createExtractTagsUseCase(
            ExtractTagsViewModel viewModel,
            InMemoryListingDAO dataAccess,
            ImaggaAPIService imaggaService) {

        ExtractTagsOutputBoundary presenter = new ExtractTagsPresenter(viewModel);
        ExtractTagsInputBoundary interactor = new ExtractTagsInteractor(
                dataAccess, imaggaService, presenter
        );
        return new ExtractTagsController(interactor);
    }
}