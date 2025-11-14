package use_case.comment;
import Entities.Listing;
import Entities.User;

public class CommentInputData {

    private final String commentId;
    private final Listing listing;
    private final User user;
    private final String content;

    public CommentInputData(String commentId, Listing listing, User user, String content){
        this.commentId = commentId;
        this.listing = listing;
        this.user = user;
        this.content = content;
    }

    public String getCommentId() {
        return commentId;
    }

    public Listing getListing(){
        return listing;
    }

    public User getUser(){
        return user;
    }

    public String getContent(){
        return content;
    }
}
