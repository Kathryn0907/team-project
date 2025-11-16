package interface_adapter.messaging;

import Entities.Message;

public class MessageState {
    private Message message;
    private String errorMessage = "";
    private boolean success = false;

    public Message getMessage() { return message; }
    public void setMessage(Message message) { this.message = message; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
}