package interface_adapter.comment;
import Entities.Comment;
import interface_adapter.ViewModel;

public class CommentViewModel extends ViewModel {

    public static final String VIEW_NAME = "comment";
    private CommentState state = new CommentState();

    public CommentViewModel() {
        super(VIEW_NAME);
    }

    public CommentState getState(){
        return state;
    }

    public void setState(CommentState state){
        this.state = state;
    }

    public static class CommentState {
        private String errorMessage;
        private String successMessage;
        private Comment newComment;
        private java.util.List<Comment> comments = new java.util.ArrayList<>();

        public java.util.List<Comment> getComments() {
            return comments;
        }

        public void setComments(java.util.List<Comment> comments) {
            this.comments = comments;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public String getSuccessMessage() {
            return successMessage;
        }

        public void setSuccessMessage(String successMessage) {
            this.successMessage = successMessage;
        }

        public Comment getNewComment() {
            return newComment;
        }

        public void setNewComment(Comment newComment) {
            this.newComment = newComment;
        }
    }
}
