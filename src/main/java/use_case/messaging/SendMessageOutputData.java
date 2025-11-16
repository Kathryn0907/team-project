package use_case.messaging;

import Entities.Message;

public class SendMessageOutputData {
    private final Message message;
    private final boolean success;
    private final String errorMessage;

    public SendMessageOutputData(Message message, boolean success, String errorMessage) {
        this.message = message;
        this.success = success;
        this.errorMessage = errorMessage;
    }

    public Message getMessage() { return message; }
    public boolean isSuccess() { return success; }
    public String getErrorMessage() { return errorMessage; }
}