package interface_adapter.extract_tags;

import use_case.extract_tags.ExtractTagsInputBoundary;
import use_case.extract_tags.ExtractTagsInputData;
import use_case.extract_tags.ExtractTagsOutputData;

public class ExtractTagsController {
    private final ExtractTagsInputBoundary interactor;

    public ExtractTagsController(ExtractTagsInputBoundary interactor) {
        this.interactor = interactor;
    }

    public ExtractTagsOutputData execute(String listingName, String imageUrl) {
        ExtractTagsInputData inputData = new ExtractTagsInputData(listingName, imageUrl);
        return interactor.execute(inputData);
    }
}