package interface_adapter.messaging;

import use_case.messaging.SendMessageInputBoundary;
import use_case.messaging.SendMessageInputData;
import use_case.messaging.SendMessageOutputData;

public class MessageController {
    private final SendMessageInputBoundary interactor;

    public MessageController(SendMessageInputBoundary interactor) {
        this.interactor = interactor;
    }

    public SendMessageOutputData sendMessage(String from, String to,
                                             String listingId, String content) {
        SendMessageInputData inputData = new SendMessageInputData(from, to, listingId, content);
        return interactor.execute(inputData);
    }
}