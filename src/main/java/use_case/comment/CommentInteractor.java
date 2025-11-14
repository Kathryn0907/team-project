package use_case.comment;

import Entities.Comment;
import Entities.Listing;
import Entities.User;

import java.time.Instant;

public class CommentInteractor implements CommentInputBoundary {

    private final CommentOutputBoundary presenter;

    public CommentInteractor(CommentOutputBoundary presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute(CommentInputData inputData){
        //1: Validate comment content
        String content = inputData.getContent();
        if(content == null || content.trim().isEmpty()){
            presenter.prepareFailView("Comment cannot be empty");
            return;
        }

        //2: Check listing availability
        Listing listing = inputData.getListing();
        if(listing == null){
            presenter.prepareFailView("Listing unavailable for commenting");
            return;
        }

        //3: Create comment
        User user = inputData.getUser();
        Comment newComment = new Comment(
                inputData.getCommentId(),
                inputData.getContent(),
                user,
                listing,
                Instant.now()
        );

        //4: Add comment to listing
        listing.getComments().add(newComment);

        //5: prepare success output
        CommentOutputData outputData = new CommentOutputData(newComment);
        presenter.prepareSuccessView(outputData);
    }
}
