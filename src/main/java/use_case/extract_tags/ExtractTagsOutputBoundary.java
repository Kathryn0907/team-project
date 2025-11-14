package use_case.extract_tags;

public interface ExtractTagsOutputBoundary {
    void prepareSuccessView(ExtractTagsOutputData outputData);
    void prepareFailView(String errorMessage);
}