package interface_adapter.extract_tags;

import use_case.extract_tags.ExtractTagsOutputBoundary;
import use_case.extract_tags.ExtractTagsOutputData;

public class ExtractTagsPresenter implements ExtractTagsOutputBoundary {
    private ExtractTagsViewModel viewModel;

    public ExtractTagsPresenter(ExtractTagsViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(ExtractTagsOutputData outputData) {
        ExtractTagsState state = new ExtractTagsState();
        state.setExtractedTags(outputData.getExtractedTags());
        state.setCategories(outputData.getCategories());
        state.setSuccess(true);
        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        ExtractTagsState state = new ExtractTagsState();
        state.setErrorMessage(errorMessage);
        state.setSuccess(false);
        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }
}