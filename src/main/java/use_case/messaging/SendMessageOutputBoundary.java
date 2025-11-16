package use_case.messaging;

public interface SendMessageOutputBoundary {
    void prepareSuccessView(SendMessageOutputData outputData);
    void prepareFailView(SendMessageOutputData outputData);
}