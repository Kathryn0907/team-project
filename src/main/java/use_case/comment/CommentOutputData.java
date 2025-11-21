package use_case.comment;

import Entities.Comment;

public class CommentOutputData {

    private final Comment newComment;

    public CommentOutputData(Comment newComment) {
        this.newComment = newComment;
    }

    public Comment getNewComment() {
        return newComment;
    }
}
