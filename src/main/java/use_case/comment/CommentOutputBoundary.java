package use_case.comment;

public interface CommentOutputBoundary {
    void prepareSuccessView(CommentOutputData outputData);
    void prepareFailView(String errorMessage);
}
