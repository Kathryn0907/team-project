package use_case.messaging;

public interface SendMessageInputBoundary {
    SendMessageOutputData execute(SendMessageInputData inputData);
}