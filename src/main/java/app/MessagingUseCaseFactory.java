package app;

import data_access.InMemoryMessageDAO;
import interface_adapter.messaging.*;
import use_case.messaging.*;

public class MessagingUseCaseFactory {

    public static MessageController createMessagingUseCase(
            MessageViewModel viewModel,
            MessageDataAccessInterface dataAccess) {

        SendMessageOutputBoundary presenter = new MessagePresenter(viewModel);
        SendMessageInputBoundary interactor = new SendMessageInteractor(dataAccess, presenter);
        return new MessageController(interactor);
    }
}