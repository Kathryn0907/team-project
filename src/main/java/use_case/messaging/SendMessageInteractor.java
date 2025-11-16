package use_case.messaging;

import Entities.Message;
import java.util.UUID;

public class SendMessageInteractor implements SendMessageInputBoundary {
    private final MessageDataAccessInterface dataAccess;
    private final SendMessageOutputBoundary presenter;

    public SendMessageInteractor(MessageDataAccessInterface dataAccess,
                                 SendMessageOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public SendMessageOutputData execute(SendMessageInputData inputData) {
        // Validate input
        if (inputData.getContent() == null || inputData.getContent().trim().isEmpty()) {
            SendMessageOutputData outputData = new SendMessageOutputData(
                    null, false, "Message content cannot be empty"
            );
            presenter.prepareFailView(outputData);
            return outputData;
        }

        if (inputData.getToUsername() == null || inputData.getToUsername().isEmpty()) {
            SendMessageOutputData outputData = new SendMessageOutputData(
                    null, false, "Recipient username is required"
            );
            presenter.prepareFailView(outputData);
            return outputData;
        }

        // Create message
        String messageId = UUID.randomUUID().toString();
        Message message = new Message(
                messageId,
                inputData.getFromUsername(),
                inputData.getToUsername(),
                inputData.getListingId(),
                inputData.getContent()
        );

        // Save to database
        try {
            dataAccess.saveMessage(message);

            SendMessageOutputData outputData = new SendMessageOutputData(
                    message, true, null
            );
            presenter.prepareSuccessView(outputData);
            return outputData;

        } catch (Exception e) {
            SendMessageOutputData outputData = new SendMessageOutputData(
                    null, false, "Failed to send message: " + e.getMessage()
            );
            presenter.prepareFailView(outputData);
            return outputData;
        }
    }
}