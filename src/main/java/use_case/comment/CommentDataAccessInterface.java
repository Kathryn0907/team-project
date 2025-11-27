package use_case.comment;

import Entities.Comment;
import Entities.Listing;
import Entities.User;

public interface CommentDataAccessInterface {

    void saveComment(Comment comment);

    void updateListing(Listing listing);

    void updateUser(User user);
}
