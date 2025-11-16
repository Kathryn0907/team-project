package interface_adapter.messaging;

import use_case.messaging.SendMessageOutputBoundary;
import use_case.messaging.SendMessageOutputData;

public class MessagePresenter implements SendMessageOutputBoundary {
    private MessageViewModel viewModel;

    public MessagePresenter(MessageViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(SendMessageOutputData outputData) {
        MessageState state = new MessageState();
        state.setMessage(outputData.getMessage());
        state.setSuccess(true);
        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(SendMessageOutputData outputData) {
        MessageState state = new MessageState();
        state.setErrorMessage(outputData.getErrorMessage());
        state.setSuccess(false);
        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }
}