package interface_adapter.comment;
import use_case.comment.*;
import Entities.Listing;
import Entities.User;

public class CommentController {

    private final CommentInputBoundary interactor;

    public CommentController(CommentInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void addComment(String id, Listing listing, User user, String content) {
        CommentInputData inputData = new CommentInputData(id, listing, user, content);
        interactor.execute(inputData);
    }
}
