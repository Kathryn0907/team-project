package app;

import interface_adapter.ViewManagerModel;
import interface_adapter.comment.CommentController;
import interface_adapter.comment.CommentPresenter;
import interface_adapter.comment.CommentViewModel;
import use_case.comment.CommentInputBoundary;
import use_case.comment.CommentInteractor;
import use_case.comment.CommentOutputBoundary;

public class CommentUseCaseFactory {

    private CommentUseCaseFactory() {}

    public static CommentController create(
            ViewManagerModel viewManagerModel,
            CommentViewModel commentViewModel
    ) {

        // 1. Presenter
        CommentOutputBoundary presenter =
                new CommentPresenter(viewManagerModel, commentViewModel);

        // 2. Interactor
        CommentInputBoundary interactor =
                new CommentInteractor(presenter);

        // 3. Controller
        return new CommentController(interactor);
    }
}
