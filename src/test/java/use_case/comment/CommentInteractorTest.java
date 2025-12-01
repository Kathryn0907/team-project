package use_case.comment;

import Entities.Comment;
import Entities.Listing;
import Entities.User;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Full coverage tests for CommentInteractor
 * Covers: success, empty content, null content, inactive listing, null listing
 */
public class CommentInteractorTest {

    /** Fake in-memory DAO for testing */
    private static class InMemoryCommentDAO implements CommentDataAccessInterface {

        int saveCommentCount = 0;
        int updateListingCount = 0;
        int updateUserCount = 0;

        Comment savedComment = null;

        @Override
        public void saveComment(Comment comment) {
            saveCommentCount++;
            savedComment = comment;
        }

        @Override
        public void updateListing(Listing listing) {
            updateListingCount++;
        }

        @Override
        public void updateUser(User user) {
            updateUserCount++;
        }
    }

    /** Fake presenter used to check output data */
    private static class TestPresenter implements CommentOutputBoundary {

        CommentOutputData successData = null;
        String errorMessage = null;

        @Override
        public void prepareSuccessView(CommentOutputData outputData) {
            successData = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }

    /** Helper to create an active listing */
    private Listing createActiveListing() {
        Listing l = new Listing();
        l.setActive(true);
        l.setComments(new ArrayList<>());
        return l;
    }

    /** Helper to create an inactive listing */
    private Listing createInactiveListing() {
        Listing l = new Listing();
        l.setActive(false);
        l.setComments(new ArrayList<>());
        return l;
    }

    /** Helper to create test user */
    private User createUser() {
        return new User("userA", "pass123");
    }

    /** 1. SUCCESS CASE */
    @Test
    public void testExecute_success() {
        InMemoryCommentDAO dao = new InMemoryCommentDAO();
        TestPresenter presenter = new TestPresenter();
        CommentInteractor interactor = new CommentInteractor(presenter, dao);

        Listing listing = createActiveListing();
        User user = createUser();

        CommentInputData inputData =
                new CommentInputData("c1", listing, user, "Nice place!");

        interactor.execute(inputData);

        // DAO side
        assertEquals(1, dao.saveCommentCount);
        assertEquals(1, dao.updateListingCount);
        assertEquals(1, dao.updateUserCount);
        assertNotNull(dao.savedComment);

        // Listing should have 1 comment
        assertEquals(1, listing.getComments().size());
        assertSame(dao.savedComment, listing.getComments().get(0));

        // Presenter side
        assertNull(presenter.errorMessage);
        assertNotNull(presenter.successData);
        assertSame(dao.savedComment, presenter.successData.getNewComment());
    }

    /** 2. EMPTY CONTENT (trim empty) */
    @Test
    public void testExecute_emptyContent() {
        InMemoryCommentDAO dao = new InMemoryCommentDAO();
        TestPresenter presenter = new TestPresenter();
        CommentInteractor interactor = new CommentInteractor(presenter, dao);

        Listing listing = createActiveListing();
        User user = createUser();

        CommentInputData inputData =
                new CommentInputData("c2", listing, user, "   ");

        interactor.execute(inputData);

        // No changes should occur
        assertEquals(0, dao.saveCommentCount);
        assertEquals(0, listing.getComments().size());
        assertEquals("Comment cannot be empty", presenter.errorMessage);
        assertNull(presenter.successData);
    }

    /** 3. NULL CONTENT (new branch) */
    @Test
    public void testExecute_nullContent() {
        InMemoryCommentDAO dao = new InMemoryCommentDAO();
        TestPresenter presenter = new TestPresenter();
        CommentInteractor interactor = new CommentInteractor(presenter, dao);

        Listing listing = createActiveListing();
        User user = createUser();

        CommentInputData inputData =
                new CommentInputData("c3", listing, user, null);

        interactor.execute(inputData);

        // Should fail on null content branch
        assertEquals(0, dao.saveCommentCount);
        assertEquals("Comment cannot be empty", presenter.errorMessage);
        assertNull(presenter.successData);
    }

    /** 4. UNAVAILABLE LISTING */
    @Test
    public void testExecute_unavailableListing() {
        InMemoryCommentDAO dao = new InMemoryCommentDAO();
        TestPresenter presenter = new TestPresenter();
        CommentInteractor interactor = new CommentInteractor(presenter, dao);

        Listing listing = createInactiveListing();
        User user = createUser();

        CommentInputData inputData =
                new CommentInputData("c4", listing, user, "Hello");

        interactor.execute(inputData);

        // No save should happen
        assertEquals(0, dao.saveCommentCount);
        assertEquals(0, listing.getComments().size());
        assertEquals("Listing unavailable for commenting", presenter.errorMessage);
        assertNull(presenter.successData);
    }

    /** 5. NULL LISTING (new branch) */
    @Test
    public void testExecute_nullListing() {
        InMemoryCommentDAO dao = new InMemoryCommentDAO();
        TestPresenter presenter = new TestPresenter();
        CommentInteractor interactor = new CommentInteractor(presenter, dao);

        User user = createUser();

        CommentInputData inputData =
                new CommentInputData("c5", null, user, "Hello world");

        interactor.execute(inputData);

        // Should hit listing == null branch
        assertEquals(0, dao.saveCommentCount);
        assertEquals("Listing unavailable for commenting", presenter.errorMessage);
        assertNull(presenter.successData);
    }
}
