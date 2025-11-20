package data_access;

import Entities.Comment;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;

public class MongoDBCommentDAO {

    private final MongoCollection<Document> commentsCollection;
    private MongoDBExtractToCache data;

    public MongoDBCommentDAO() {
        MongoClient client = MongoConfig.getClient();
        MongoDatabase db = client.getDatabase("CSC207_group_project_2025");
        this.commentsCollection = db.getCollection("Comments");
        this.data = new MongoDBExtractToCache();
    }


    /**
     * Save or update the comment to Database.
     * To update, make sure the Id is correct.
     * @param comment the comment.
     */
    public void saveComment(Comment comment) {

        if (commentsCollection.find(Filters.eq("idForDB", comment.getIdForDB())).first() != null) {
            commentsCollection.deleteOne(Filters.eq("idForDB", comment.getIdForDB()));
        }

        Document doc = new Document()
                .append("id", comment.getId())
                .append("content", comment.getContent())
                .append("userId", comment.getUser().getId())
                .append("listingId", comment.getListing().getId())
                .append("createdAt", comment.getCreatedAt().getEpochSecond())
                .append("idForDB", comment.getIdForDB());

        commentsCollection.insertOne(doc);
    }



    public void deleteComment(Comment comment) {
        ObjectId commentId = comment.getIdForDB();
        if (commentsCollection.find(Filters.eq("idForDB", commentId)).first() != null) {
            commentsCollection.deleteOne(Filters.eq("idForDB", commentId));
        } else {
            System.out.println("Comment with Database id: " + commentId +  " and String id: "
                    + comment.getId() +" not found in database");
        }
    }




    /**
     * Find the comment in local cache. You can use refresh method if you just
     * save something but didn't find it.
     * @param idForDB The idForDB of the comment, not the String id.
     * @return null if not found.
     */
    public Comment findCommentById(ObjectId idForDB) {
        return data.findCommentById(idForDB);
    }

    /**
     * Refresh the local cache.
     */
    public void refreshData() {
        this.data = new MongoDBExtractToCache();
    }


    /**
     * Get all comments in local cache. You can use refresh method if you just
     * save something but didn't find it.
     * @return A list of comments.
     */
    public ArrayList<Comment> getAllComments() {
        return data.getCommentsCache();
    }
}
