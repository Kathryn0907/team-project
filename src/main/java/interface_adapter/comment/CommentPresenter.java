package interface_adapter.comment;

import interface_adapter.ViewManagerModel;
import interface_adapter.comment.CommentViewModel;
import use_case.comment.CommentOutputBoundary;
import use_case.comment.CommentOutputData;

public class CommentPresenter implements CommentOutputBoundary {

    private final ViewManagerModel viewManagerModel;
    private final CommentViewModel commentViewModel;

    public CommentPresenter(ViewManagerModel viewManagerModel,
                            CommentViewModel commentViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.commentViewModel = commentViewModel;
    }

    @Override
    public void prepareSuccessView(CommentOutputData response){
        CommentViewModel.CommentState state = commentViewModel.getState();

        state.setErrorMessage(null);
        state.setSuccessMessage("Comment added");
        state.setNewComment(response.getNewComment());

        commentViewModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String error){
        CommentViewModel.CommentState state = commentViewModel.getState();

        state.setErrorMessage(error);
        state.setSuccessMessage(null);
        state.setNewComment(null);

        commentViewModel.firePropertyChange();
    }
}
